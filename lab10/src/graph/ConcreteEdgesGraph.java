package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;

/**
 * ConcreteEdgesGraph implements Graph<String> using the provided rep:
 *   private final Set<String> vertices = new HashSet<>();
 *   private final List<Edge> edges = new ArrayList<>();
 *
 * Abstraction function (AF):
 *   AF(vertices, edges) = a directed weighted graph where
 *     - vertices contains all vertex labels in the graph, and
 *     - edges contains Edge objects representing directed edges (edge.source -> edge.target
 *       with weight edge.weight).
 *
 * Representation invariant (RI):
 *   - vertices != null
 *   - edges != null
 *   - no null elements in vertices
 *   - no null elements in edges
 *   - for every Edge e in edges:
 *       - e.getWeight() > 0
 *       - e.getSource() and e.getTarget() are non-null strings
 *       - vertices contains e.getSource() and e.getTarget()
 *   - there are no duplicate edges with same (source,target) pair (at most one Edge per ordered pair)
 *
 * Safety from rep exposure:
 *   - all internal collections are private and never returned directly.
 *   - vertices() returns an unmodifiable copy (Set.copyOf).
 *   - sources(...) / targets(...) build and return Map.copyOf maps.
 *
 */
public class ConcreteEdgesGraph implements Graph<String> {

    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();

    /**
     * Default constructor: creates an empty graph.
     */
    public ConcreteEdgesGraph() {
        checkRep();
    }

    /**
     * checkRep: verify representation invariant.
     */
    private void checkRep() {
        assert vertices != null : "vertices must not be null";
        assert edges != null : "edges must not be null";
        for (String v : vertices) {
            assert v != null : "vertex label must not be null";
        }
        Set<String> seenPairs = new HashSet<>();
        for (Edge e : edges) {
            assert e != null : "edge must not be null";
            assert e.getWeight() > 0 : "edge weight must be > 0";
            assert e.getSource() != null && e.getTarget() != null : "edge endpoints non-null";
            assert vertices.contains(e.getSource()) : "edge source must be in vertices";
            assert vertices.contains(e.getTarget()) : "edge target must be in vertices";
            String pair = e.getSource() + "->" + e.getTarget();
            assert !seenPairs.contains(pair) : "duplicate edges for same (source,target)";
            seenPairs.add(pair);
        }
    }

    @Override
    public boolean add(String vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("vertex must not be null");
        }
        boolean changed = vertices.add(vertex);
        checkRep();
        return changed;
    }

    @Override
    public int set(String source, String target, int weight) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("source and target must not be null");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("weight must be nonnegative");
        }

        // Ensure vertices exist if weight > 0
        if (weight > 0) {
            vertices.add(source);
            vertices.add(target);
        }

        // find existing edge if any
        Edge found = null;
        for (Edge e : edges) {
            if (e.getSource().equals(source) && e.getTarget().equals(target)) {
                found = e;
                break;
            }
        }
        if (weight == 0) {
            // remove edge if exists
            if (found != null) {
                int old = found.getWeight();
                edges.remove(found);
                checkRep();
                return old;
            } else {
                checkRep();
                return 0;
            }
        } else {
            // add or update
            if (found != null) {
                int old = found.getWeight();
                // replace the edge object with new weight
                edges.remove(found);
                edges.add(new Edge(source, target, weight));
                checkRep();
                return old;
            } else {
                edges.add(new Edge(source, target, weight));
                checkRep();
                return 0;
            }
        }
    }

    @Override
    public boolean remove(String vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("vertex must not be null");
        }
        if (!vertices.contains(vertex)) {
            return false;
        }
        vertices.remove(vertex);

        // remove all edges incident to vertex (either source or target)
        List<Edge> toRemove = new ArrayList<>();
        for (Edge e : edges) {
            if (e.getSource().equals(vertex) || e.getTarget().equals(vertex)) {
                toRemove.add(e);
            }
        }
        edges.removeAll(toRemove);

        checkRep();
        return true;
    }

    @Override
    public Set<String> vertices() {
        // return an unmodifiable copy
        return Set.copyOf(vertices);
    }

    @Override
    public Map<String, Integer> sources(String target) {
        if (target == null) {
            throw new IllegalArgumentException("target must not be null");
        }
        Map<String, Integer> result = new HashMap<>();
        for (Edge e : edges) {
            if (e.getTarget().equals(target)) {
                result.put(e.getSource(), e.getWeight());
            }
        }
        return Map.copyOf(result);
    }

    @Override
    public Map<String, Integer> targets(String source) {
        if (source == null) {
            throw new IllegalArgumentException("source must not be null");
        }
        Map<String, Integer> result = new HashMap<>();
        for (Edge e : edges) {
            if (e.getSource().equals(source)) {
                result.put(e.getTarget(), e.getWeight());
            }
        }
        return Map.copyOf(result);
    }

    /**
     * A human readable string representation.
     * Format:
     *  Vertices: [A, B, C]
     *  Edges:
     *   A -> B (3)
     *   C -> A (1)
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertices: ").append(vertices).append(System.lineSeparator());
        sb.append("Edges:").append(System.lineSeparator());
        for (Edge e : edges) {
            sb.append("  ").append(e.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}

/**
 * Immutable Edge class used by ConcreteEdgesGraph.
 *
 * Spec:
 *   An Edge represents a directed edge (source -> target) with a strictly positive weight.
 *
 * AF:
 *   AF(source,target,weight) = edge in graph from source to target with weight
 *
 * RI:
 *   - source != null
 *   - target != null
 *   - weight > 0
 *
 * Safety from rep exposure:
 *   - all fields are private final and immutable
 *   - no methods return internal mutable objects
 */
class Edge {

    private final String source;
    private final String target;
    private final int weight;

    /**
     * Construct an Edge.
     *
     * @throws IllegalArgumentException if source or target is null, or weight <= 0
     */
    public Edge(String source, String target, int weight) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("edge endpoints must not be null");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("edge weight must be > 0");
        }
        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }

    private void checkRep() {
        assert source != null;
        assert target != null;
        assert weight > 0;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source + " -> " + target + " (" + weight + ")";
    }
}
