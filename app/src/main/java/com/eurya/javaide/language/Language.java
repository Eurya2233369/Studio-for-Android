package com.eurya.javaide.language;

import io.github.rosemoe.sora.interfaces.EditorLanguage;
import io.github.rosemoe.sora.widget.CodeEditor;

import java.io.File;

public interface Language {
	
	public boolean isApplicable(File ext);
	
	public EditorLanguage get(CodeEditor editor);
}
