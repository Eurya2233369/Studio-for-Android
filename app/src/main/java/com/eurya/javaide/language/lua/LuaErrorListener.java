package com.eurya.javaide.language.lua;

import io.github.rosemoe.sora.data.Span;
import io.github.rosemoe.sora.text.TextAnalyzeResult;

import io.github.rosemoe.sora.widget.EditorColorScheme;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;

public class LuaErrorListener implements ANTLRErrorListener {
    private TextAnalyzeResult mCodes;

    public LuaErrorListener(TextAnalyzeResult result) {
        mCodes = result;
    }

    @Override
    public void reportContextSensitivity(
            Parser arg0, DFA arg1, int arg2, int arg3, int arg4, ATNConfigSet arg5) {
        // TODO: Implement this method
    }

    @Override
    public void reportAttemptingFullContext(
            Parser arg0, DFA arg1, int arg2, int arg3, BitSet arg4, ATNConfigSet arg5) {
        // TODO: Implement this method
    }

    @Override
    public void reportAmbiguity(
            Parser arg0,
            DFA arg1,
            int arg2,
            int arg3,
            boolean arg4,
            BitSet arg5,
            ATNConfigSet arg6) {
        // TODO: Implement this method
    }

    @Override
    public void syntaxError(
            Recognizer<?, ?> paser,
            Object obj,
            int line,
            int column,
            String str,
            RecognitionException error) {
        Span span = Span.obtain(column, EditorColorScheme.COMMENT);
        span.setUnderlineColor(0xffFF0000);
        mCodes.add(line, span);
    }
}
