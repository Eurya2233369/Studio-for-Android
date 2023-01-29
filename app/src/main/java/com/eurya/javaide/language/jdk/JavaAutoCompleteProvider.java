package com.eurya.javaide.language.jdk;

import com.eurya.javaide.compiler.CompileBatch;
import com.eurya.javaide.completion.provider.CompletionEngine;
import com.eurya.javaide.manager.FileManager;
import com.eurya.javaide.model.CompletionList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.rosemoe.sora.interfaces.AutoCompleteProvider;
import io.github.rosemoe.sora.data.CompletionItem;
import io.github.rosemoe.sora.text.Cursor;
import io.github.rosemoe.sora.text.TextAnalyzeResult;
import io.github.rosemoe.sora.widget.CodeEditor;

public class JavaAutoCompleteProvider implements AutoCompleteProvider {

    private final CodeEditor mEditor;

    public JavaAutoCompleteProvider(CodeEditor editor) {
        mEditor = editor;
    }

    @Override
    public List<CompletionItem> getAutoCompleteItems(
            String partial, TextAnalyzeResult prev, int line, int column) {
        FileManager.getInstance().save(mEditor.getCurrentFile(), mEditor.getText().toString());

        Cursor cursor = mEditor.getCursor();

        // The previous call hasn't finished
        CompileBatch batch = CompletionEngine.getInstance().getCompiler().cachedCompile;
        if (batch != null && !batch.closed) {
            return Collections.emptyList();
        }
        CompletionList list =
                CompletionEngine.getInstance().complete(mEditor.getCurrentFile(), cursor.getLeft());

        List<CompletionItem> result = new ArrayList<>();

        for (com.eurya.javaide.model.CompletionItem item : list.items) {
            result.add(new CompletionItem(item));
        }

        return result;
    }
}
