/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;


import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Set;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
	// Testing strategy:
    //
    // vertices():
    //   - empty graph
    //   - graph with one vertex
    //   - graph with multiple vertices
    //
    // add(vertex):
    //   - add new vertex
    //   - add duplicate vertex
    //
    // remove(vertex):
    //   - remove existing vertex
    //   - remove non-existing vertex
    //
    // set(source, target, weight):
    //   - add new edge
    //   - add edge when vertices don't exist (auto-add)
    //   - update existing edge weight
    //   - remove edge with weight 0
    //   - self-edge (source == target)
    //
    // sources(target):
    //   - no incoming edges
    //   - one incoming edge
    //   - multiple incoming edges
    //
    // targets(source):
    //   - no outgoing edges
    //   - one outgoing edge
    //   - multiple outgoing edges
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    // --- vertices() ---
    @Test
    public void testInitialVerticesEmpty() {
        assertEquals(Collections.emptySet(), emptyInstance().vertices());
    }
    
    @Test
    public void testVerticesAfterAdd() {
        Graph<String> g = emptyInstance();
        g.add("A");
        assertEquals(Set.of("A"), g.vertices());
    }

    @Test
    public void testVerticesMultiple() {
        Graph<String> g = emptyInstance();
        g.add("A");
        g.add("B");
        g.add("C");
        assertEquals(Set.of("A", "B", "C"), g.vertices());
    }
    
    
    // --- add() ---
    @Test
    public void testAddReturnsTrueOnNewVertex() {
        Graph<String> g = emptyInstance();
        assertTrue(g.add("A"));
    }

    @Test
    public void testAddDuplicateReturnsFalse() {
        Graph<String> g = emptyInstance();
        g.add("A");
        assertFalse(g.add("A"));
    }
    
    
    // --- remove() ---
    @Test
    public void testRemoveExistingVertex() {
        Graph<String> g = emptyInstance();
        g.add("A");
        assertTrue(g.remove("A"));
        assertEquals(Collections.emptySet(), g.vertices());
    }

    @Test
    public void testRemoveNonExistingVertex() {
        Graph<String> g = emptyInstance();
        assertFalse(g.remove("X"));
    }


    // --- set() ---
    @Test
    public void testSetAddNewEdge() {
        Graph<String> g = emptyInstance();
        assertEquals(0, g.set("A", "B", 3));
        assertEquals(Map.of("B", 3), g.targets("A"));
    }

    @Test
    public void testSetAutoAddVertices() {
        Graph<String> g = emptyInstance();
        g.set("X", "Y", 10);
        assertEquals(Set.of("X", "Y"), g.vertices());
    }

    @Test
    public void testSetUpdateEdge() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 5);
        assertEquals(5, g.set("A", "B", 7));
        assertEquals(Integer.valueOf(7), g.targets("A").get("B"));
    }

    @Test
    public void testSetRemoveEdge() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 4);
        assertEquals(4, g.set("A", "B", 0));
        assertEquals(Collections.emptyMap(), g.targets("A"));
    }

    @Test
    public void testSetSelfEdge() {
        Graph<String> g = emptyInstance();
        g.set("A", "A", 9);
        assertEquals(Map.of("A", 9), g.targets("A"));
    }

    
    // --- sources() ---
    @Test
    public void testSourcesNone() {
        Graph<String> g = emptyInstance();
        g.add("A");
        assertEquals(Collections.emptyMap(), g.sources("A"));
    }

    @Test
    public void testSourcesOne() {
        Graph<String> g = emptyInstance();
        g.set("X", "A", 4);
        assertEquals(Map.of("X", 4), g.sources("A"));
    }

    @Test
    public void testSourcesMultiple() {
        Graph<String> g = emptyInstance();
        g.set("X", "A", 2);
        g.set("Y", "A", 3);
        assertEquals(Map.of("X", 2, "Y", 3), g.sources("A"));
    }

    // --- targets() ---

    @Test
    public void testTargetsNone() {
        Graph<String> g = emptyInstance();
        g.add("A");
        assertEquals(Collections.emptyMap(), g.targets("A"));
    }

    @Test
    public void testTargetsOne() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 5);
        assertEquals(Map.of("B", 5), g.targets("A"));
    }

    @Test
    public void testTargetsMultiple() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 1);
        g.set("A", "C", 2);
        assertEquals(Map.of("B", 1, "C", 2), g.targets("A"));
    }
}
