import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecursiveFileSearch {

	public static void main(String[] args) {

		// Ensure sufficient arguments are provided
		if (args.length < 3) {
			System.out.println(
					"Usage: java RecursiveFileSearch <directory_path> <case_sensitive:true|false> <file1> <file2> ...");
			return;
		}

		String directoryPath = args[0];
		boolean caseSensitive = Boolean.parseBoolean(args[1]);

		// Extract the file names to search for
		List<String> fileNames = new ArrayList<>();
		for (int i = 2; i < args.length; i++) {
			fileNames.add(args[i]);
		}

		// Validate directory
		File directory = new File(directoryPath);
		if (!directory.exists() || !directory.isDirectory()) {
			System.out.println("Error: The specified directory does not exist or is not a directory.");
			return;
		}

		// Search each file
		for (String fileName : fileNames) {
			System.out.println("\nSearching for file: " + fileName);
			List<String> foundPaths = new ArrayList<>();
			searchFileRecursive(directory, fileName, foundPaths, caseSensitive);

			if (foundPaths.isEmpty()) {
				System.out.println("File not found: " + fileName);
			} else {
				System.out.println("Found " + foundPaths.size() + " instance(s) of '" + fileName + "':");
				for (String path : foundPaths) {
					System.out.println("   - " + path);
				}
			}
		}
	}

	/**
	 * Recursively searches for a file within a directory and its subdirectories.
	 *
	 * @param directory     The directory to search
	 * @param targetName    The name of the file to find
	 * @param foundPaths    A list to store paths of found files
	 * @param caseSensitive Whether the search is case-sensitive
	 */
	public static void searchFileRecursive(File directory, String targetName, List<String> foundPaths,
			boolean caseSensitive) {
		File[] files = directory.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				// Recursively search in subdirectory
				searchFileRecursive(file, targetName, foundPaths, caseSensitive);
			} else {
				String fileNameToCompare = caseSensitive ? file.getName() : file.getName().toLowerCase();
				String targetNameToCompare = caseSensitive ? targetName : targetName.toLowerCase();

				if (fileNameToCompare.equals(targetNameToCompare)) {
					foundPaths.add(file.getAbsolutePath());
				}
			}
		}
	}
}
