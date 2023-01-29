package com.eurya.javaide.language.lua;

import com.eurya.javaide.language.lua.LuaErrorListener;
import com.eurya.javaide.language.lua.LuaLexer;
// import com.eurya.javaide.language.lua.LuaParser;

import io.github.rosemoe.sora.data.BlockLine;
import io.github.rosemoe.sora.data.Span;
import io.github.rosemoe.sora.interfaces.CodeAnalyzer;
import io.github.rosemoe.sora.text.TextAnalyzeResult;
import io.github.rosemoe.sora.text.TextAnalyzer;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.EditorColorScheme;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.util.Stack;

public class LuaAnalyzer implements CodeAnalyzer {

    private CodeEditor mEditor;

    public LuaAnalyzer(CodeEditor editor) {
        mEditor = editor;
    }

    @Override
    public void analyze(
            CharSequence content,
            TextAnalyzeResult colors,
            TextAnalyzer.AnalyzeThread.Delegate delegate) {
        CodePointCharStream stream = CharStreams.fromString(String.valueOf(content));
        LuaLexer lexer = new LuaLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        /*LuaParser parser = new LuaParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new LuaErrorListener(colors));
        parser.chunk();*/
        Token token, previous = null;
        int maxSwitch = 1, currSwitch = 0;
        int lastLine = 1;
        int line, column;
        while (delegate.shouldAnalyze()) {
            token = lexer.nextToken();
            if (token == null) break;
            if (token.getType() == LuaLexer.EOF) {
                lastLine = token.getLine() - 1;
                break;
            }
            line = token.getLine() - 1;
            column = token.getCharPositionInLine();
            lastLine = line;
            switch (token.getType()) {
                case LuaLexer.COMMENT:
                    colors.addIfNeeded(line, column, EditorColorScheme.COMMENT);
                    break;
                case LuaLexer.NAME:
                    colors.addIfNeeded(line, column, EditorColorScheme.IDENTIFIER_NAME);
                    break;
                case LuaLexer.T__2:
                case LuaLexer.T__3:
                case LuaLexer.T__4:
                case LuaLexer.T__5:
                case LuaLexer.T__6:
                case LuaLexer.T__7:
                case LuaLexer.T__8:
                case LuaLexer.T__9:
                case LuaLexer.T__10:
                case LuaLexer.T__11:
                case LuaLexer.T__12:
                case LuaLexer.T__13:
                case LuaLexer.T__15:
                case LuaLexer.T__16:
                case LuaLexer.T__17:
                case LuaLexer.T__18:
                case LuaLexer.T__22:
                case LuaLexer.T__23:
                case LuaLexer.T__24:
                case LuaLexer.T__32:
                case LuaLexer.T__33:
                case LuaLexer.T__46:
                    colors.addIfNeeded(line, column, EditorColorScheme.KEYWORD);
                    break;
                case LuaLexer.T__30:
                    break;
                case LuaLexer.T__31:
                    break;
                case LuaLexer.FLOAT:
                case LuaLexer.INT:
                case LuaLexer.HEX_FLOAT:
                case LuaLexer.HEX:
                    colors.addIfNeeded(line, column, EditorColorScheme.KEYWORD);
                    break;
                case LuaLexer.CHARSTRING:
                case LuaLexer.LONGSTRING:
                case LuaLexer.NORMALSTRING:
                    colors.addIfNeeded(line, column, EditorColorScheme.LITERAL);
                    break;
                default:
                    colors.addIfNeeded(line, column, EditorColorScheme.TEXT_NORMAL);
                    break;
            }
        }
    }
}
