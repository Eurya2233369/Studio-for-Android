package com.eurya.javaide.model;

import android.util.Log;

import com.eurya.javaide.BaseApplication;
import com.eurya.javaide.utils.Tools;
import com.eurya.javaide.utils.StringSearch;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openjdk.javax.lang.model.SourceVersion;

/**
 * Class for storing project data, directories and files
 */
public class Project {
    
    public final File mRoot;
    
    public Map<String, File> javaFiles = new HashMap<>();
    public List<String> jarFiles = new ArrayList<>();

    private final Set<File> libraries = new HashSet<>();
    private final Map<String, File> RJavaFiles = new HashMap<>();

    //TODO: Adjust these values according to build.gradle or manifest
    private final int minSdk = 21;
    private final int targetSdk = 31;

    private String packageName;

    /**
     * Creates a project object from specified root
     */
    public Project(File root) {
        mRoot = root;
        
        findJavaFiles(new File(root, "app/src/main/java"));
    }
    
    private void findJavaFiles(File file) {
        File[] files = file.listFiles();
        
        if (files != null) {
            for (File child : files) {
                if (child.isDirectory()) {
                    findJavaFiles(child);
                } else {
                    if (child.getName().endsWith(".java")) {
                        String packageName = StringSearch.packageName(child);
                        Log.d("PROJECT FIND JAVA", "Found " + child.getAbsolutePath());
                        if (packageName.isEmpty()) {
                            Log.d("Error package empty", child.getAbsolutePath());
                        } else {
                            if (SourceVersion.isName(packageName + "." + child.getName().replace(".java", ""))) {
                                javaFiles.put(packageName + "." + child.getName().replace(".java", ""), child);
                            }
                        }
                    }
                }
            }
        }
    }

    public String getPackageName() {
        if (packageName == null) {
            loadPackageName();
        }
        return packageName;
    }

    private void loadPackageName() {
      //Test 
        packageName = "com.mycompany.application";
    }
    
    public Map<String, File> getJavaFiles() {
        if (javaFiles.isEmpty()) {
            findJavaFiles(getJavaDirectory());
        }
        return javaFiles;
    }

    /*private void searchRJavaFiles(File root) {
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchRJavaFiles(file);
                } else {
                    String packageName = StringSearch.packageName(file);
                    if (!packageName.isEmpty()) {
                        packageName = packageName + "." + file.getName().substring(0, file.getName().lastIndexOf("."));
                        RJavaFiles.put(packageName, file);
                    }
                }
            }
        }
    }
    public Map<String, File> getRJavaFiles() {
        if (RJavaFiles.isEmpty()) {
            searchRJavaFiles(new File(getBuildDirectory(), "gen/comlmycompany/application/R.java"));
        }
        return RJavaFiles;
    }*/

    /*public void searchLibraries() {
        libraries.clear();

        File libPath = new File(getBuildDirectory(), "libs");
        File[] files = libPath.listFiles();
        if (files != null) {
            for (File lib : files) {
                if (lib.isDirectory()) {
                    File check = new File(lib, "classes.jar");
                    if (check.exists()) {
                        libraries.add(check);
                    }
                }
            }
        }
    }
    public Set<File> getLibraries() {
        if (libraries.isEmpty()) {
            LibraryChecker checker = new LibraryChecker(this);
            checker.check();

            searchLibraries();
        }
        return libraries;
    }*/

    /**
     * Clears all the cached files stored in this project, the next time ths project
     * is opened, it will get loaded again
     */
    public void clear() {
        packageName = null;

        RJavaFiles.clear();
        libraries.clear();
        javaFiles.clear();
        libraries.clear();
    }
    /**
     * Used to check if this project contains the required directories
     * such as app/src/main/java, resources and others
     */
    public boolean isValidProject() {
        File check = new File(mRoot, "app/src/main/java");

        if (!check.exists()) {
            return false;
        }

        if (!getResourceDirectory().exists()) {
            return false;
        }

        return true;
    }

    public int getMinSdk() {
        return minSdk;
    }

    public int getTargetSdk() {
        return targetSdk;
    }
	
	public File getResourceDirectory() {
		return new File(mRoot, "app/src/main/res");
	}
    
    public File getJavaDirectory() {
        return new File(mRoot, "app/src/main/java");
    }
    
    public File getLibraryDirectory() {
        return new File(mRoot, "app/libs");
    }
    
    public File getBuildDirectory() {
        return new File(mRoot, "app/build");
    }

    public File getManifestFile() {
        return new File(mRoot, "app/src/main/AndroidManifest.xml");
    }
}
