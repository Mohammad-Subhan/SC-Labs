import java.util.*;

public class StringPermutations {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		try {
			// Input string
			System.out.print("Enter a string: ");
			String input = scanner.nextLine();

			if (input == null || input.trim().isEmpty()) {
				System.out.println("Error: Input string cannot be empty.");
				return;
			}

			// Option for duplicates
			System.out.print("Include duplicate permutations? (true/false): ");
			boolean includeDuplicates = scanner.nextBoolean();

			// --- Recursive approach ---
			long startRecursive = System.nanoTime();
			Set<String> recursivePermutations = generatePermutationsRecursive(input, includeDuplicates);
			long endRecursive = System.nanoTime();

			System.out.println("\nRecursive Permutations (" + recursivePermutations.size() + "):");
			for (String perm : recursivePermutations) {
				System.out.println(perm);
			}

			System.out.println("Time (Recursive): " + (endRecursive - startRecursive) / 1_000_000.0 + " ms");

			// --- Non-recursive approach ---
			long startIterative = System.nanoTime();
			Set<String> iterativePermutations = generatePermutationsIterative(input, includeDuplicates);
			long endIterative = System.nanoTime();

			System.out.println("\nNon-Recursive Permutations (" + iterativePermutations.size() + "):");
			for (String perm : iterativePermutations) {
				System.out.println(perm);
			}

			System.out.println("Time (Non-Recursive): " + (endIterative - startIterative) / 1_000_000.0 + " ms");

			// Time comparison
			System.out.println("\nPerformance Comparison:");
			System.out.printf("Recursive: %.3f ms | Iterative: %.3f ms\n",
					(endRecursive - startRecursive) / 1_000_000.0, (endIterative - startIterative) / 1_000_000.0);

		} catch (InputMismatchException e) {
			System.out.println("Invalid input. Please enter 'true' or 'false' for the duplicate option.");
		} catch (Exception e) {
			System.out.println("An unexpected error occurred: " + e.getMessage());
		} finally {
			scanner.close();
		}
	}

	/**
	 * Generates all permutations of a string recursively.
	 *
	 * @param str               The input string
	 * @param includeDuplicates Whether to include duplicate permutations
	 * @return A set of all permutations
	 *
	 *         Time Complexity: O(n!) where n = length of the string. Space
	 *         Complexity: O(n) for recursion stack.
	 */
	public static Set<String> generatePermutationsRecursive(String str, boolean includeDuplicates) {
		Set<String> result = includeDuplicates ? new LinkedHashSet<>() : new TreeSet<>();
		permute(str.toCharArray(), 0, result, includeDuplicates);
		return result;
	}

	private static void permute(char[] chars, int index, Set<String> result, boolean includeDuplicates) {
		if (index == chars.length - 1) {
			result.add(new String(chars));
			return;
		}

		Set<Character> used = new HashSet<>(); // to avoid duplicate branches
		for (int i = index; i < chars.length; i++) {
			if (!includeDuplicates && used.contains(chars[i]))
				continue;
			used.add(chars[i]);

			swap(chars, index, i);
			permute(chars, index + 1, result, includeDuplicates);
			swap(chars, index, i); // backtrack
		}
	}

	private static void swap(char[] arr, int i, int j) {
		char temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	/**
	 * Generates all permutations of a string using an iterative algorithm. Based on
	 * Heap's algorithm for permutation generation.
	 *
	 * @param str               The input string
	 * @param includeDuplicates Whether to include duplicate permutations
	 * @return A set of all permutations
	 *
	 *         Time Complexity: O(n!) similar to recursive approach. Space
	 *         Complexity: O(n)
	 */
	public static Set<String> generatePermutationsIterative(String str, boolean includeDuplicates) {
		Set<String> result = includeDuplicates ? new LinkedHashSet<>() : new TreeSet<>();

		char[] chars = str.toCharArray();
		int n = chars.length;
		int[] c = new int[n];

		result.add(new String(chars));

		int i = 0;
		while (i < n) {
			if (c[i] < i) {
				if (i % 2 == 0)
					swap(chars, 0, i);
				else
					swap(chars, c[i], i);

				result.add(new String(chars));
				c[i]++;
				i = 0;
			} else {
				c[i] = 0;
				i++;
			}
		}

		// Remove duplicates if requested
		if (!includeDuplicates) {
			return new TreeSet<>(result);
		}
		return result;
	}
}
