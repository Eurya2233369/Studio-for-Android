package com.eurya.javaide.utils;

import com.eurya.javaide.BaseApplication;
import com.eurya.javaide.completion.SourceFileManager;
import com.eurya.javaide.utils.Tools;

import org.openjdk.javax.tools.StandardLocation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Main class that holds all the files that ends with "-sources" including
 * android.jar sources
 */
public class Docs {

    public final SourceFileManager fileManager = new SourceFileManager();

    public Docs(Set<File> docPaths) {
        // we include android sources into the list
        File srcZip = androidSourcesZip();

        List<File> sourcePaths = new ArrayList<>(docPaths);
        if (srcZip != NOT_FOUND) {
            sourcePaths.add(srcZip);
        }
        try {
            fileManager.setLocation(StandardLocation.SOURCE_PATH, sourcePaths);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final File NOT_FOUND = new File("");
    private static File cacheAndroidSources;

    private static File androidSourcesZip() {
        if (cacheAndroidSources == null) {
            try {
                cacheAndroidSources = findAndroidSources();
            } catch (IOException e) {
                cacheAndroidSources = NOT_FOUND;
            }
        }

        if (cacheAndroidSources == NOT_FOUND) {
            return NOT_FOUND;
        }

        return cacheAndroidSources;
    }

    // TODO: im gonna bundle the java docs sources in the app assets for now
    // we could let the user download it later.
    private static File findAndroidSources() throws IOException {
        File sourcePath = new File(
                BaseApplication.getContext().getFilesDir(),
                "docs/android-sources.jar"
        );
        if (!sourcePath.exists()) {
            if (!sourcePath.getParentFile().mkdirs()) {
                throw new IOException("Couldn't create directory for android sources");
            }
            Tools.unzipFromAssets(BaseApplication.getContext(),
                    "android-sources.zip", sourcePath.getParent());
        }

        return sourcePath;
    }
}