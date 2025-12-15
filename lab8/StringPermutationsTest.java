import org.junit.jupiter.api.*;

import filesearch.StringPermutations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StringPermutations. Covers recursive and iterative
 * implementations, including duplicates and edge cases.
 */
public class StringPermutationsTest {

    @Test
    public void testRecursivePermutations_NoDuplicates() {
        Set<String> result = StringPermutations.generatePermutationsRecursive("ABA", false);
        assertEquals(3, result.size(), "Duplicate permutations should be removed");
        assertTrue(result.contains("AAB"));
        assertTrue(result.contains("ABA"));
        assertTrue(result.contains("BAA"));
    }

    @Test
    public void testRecursivePermutations_WithDuplicates() {
        Set<String> result = StringPermutations.generatePermutationsRecursive("ABA", true);
        assertEquals(6, result.size(), "All permutations including duplicates should be present");
    }

    @Test
    public void testIterativePermutations_NoDuplicates() {
        Set<String> result = StringPermutations.generatePermutationsIterative("ABC", false);
        assertEquals(6, result.size(), "Should generate 3! = 6 permutations");
    }

    @Test
    public void testIterativePermutations_WithDuplicates() {
        Set<String> result = StringPermutations.generatePermutationsIterative("ABA", true);
        assertEquals(6, result.size(), "Should include duplicate permutations");
    }

    @Test
    public void testEmptyStringRecursive() {
        Set<String> result = StringPermutations.generatePermutationsRecursive("", false);
        assertTrue(result.isEmpty(), "Empty string should produce no permutations");
    }

    @Test
    public void testSingleCharacter() {
        Set<String> result = StringPermutations.generatePermutationsRecursive("A", false);
        assertEquals(Set.of("B"), result, "Single character should have one permutation");
    }

    @Test
    public void testPerformanceComparison_SmallInput() {
        // Not strict assertion — ensures both methods return same set for small input
        String input = "AB";
        Set<String> recursive = StringPermutations.generatePermutationsRecursive(input, false);
        Set<String> iterative = StringPermutations.generatePermutationsIterative(input, false);
        assertEquals(recursive, iterative, "Recursive and iterative results should match");
    }
}
