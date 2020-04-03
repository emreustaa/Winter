package com.fsm.Utils;

import com.fsm.Exceptions.IllegalDirectoryNameException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PathScanner {

    private PathScanner() {
    }

    private static final PathScanner p = new PathScanner();

    public static PathScanner getInstance() {
        return p;
    }


    public List<Class<?>> getClassesOf(String packageName) {
        return getClassesFrom(packageName);
    }


    public List<Class<?>> getClassesOf(String packageName, Predicate<Class<?>> filter) {
        return getClassesFrom(packageName)
                .stream()
                .filter(filter)
                .collect(Collectors.toList());
    }


    private List<Class<?>> getClassesFrom(String packageName) {
        String path = getPathNameFrom(packageName);
        Enumeration<URL> resources = getResourcesIn(path);
        List<File> directories = getDirectoriesFrom(resources);
        return getClassesIn(directories, packageName);
    }


    private String getPathNameFrom(String packageName) {
        return packageName.replace(".", "/");
    }

    private Enumeration<URL> getResourcesIn(String path) {
        return getResources(path);
    }


    private Enumeration<URL> getResources(String pathName) {
        Enumeration<URL> emptyResources = Collections.emptyEnumeration();
        try {
            ClassLoader classLoader = getClassLoader();
            return classLoader.getResources(pathName);
        } catch (IOException | NoSuchElementException e) {
            e.printStackTrace();
        }
        return emptyResources;
    }


    private ClassLoader getClassLoader() {
        return Optional
                .ofNullable(Thread.currentThread().getContextClassLoader())
                .orElseThrow(() -> new NoSuchElementException("ClassLoader cannot be instantiated"));
    }


    private List<File> getDirectoriesFrom(Enumeration<URL> resources) {
        var directories = new ArrayList<File>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            directories.add(new File(resource.getFile()));
        }
        return directories;
    }


    private List<Class<?>> getClassesIn(List<File> directories, String packageName) {
        var classes = new ArrayList<Class<?>>();
        directories.forEach(directory ->
                classes.addAll(getClassesIn(directory, packageName)));
        return classes;
    }


    private List<Class<?>> getClassesIn(File directory, String packageName) {
        var classes = new ArrayList<Class<?>>();
        File[] files = getFiles(directory);
        List.of(files).forEach(file -> {
            if (file.isDirectory()) {
                String dirName = validateDirName(file.getName());
                classes.addAll(getClassesIn(file, getSubDirName(packageName, dirName)));
            } else if (file.getName().endsWith(".class")) {
                classes.add(getClass(packageName, file.getName()));
            }
        });
        return classes;
    }


    private File[] getFiles(File directory) {
        File[] emptyFiles = new File[0];
        try{
            return Optional
                    .ofNullable(directory.listFiles())
                    .orElseThrow(() ->
                            new NoSuchElementException(
                                    "abstract pathname does not denote a directory"));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return emptyFiles;
    }


    private String validateDirName(String fileName) {
        try{
            if(fileName.contains(".")) {
                throw new IllegalDirectoryNameException();
            }
        } catch (IllegalDirectoryNameException e) {
            e.printStackTrace();
        }
        return fileName;
    }


    private String getSubDirName(String packageName, String dirName) {
        return packageName + "." + dirName;
    }


    private Class<?> getClass(String packageName, String fileName) {
        try {
            return Class.forName(getClassNameFrom(packageName, fileName));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getClassNameFrom(String packageName, String fileName) {
        return packageName + "." + removeDotClassPostFix(fileName);
    }


    String removeDotClassPostFix(String fileName) {
        return fileName.substring(0, fileName.length() - 6);
    }

}
