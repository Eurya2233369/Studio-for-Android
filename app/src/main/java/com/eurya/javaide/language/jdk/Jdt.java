package com.eurya.javaide.language.jdk;

import com.eurya.javaide.language.Language;
import io.github.rosemoe.sora.interfaces.EditorLanguage;
import io.github.rosemoe.sora.widget.CodeEditor;
import java.io.File;

public class Jdt implements Language {

    @Override
    public boolean isApplicable(File ext) {
        // is Java File
        return ext.getName().endsWith(".java");
    }

    @Override
    public EditorLanguage get(CodeEditor editor) {
        return new JavaLanguage(editor);
    }
}
