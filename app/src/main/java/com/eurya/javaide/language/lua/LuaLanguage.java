package com.eurya.javaide.language.lua;

import io.github.rosemoe.sora.langs.IdentifierAutoComplete;
import java.util.List;

import io.github.rosemoe.sora.data.CompletionItem;
import io.github.rosemoe.sora.interfaces.AutoCompleteProvider;
import io.github.rosemoe.sora.interfaces.CodeAnalyzer;
import io.github.rosemoe.sora.interfaces.EditorLanguage;
import io.github.rosemoe.sora.interfaces.NewlineHandler;
import io.github.rosemoe.sora.text.TextAnalyzeResult;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.SymbolPairMatch;

public class LuaLanguage implements EditorLanguage {

    private final CodeEditor mEditor;
    private final String[] keyword = {
        "and",
        "break",
        "do",
        "else",
        "elseif",
        "if",
        "end",
        "false",
        "for",
        "function",
        "in",
        "local",
        "nil",
        "not",
        "or",
        "repeat",
        "return",
        "then",
        "until",
        "while",
        "goto"
    };

    public LuaLanguage(CodeEditor editor) {
        mEditor = editor;
    }

    @Override
    public CodeAnalyzer getAnalyzer() {
        return new LuaAnalyzer(mEditor);
    }

    @Override
    public AutoCompleteProvider getAutoCompleteProvider() {
        IdentifierAutoComplete autoComplete = new IdentifierAutoComplete();
        autoComplete.setKeywords(keyword);
        return autoComplete;
    }

    @Override
    public boolean isAutoCompleteChar(char ch) {
        return false;
    }

    @Override
    public int getIndentAdvance(String content) {
        return 0;
    }

    @Override
    public boolean useTab() {
        return true;
    }

    @Override
    public CharSequence format(CharSequence text) {
        return text;
    }

    @Override
    public SymbolPairMatch getSymbolPairs() {
        return null;
    }

    @Override
    public NewlineHandler[] getNewlineHandlers() {
        return new NewlineHandler[0];
    }
}
