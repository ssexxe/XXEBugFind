/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.utils.misc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mikosh
 */
public class FileUtilTest {
    private FileUtil fileUtil;
    private static String[] fnames;
    private static String fullPathName;
    private static String paths = "";
    private static List allPaths;
    private static File testfile;
    
    public FileUtilTest() {
        fileUtil = FileUtil.getFileUtil();
    }
    
    @BeforeClass
    public static void setUpClass() throws IOException {
        testfile = new File("testfile.xxe");
        if (!testfile.exists()) {
            testfile.createNewFile();
        }
        File f1, f2;
        fnames = new String[10];
        allPaths = new ArrayList();
        for (int i=0; i<10; ++i) {
            
            f1 = new File("xxeTestXXE" + i + ".rmx");
            if (!f1.exists()) {
                f1.createNewFile();
            }           
            
            fnames[i] = f1.getAbsolutePath();
            paths += (f1.getAbsolutePath() + ";");
            allPaths.add(f1.getAbsolutePath());
            
            if (i==0) {
                fullPathName = f1.getAbsolutePath();
            }
            
            f2 = new File("xxeTestXXE2" + i + ".mxr");
            if (!f2.exists()) {
                f2.createNewFile();
            }            
        }
        paths = paths.substring(0, paths.length() -1);
    }
    
    @AfterClass
    public static void tearDownClass() {
        File f = new File("testfile.xxe");
        if (f.exists()) {
            f.delete();
        }
        
        File f1, f2;
        for (int i=0; i<10; ++i) {
            f1 = new File("xxeTestXXE" + i + ".rmx");
            if (f1.exists()) {
                f1.delete();
            }
            
            f2 = new File("xxeTestXXE2" + i + ".mxr");
            if (f2.exists()) {
                f2.delete();
            }
        }
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getFileUtil method, of class FileUtil.
     */
    @Test
    public void testGetFileUtil() {
        System.out.println("getFileUtil");
        FileUtil result = FileUtil.getFileUtil();
        assertNotNull(result);
    }

    /**
     * Test of getAllFilesWithExtension method, of class FileUtil.
     */
    @Test
    public void testGetAllFilesWithExtension() throws Exception {
        System.out.println("getAllFilesWithExtension");
        String dir = "";
        String extension = ".rmx";
        FileUtil instance = fileUtil;
        String[] expResult = fnames;
        String[] result = instance.getAllFilesWithExtension(new File(testfile.getAbsolutePath()).getParent(), extension);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getFullPathName method, of class FileUtil.
     */
    @Test
    public void testGetFullPathName() {
        System.out.println("getFullPathName");
        String fileName = "xxeTestXXE" + 0 + ".rmx";
        FileUtil instance = fileUtil;
        String expResult = fullPathName;
        String result = instance.getFullPathName(fileName);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractPaths method, of class FileUtil.
     */
    @Test
    public void testExtractPaths() {
        System.out.println("extractPaths");
        String pathStrings = paths;
        String pathStringsSeparator = ";";
        List<String> expResult = allPaths;
        List<String> result = FileUtil.extractPaths(pathStrings, pathStringsSeparator);
        assertEquals(expResult, result);
    }
    
}
