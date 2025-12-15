import org.junit.jupiter.api.*;

import filesearch.RecursiveFileSearch;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RecursiveFileSearch.
 * Tests base, recursive, and edge cases for file search functionality.
 */
public class RecursiveFileSearchTest {

    private static File testRootDir;

    @BeforeAll
    public static void setup() throws IOException {
        // Create temporary directory structure for testing
        testRootDir = new File("test_data");
        testRootDir.mkdir();

        // Create subdirectories and files
        File subDir = new File(testRootDir, "sub");
        subDir.mkdir();

        new File(testRootDir, "report.txt").createNewFile();
        new File(subDir, "report.txt").createNewFile();
        new File(subDir, "notes.txt").createNewFile();
    }

    @AfterAll
    public static void cleanup() {
        deleteDirectory(testRootDir);
    }

    private static void deleteDirectory(File dir) {
        if (dir == null || !dir.exists())
            return;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory())
                    deleteDirectory(f);
                else
                    f.delete();
            }
        }
        dir.delete();
    }

    @Test
    public void testFileFoundInRootDirectory() {
        List<String> foundPaths = new ArrayList<>();
        RecursiveFileSearch.searchFileRecursive(testRootDir, "report.txt", foundPaths, true);
        assertFalse(foundPaths.isEmpty(), "File should be found in root directory");
    }

    @Test
    public void testFileFoundInSubdirectory() {
        List<String> foundPaths = new ArrayList<>();
        RecursiveFileSearch.searchFileRecursive(testRootDir, "notes.txt", foundPaths, true);
        assertEquals(1, foundPaths.size(), "File should be found in subdirectory");
    }

    @Test
    public void testFileNotFound() {
        List<String> foundPaths = new ArrayList<>();
        RecursiveFileSearch.searchFileRecursive(testRootDir, "missing.txt", foundPaths, true);
        assertTrue(foundPaths.isEmpty(), "Missing file should not be found");
    }

    @Test
    public void testCaseInsensitiveSearch() {
        List<String> foundPaths = new ArrayList<>();
        RecursiveFileSearch.searchFileRecursive(testRootDir, "REPORT.TXT", foundPaths, false);
        assertEquals(2, foundPaths.size(), "Case-insensitive search should find all instances");
    }

    @Test
    public void testInvalidDirectory() {
        File invalidDir = new File("nonexistent_dir");
        List<String> foundPaths = new ArrayList<>();
        assertDoesNotThrow(() -> RecursiveFileSearch.searchFileRecursive(invalidDir, "file.txt", foundPaths, true),
                "Function should handle invalid directories gracefully");
    }
}
