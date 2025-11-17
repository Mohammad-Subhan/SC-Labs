package graph;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * This runs GraphInstanceTest via inheritance and adds ConcreteVerticesGraph-specific tests.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {

    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph();
    }

    // Testing strategy for ConcreteVerticesGraph.toString():
    //  - empty graph string contains "Vertices" and "Edges"
    //  - after adding edges, string contains appropriate edge representations
    @Test
    public void testToStringEmpty() {
        ConcreteVerticesGraph g = new ConcreteVerticesGraph();
        String s = g.toString();
        assertTrue(s.contains("Vertices"));
        assertTrue(s.contains("Edges"));
    }

    @Test
    public void testToStringWithEdges() {
        ConcreteVerticesGraph g = new ConcreteVerticesGraph();
        g.set("P", "Q", 2);
        g.set("P", "R", 3);
        String s = g.toString();
        assertTrue(s.contains("P -> Q (2)"));
        assertTrue(s.contains("P -> R (3)"));
    }

    // Tests for Vertex class
    // Testing strategy:
    //  - constructor rejects null name
    //  - adding, updating, removing targets behave correctly
    @Test(expected = IllegalArgumentException.class)
    public void testVertexConstructorNullName() {
        new Vertex(null);
    }

    @Test
    public void testVertexTargetsOperations() {
        Vertex v = new Vertex("V");
        v.setTarget("A", 4);
        v.setTarget("B", 5);
        assertEquals(Map.of("A", 4, "B", 5), v.getTargets());
        assertEquals(4, v.getTargetWeight("A"));
        // update weight
        v.setTarget("A", 6);
        assertEquals(6, v.getTargetWeight("A"));
        // remove
        v.removeTarget("A");
        assertEquals(0, v.getTargetWeight("A"));
        assertEquals(Map.of("B", 5), v.getTargets());
    }
}
