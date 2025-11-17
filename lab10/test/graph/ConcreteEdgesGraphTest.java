package graph;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * This runs GraphInstanceTest via inheritance and adds ConcreteEdgesGraph-specific tests.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {

    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }

    // Testing strategy for ConcreteEdgesGraph.toString():
    //  - empty graph
    //  - graph with vertices but no edges
    //  - graph with edges
    @Test
    public void testToStringEmpty() {
        ConcreteEdgesGraph g = new ConcreteEdgesGraph();
        String s = g.toString();
        assertTrue(s.contains("Vertices"));
        assertTrue(s.contains("Edges"));
    }

    @Test
    public void testToStringWithEdges() {
        ConcreteEdgesGraph g = new ConcreteEdgesGraph();
        g.set("A", "B", 3);
        g.set("B", "C", 4);
        String s = g.toString();
        assertTrue(s.contains("A"));
        assertTrue(s.contains("B"));
        assertTrue(s.contains("C"));
        assertTrue(s.contains("A -> B (3)"));
        assertTrue(s.contains("B -> C (4)"));
    }

    // Tests for Edge
    // Testing strategy:
    //  - constructor rejects invalid args
    //  - getters return stored values
    //  - toString format
    @Test(expected = IllegalArgumentException.class)
    public void testEdgeConstructorNullSource() {
        new Edge(null, "B", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEdgeConstructorNullTarget() {
        new Edge("A", null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEdgeConstructorNonPositiveWeight() {
        new Edge("A", "B", 0);
    }

    @Test
    public void testEdgeGettersAndToString() {
        Edge e = new Edge("X", "Y", 5);
        assertEquals("X", e.getSource());
        assertEquals("Y", e.getTarget());
        assertEquals(5, e.getWeight());
        assertEquals("X -> Y (5)", e.toString());
    }
}
