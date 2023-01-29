package com.eurya.javaide.language.lua;

import io.github.rosemoe.sora.data.CompletionItem;
import io.github.rosemoe.sora.interfaces.AutoCompleteProvider;
import io.github.rosemoe.sora.text.TextAnalyzeResult;
import java.util.List;

public class LuaCompleteProvider implements AutoCompleteProvider {

    @Override
    public List<CompletionItem> getAutoCompleteItems(
            String prefix, TextAnalyzeResult analyzeResult, int line, int column) {
        // TODO: Implement this method
        return null;
    }
}
