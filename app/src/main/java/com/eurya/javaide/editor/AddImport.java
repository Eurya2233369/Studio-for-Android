package com.eurya.javaide.editor;

import android.annotation.SuppressLint;

import org.openjdk.source.tree.Tree;
import org.openjdk.source.util.SourcePositions;
import org.openjdk.source.util.Trees;
import com.eurya.javaide.model.Position;
import com.eurya.javaide.completion.ParseTask;
import java.util.List;
import java.util.Map;
import com.eurya.javaide.model.TextEdit;

import org.openjdk.source.tree.ImportTree;

import java.io.File;

public class AddImport {
    
    private final String className;
    private final File currentFile;
    
    public AddImport(File currentFile, String className) {
        this.className = className;
        this.currentFile = currentFile;
    }

    @SuppressLint("NewApi")
    public Map<File, TextEdit> getText(ParseTask task) {
        Position point = insertPosition(task);
        String text = "import " + className + ";\n";
        TextEdit edit = new TextEdit(point, point, text);
        return Map.of(currentFile, edit);
    }
    
    private Position insertPosition(ParseTask task) {
        List<? extends ImportTree> imports = task.root.getImports();
        for (ImportTree i : imports) {
            String next = i.getQualifiedIdentifier().toString();
            if (className.compareTo(next) < 0) {
                return insertBefore(task, i);
            }
        }
        if (!imports.isEmpty()) {
            ImportTree last = imports.get(imports.size() - 1);
            return insertAfter(task, last);
        }
        if (task.root.getPackageName() != null) {
            return insertAfter(task, task.root.getPackageName());
        }
        return new Position(0, 0);
    }

    private Position insertBefore(ParseTask task, Tree i) {
        SourcePositions pos = Trees.instance(task.task).getSourcePositions();
        long offset = pos.getStartPosition(task.root, i);
        int line = (int) task.root.getLineMap().getLineNumber(offset);
        return new Position(line - 1, 0);
    }

    private Position insertAfter(ParseTask task, Tree i) {
        SourcePositions pos = Trees.instance(task.task).getSourcePositions();
        long offset = pos.getStartPosition(task.root, i);
        int line = (int) task.root.getLineMap().getLineNumber(offset);
        return new Position(line, 0);
    }
}
