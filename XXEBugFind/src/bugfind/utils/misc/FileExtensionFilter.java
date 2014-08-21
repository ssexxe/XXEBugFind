/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.utils.misc;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Mikosh
 */
public class FileExtensionFilter implements FilenameFilter {
    private String extension;

    public FileExtensionFilter(String extension) {
        setExtension(extension);
    }

    public String getExtension() {
        return extension;
    }

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
