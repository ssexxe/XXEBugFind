/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.utils.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of frequently used file operations. This class cannot be instantiated directly. Use the
 * static FileUtil.getFileUtil() to get a handle to it
 * @author Mikosh
 */
public class FileUtil {
    private static FileUtil fileUtil; 
    private static FileExtensionFilter fileExtensionFilter = new FileExtensionFilter("");
    private static FilenameFilter fileDirectoryFilter = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String name) {
            return (new File(dir, name).isDirectory());
        }
    };

    private FileUtil() {
    }
    
    /**
     * Gets a handle to the FileUtil
     * @return a FileUtil object
     */
    public static FileUtil getFileUtil() {
        if (fileUtil == null) {
            fileUtil = new FileUtil();
        }
        
        return fileUtil;
    }
    
    /**
     * Gets the names of all files in the specified directory ending with the specified extension
     * @param dir the directory containing the files
     * @param extension the extension to be used as filter
     * @return a String array corresponding to the names of the files matching the filter. This array will be empty 
     * if there is no match or the specified directory is empty. This method will return null if this abstract 
     * pathname does not denote a directory, or if an I/O error occurs.
     * @throws FileNotFoundException
     * @throws FileSystemException 
     */
    public String[] getAllFilesWithExtension(String dir, String extension) throws FileNotFoundException, FileSystemException {
        File f = new File(dir);
        if (!f.isDirectory()) {
            throw new FileNotFoundException("Invalid directory '" + dir + "' specified");
        }
        if (extension == null || extension.trim().equals("")) {
            throw new FileSystemException("Empty or invalid file extension specified. An example of a valid extension is .jar");
        }
        
        fileExtensionFilter.setExtension(extension);
        File[] files= f.listFiles(fileExtensionFilter);
        
        if (files == null) {
            return null;
        }
        
        String[] retVal = new String[files.length];
        
        for (int i=0; i< files.length; ++i) {
            retVal[i] = files[i].getAbsolutePath();
        }
        return retVal;//f.list(fileExtensionFilter);
    }
   
    /**
     * Gets the full path name for the specified file. This method does a verification on whether the resolved full path exists and 
     * throws a RuntimeException if it doesn't exist. This check is necessary for the application so as alert a user immediately if
     * when any of the files entered via the command line is incorrect. This will prevent unexpected behaviour from missing files/libs.
     * @param fileName the filename corresponding to the file whose full path is to be obtained
     * @return the full path name corresponding to the file given.
     * @throws RuntimeException if the path is incorrect or does not exist
     */
    public String getFullPathName(String fileName) {
        File f = new File(fileName);
        // do a check to ensure path exists
        if (!f.exists()) {
            throw new RuntimeException("Cannot resolve the full pathname for '" + fileName +"'. It may not exist. "
                    + "Check the path correctly written");
        }
        return f.getAbsolutePath();
    } 
    
    /**
     * Tries to get the rt.jar directory. This is currently needed by soot for analysis.
     * Returns the rt.jar directory if found or null if not found 
     * @return the rt.jar directory on the local file system or null if it cannot find it
     */
    public String getRTDirectory() {
        String javahome = System.getProperty("java.home");
        // return null if there is an invalid java home
        if (javahome == null || javahome.trim().isEmpty()) {
            return null;
        }
        
        File fdir = new File(javahome);
        // return null if the path doesnt exist or is not a directory
        if (!fdir.exists() || !fdir.isDirectory()) {
            return null;
        }
        
        String[] dirs = fdir.list(fileDirectoryFilter);
        if (dirs == null || dirs.length == 0) {
            return null;
        }
        
        for (String dir : dirs) {
            File fd = new File(fdir, dir);
            File f = new File(fd, "rt.jar");
            if (f.exists()) {
                return fd.getAbsolutePath();
            }
        }
        
        // reaching here means rt.jar directory was not found
        return null;
    }
    
    /**
     * Extracts paths from a string containing the concatenation of one or more paths using the specified path separator
     * This method is useful for the command-line parsing of -d, or -l options where the user can specify
     * one or more paths in s string separated by ";"
     * See example below.
     * <code><pre>
     * String paths = "C:\Users\File1;C:\Users\File2.txt;C:\Users\File3";
     * FileUtils.extractPaths(paths, ",");
     * </pre></code>
     * Result given should be a list of length 3 containing the following
     * <code><pre>
     * C:\Users\File1
     * C:\Users\File2.txt
     * C:\Users\File3
     * </pre></code>
     * @param pathStrings the string to be separated into paths
     * @param pathStringsSeparator the path separator to be used
     * @return a list containing all the paths
     */
    public static List<String> extractPaths(String pathStrings, String pathStringsSeparator) {
        List<String> listPaths = new ArrayList<>();      
        
        String[] paths = pathStrings.split(pathStringsSeparator);
        for (String path : paths) {
            if (!path.trim().isEmpty()) {
                listPaths.add(fileUtil.getFullPathName(path.trim()));
            }
        }
        
        return listPaths;
    }
    
}
