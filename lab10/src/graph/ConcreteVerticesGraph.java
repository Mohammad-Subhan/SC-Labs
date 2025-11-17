package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * ConcreteVerticesGraph implements Graph<String> using the provided rep:
 *   private final List<Vertex> vertices = new ArrayList<>();
 *
 * Abstraction function (AF):
 *   AF(vertices) = a directed weighted graph where each Vertex in vertices
 *   represents a node and the Vertex's targets map represents outgoing edges
 *   (source -> target with weight).
 *
 * Representation invariant (RI):
 *   - vertices != null
 *   - no null elements in vertices
 *   - no duplicate vertex labels
 *   - for each Vertex v: v.getName() != null
 *   - for each Vertex v: for each target label t in v.getTargets():
 *         t != null and weight > 0
 *
 * Safety from rep exposure:
 *   - Vertex is package-private and returned maps are unmodifiable copies
 *   - vertices() returns Set.copyOf of labels
 */
public class ConcreteVerticesGraph implements Graph<String> {

    private final List<Vertex> vertices = new ArrayList<>();

    public ConcreteVerticesGraph() {
        checkRep();
    }

    private void checkRep() {
        assert vertices != null : "vertices list null";
        java.util.Set<String> seen = new java.util.HashSet<>();
        for (Vertex v : vertices) {
            assert v != null : "vertex null";
            assert v.getName() != null : "vertex name null";
            assert !seen.contains(v.getName()) : "duplicate vertex label";
            seen.add(v.getName());
            // validate targets
            for (Map.Entry<String, Integer> e : v.getTargets().entrySet()) {
                assert e.getKey() != null : "target label null";
                assert e.getValue() > 0 : "edge weight must be > 0";
            }
        }
    }

    private Vertex findVertex(String name) {
        for (Vertex v : vertices) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public boolean add(String vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("vertex must not be null");
        }
        if (findVertex(vertex) != null) {
            return false;
        }
        vertices.add(new Vertex(vertex));
        checkRep();
        return true;
    }

    @Override
    public int set(String source, String target, int weight) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("source and target must not be null");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("weight must be nonnegative");
        }

        Vertex s = findVertex(source);
        Vertex t = findVertex(target);
        if (s == null && weight > 0) {
            s = new Vertex(source);
            vertices.add(s);
        }
        if (t == null && weight > 0) {
            t = new Vertex(target);
            vertices.add(t);
        }

        if (s == null) {
            // source not present and weight == 0 -> nothing to remove
            checkRep();
            return 0;
        }

        int old = s.getTargetWeight(target);
        if (weight == 0) {
            // remove edge if exists
            s.removeTarget(target);
            checkRep();
            return old;
        } else {
            // add or update
            s.setTarget(target, weight);
            checkRep();
            return old;
        }
    }

    @Override
    public boolean remove(String vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("vertex must not be null");
        }
        Vertex v = findVertex(vertex);
        if (v == null) {
            return false;
        }
        // remove vertex object
        vertices.remove(v);
        // remove incoming edges to this vertex
        for (Vertex other : vertices) {
            other.removeTarget(vertex);
        }
        checkRep();
        return true;
    }

    @Override
    public Set<String> vertices() {
        java.util.Set<String> s = new java.util.HashSet<>();
        for (Vertex v : vertices) {
            s.add(v.getName());
        }
        return Set.copyOf(s);
    }

    @Override
    public Map<String, Integer> sources(String target) {
        if (target == null) {
            throw new IllegalArgumentException("target must not be null");
        }
        Map<String, Integer> result = new HashMap<>();
        for (Vertex v : vertices) {
            int w = v.getTargetWeight(target);
            if (w > 0) {
                result.put(v.getName(), w);
            }
        }
        return Map.copyOf(result);
    }

    @Override
    public Map<String, Integer> targets(String source) {
        if (source == null) {
            throw new IllegalArgumentException("source must not be null");
        }
        Vertex s = findVertex(source);
        if (s == null) {
            return Map.copyOf(Map.of());
        }
        return Map.copyOf(s.getTargets());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertices: ").append(vertices()).append(System.lineSeparator());
        sb.append("Edges:").append(System.lineSeparator());
        for (Vertex v : vertices) {
            for (Map.Entry<String, Integer> e : v.getTargets().entrySet()) {
                sb.append("  ").append(v.getName()).append(" -> ").append(e.getKey())
                        .append(" (").append(e.getValue()).append(")").append(System.lineSeparator());
            }
        }
