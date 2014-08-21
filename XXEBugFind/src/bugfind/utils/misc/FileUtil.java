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
 *
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
    
    public static FileUtil getFileUtil() {
        if (fileUtil == null) {
            fileUtil = new FileUtil();
        }
        
        return fileUtil;
    }
    
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
        String[] retVal = new String[files.length];
        
        for (int i=0; i< files.length; ++i) {
            retVal[i] = files[i].getAbsolutePath();
        }
        return retVal;//f.list(fileExtensionFilter);
    }
   
    
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
     * @return 
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
