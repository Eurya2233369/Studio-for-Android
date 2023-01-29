package com.eurya.javaide.completion;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.eurya.javaide.manager.FileManager;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

import org.openjdk.javax.tools.JavaFileObject;
import org.openjdk.javax.tools.SimpleJavaFileObject;

@SuppressLint("NewApi")
public class SourceFileObject extends SimpleJavaFileObject {

	public Path mFile;
	private final Instant modified;
	private String mContents;
	
	public SourceFileObject(Path file) {
		this(file, null, null);
	}
	
	public SourceFileObject(Path file, String contents, Instant modified) {
		super(file.toUri(), JavaFileObject.Kind.SOURCE);
		mContents = contents;
		mFile = file;
		this.modified = modified;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		if (mContents != null) {
			return mContents;
		}
		return FileManager.readFile(mFile.toFile());
	}
	
	@Override
    public Kind getKind() {
        String name = mFile.getFileName().toString();
        return kindFromExtension(name);
    }

    private static Kind kindFromExtension(String name) {
        for (Kind candidate : Kind.values()) {
            if (name.endsWith(candidate.extension)) {
                return candidate;
            }
        }
        return null;
    }
	
	@Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        return mFile.getFileName().toString().equals(simpleName + kind.extension);
    }
	
	@Override
	public URI toUri() {
		return mFile.toUri();
	}

	@Override
	public long getLastModified() {
		if (modified == null) {
			try {
				return Files.getLastModifiedTime(mFile).toMillis();
			} catch (IOException e) {
				return Instant.EPOCH.toEpochMilli();
			}
		}
		return modified.toEpochMilli();
	}
	
	@NonNull
	@Override
	public String toString() {
		return mFile.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (getClass() != o.getClass()) return false;
		SourceFileObject that = (SourceFileObject) o;
		return this.mFile.equals(that.mFile);
	}

	@Override
	public int hashCode() {
		return mFile.hashCode();
	}
}
