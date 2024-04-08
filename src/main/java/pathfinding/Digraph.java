package pathfinding;

import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.TreeMap;

import parser.TagNode;

import java.util.ArrayList;

import pathfinding.Edge;

// Note: This Class is from https://algs4.cs.princeton.edu/44sp/EdgeWeightedGraph.java.html

/**
 *  The {@code EdgeWeightedGraph} class represents an edge-weighted
 *  graph of vertices named 0 through <em>V</em> – 1, where each
 *  undirected edge is of type {@link Edge} and has a real-valued weight.
 *  It supports the following two primary operations: add an edge to the graph,
 *  iterate over all of the edges incident to a vertex. It also provides
 *  methods for returning the degree of a vertex, the number of vertices
 *  <em>V</em> in the graph, and the number of edges <em>E</em> in the graph.
 *  Parallel edges and self-loops are permitted.
 *  By convention, a self-loop <em>v</em>-<em>v</em> appears in the
 *  adjacency list of <em>v</em> twice and contributes two to the degree
 *  of <em>v</em>.
 *  <p>
 *  This implementation uses an <em>adjacency-lists representation</em>, which
 *  is a vertex-indexed array of {@link Bag} objects.
 *  It uses &Theta;(<em>E</em> + <em>V</em>) space, where <em>E</em> is
 *  the number of edges and <em>V</em> is the number of vertices.
 *  All instance methods take &Theta;(1) time. (Though, iterating over
 *  the edges returned by {@link #adj(int)} takes time proportional
 *  to the degree of the vertex.)
 *  Constructing an empty edge-weighted graph with <em>V</em> vertices takes
 *  &Theta;(<em>V</em>) time; constructing an edge-weighted graph with
 *  <em>E</em> edges and <em>V</em> vertices takes
 *  &Theta;(<em>E</em> + <em>V</em>) time.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Digraph {
    private int V;
    private int E;
    private TreeMap<TagNode, ArrayList<Edge>> adj;

    /**
     * Initializes an empty edge-weighted graph with 0 edges.
     */
    public Digraph() {
        this.V = 0;
        this.E = 0;
        adj = new TreeMap<>(); 
    }

    /**
    * Returns the number of vertices in this edge-weighted graph.
    *
    * @return the number of vertices in this edge-weighted graph
    */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted graph.
     *
     * @return the number of edges in this edge-weighted graph
     */
    public int E() {
        return E;
    }  

    /**
     * Adds the undirected edge {@code e} to this edge-weighted graph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless both endpoints are between {@code 0} and {@code V-1}
     */
    public void addEdge(Edge e) {
        TagNode v = e.either();
        TagNode w = e.other(v);

        if(adj.containsKey(v)){
            adj.get(v).add(e);
        } else {
            ArrayList<Edge> list = new ArrayList<>();
            list.add(e);
            adj.put(v, list);
        }
        if (adj.containsKey(w)) {
            adj.get(w).add(e);
        } else {
            ArrayList<Edge> list = new ArrayList<>();
            list.add(e);
            adj.put(w, list);
        }
        E++;
        V = adj.size();
    }

    /**
     * Returns the edges incident on vertex {@code v}.
     *
     * @param  v the vertex
     * @return the edges incident on vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Edge> adj(TagNode v) {
        return adj.get(v);
    }

    /**
     * Returns the degree of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the degree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int degree(TagNode v) {
        return adj.get(v).size();
    }

    /**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * {@code for (Edge e : G.edges())}.
     *
     * @return all edges in this edge-weighted graph, as an iterable
     */
    public Iterable<Edge> edges() {
        ArrayList<Edge> list = new ArrayList<>();
        for (TagNode v : adj.keySet()) {
            for (Edge e : adj.get(v)) {
                list.add(e);
            }
        }
        return list;
    }
}
