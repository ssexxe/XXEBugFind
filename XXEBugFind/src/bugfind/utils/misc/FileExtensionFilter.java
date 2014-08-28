/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.utils.misc;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A custom file extension filter for use by the application. It filters the application by the file extension
 * given
 * @author Mikosh
 */
public class FileExtensionFilter implements FilenameFilter {
    private String extension;

    /**
     * Creates a FileExtension filter from the given extension
     * @param extension the given extension to server as the filter (e.g. of valid file extension .tiff)
     */
    public FileExtensionFilter(String extension) {
        setExtension(extension);
    }

    /**
     * Gets the extension
     * @return 
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Sets the file extension used by this file filter
     * @param extension the extension to be set (e.g. .gif)
     */
    public void setExtension(String extension) {
        if (extension == null || extension.trim().equals("")) {
            this.extension = "";
        }
        else {
            this.extension = (extension.startsWith(".")) ? extension : "." + extension;
        }
    }   

    @Override
    public boolean accept(File dir, String name) {
        return (name.endsWith(extension));
    }
    
}
