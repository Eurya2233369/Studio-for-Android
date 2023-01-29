package com.eurya.javaide.language.lua;

import com.eurya.javaide.language.Language;
import io.github.rosemoe.sora.interfaces.EditorLanguage;
import io.github.rosemoe.sora.widget.CodeEditor;
import java.io.File;

public class Lua implements Language {

    @Override
    public boolean isApplicable(File ext) {
        // is Lua File
        return ext.getName().endsWith(".lua");
    }

    @Override
    public EditorLanguage get(CodeEditor editor) {
        return new LuaLanguage(editor);
    }
}
