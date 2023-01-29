package com.eurya.javaide.completion.provider;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.eurya.javaide.compiler.JavaCompilerService;
import com.eurya.javaide.completion.CompileTask;
import com.eurya.javaide.manager.FileManager;
import com.eurya.javaide.model.CompletionList;
import com.eurya.javaide.model.Project;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CompletionEngine {
    private static final String TAG = CompletionEngine.class.getSimpleName();

    public static CompletionEngine Instance = null;

    public static CompletionEngine getInstance() {
        if (Instance == null) {
            Instance = new CompletionEngine();
        }
        return Instance;
    }

    private JavaCompilerService mProvider;
    private boolean mIndexing;

    private CompletionEngine() {
        getCompiler();
    }

    @NonNull
    public JavaCompilerService getCompiler() {
        if (mProvider != null) {
            return mProvider;
        }

        mProvider =
                new JavaCompilerService(
                        FileManager.getInstance().fileClasspath(),
                        Collections.emptySet(),
                        Collections.emptySet());
        return mProvider;
    }

    /** Disable subsequent completions */
    public void setIndexing(boolean val) {
        mIndexing = val;
    }

    public boolean isIndexing() {
        return mIndexing;
    }

    @SuppressLint("NewApi")
    public void index(Project project, Runnable callback) {
        setIndexing(true);

        JavaCompilerService compiler = getCompiler();

        Set<File> filesToIndex = new HashSet<>(project.getJavaFiles().values());
        // filesToIndex.addAll(project.getRJavaFiles().values());

        for (File file : filesToIndex) {
            try {
                CompileTask task = compiler.compile(file.toPath());
                task.close();
            } catch (Throwable ignored) {

            }
        }
        setIndexing(false);
        if (callback != null) {
            // ApplicationLoader.applicationHandler.post(callback);
        }
    }

    @NonNull
    public synchronized CompletionList complete(File file, long cursor) {
        // Do not request for completion if we're indexing
        if (mIndexing) {
            return CompletionList.EMPTY;
        }

        try {
            return new CompletionProvider(mProvider).complete(file, cursor);
        } catch (RuntimeException | AssertionError e) {
            Log.d(TAG, "Completion failed: " + Log.getStackTraceString(e) + " Clearing cache.");
            mProvider = null;
            index(FileManager.getInstance().getCurrentProject(), null);
        }
        return CompletionList.EMPTY;
    }
}
