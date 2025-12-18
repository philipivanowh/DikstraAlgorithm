package finalproject;


import java.util.ArrayList;
import java.util.LinkedList;

import finalproject.system.Tile;

public class SafestShortestPath extends ShortestPath {
    public int health;
    public Graph costGraph;
    public Graph damageGraph;
    public Graph aggregatedGraph;

    public SafestShortestPath(Tile start, int health) {
        super(start);
        this.health = health;
    }

    @Override
    public void generateGraph() {
        g = new Graph(GraphTraversal.BFS(this.source));

        // Initialize all three graphs
        this.costGraph = new Graph(g.vertices);
        this.damageGraph = new Graph(g.vertices);
        this.aggregatedGraph = new Graph(g.vertices);

        // Generate costGraph with distance costs
        for (Tile t : g.vertices) {
            for (Tile neighbour : t.adjacentTiles) {
                if (neighbour.isWalkable()) {
                    double weight = neighbour.distanceCost;
                    if (g.checkIfMetroTile(t) && g.checkIfMetroTile(neighbour)) {
                        weight = g.converToMetroTile(neighbour).metroDistanceCost;
                    }
                    this.costGraph.addEdge(t, neighbour, weight);
                }
            }
        }

        // Generate damageGraph with damage costs
        for (Tile t : g.vertices) {
            for (Tile neighbour : t.adjacentTiles) {
                if (neighbour.isWalkable()) {
                    double weight = neighbour.damageCost;
                    this.damageGraph.addEdge(t, neighbour, weight);
                }
            }
        }

        // Generate aggregatedGraph with damage costs initially
        for (Tile t : g.vertices) {
            for (Tile neighbour : t.adjacentTiles) {
                if (neighbour.isWalkable()) {
                    double weight = neighbour.damageCost;
                    this.aggregatedGraph.addEdge(t, neighbour, weight);
                }
            }
        }

        // Set the parent's graph to costGraph
        this.g = this.costGraph;
    }

    @Override
    public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints) {
        // If no waypoints, just find direct path
        if (waypoints.isEmpty()) {
            return findPathLARAC(start, this.costGraph.getEnd());
        }

        // Handle waypoints by breaking into segments
        ArrayList<Tile> tracePath = new ArrayList<>();

        // Add start to the beginning (don't modify original waypoints)
        Tile current = start;

        // Go through each waypoint
        for (Tile waypoint : waypoints) {
            ArrayList<Tile> segment = findPathLARAC(current, waypoint);
            if (segment == null) return null;

            // Add all tiles except the last one (to avoid duplicates)
            for (int i = 0; i < segment.size() - 1; i++) {
                tracePath.add(segment.get(i));
            }
            current = waypoint;
        }

        // Add final segment from last waypoint to destination
        ArrayList<Tile> lastSegment = findPathLARAC(current, this.costGraph.getEnd());
        if (lastSegment == null) return null;

        tracePath.addAll(lastSegment);
        return tracePath;
    }

    private ArrayList<Tile> findPathLARAC(Tile start, Tile end) {
        // Step 1: Find path with minimum distance cost
        this.g = this.costGraph;
        ArrayList<Tile> pc = super.findPath(start, end);
        if (pc == null) return null;

        double damagePc = this.damageGraph.computePathCost(pc);

        if (damagePc <= this.health) {
            return pc; // Path is safe enough with minimum distance
        }

        // Step 2: Find path with minimum damage cost
        this.g = this.damageGraph;
        ArrayList<Tile> pd = super.findPath(start, end);
        if (pd == null) return null;

        double damagePd = this.damageGraph.computePathCost(pd);

        if (damagePd > this.health) {
            return null; // No safe path exists
        }

        // Step 3-5: Iterate using LARAC until convergence
        int maxIterations = 1000; // Prevent infinite loops
        int iterations = 0;

        while (iterations < maxIterations) {
            iterations++;

            // Step 3: Compute lambda
            double costPc = this.costGraph.computePathCost(pc);
            double costPd = this.costGraph.computePathCost(pd);

            double damageDiff = damagePd - damagePc;

            // Avoid division by zero
            if (Math.abs(damageDiff) < 1e-10) {
                return pd;
            }

            double lambda = (costPc - costPd) / damageDiff;

            // Update aggregated graph with new lambda
            updateAggregatedGraph(lambda);

            // Step 4: Find path with minimum aggregated cost
            this.g = this.aggregatedGraph;
            ArrayList<Tile> pr = super.findPath(start, end);
            if (pr == null) return pd; // Fallback to safest path

            double aggCostPr = this.aggregatedGraph.computePathCost(pr);
            double aggCostPc = this.aggregatedGraph.computePathCost(pc);

            // Check termination condition: if aggregated costs are equal, return safest path
            if (Math.abs(aggCostPr - aggCostPc) < 1e-6) {
                return pd;
            }

            double damagePr = this.damageGraph.computePathCost(pr);

            // Update pc or pd based on damage constraint
            if (damagePr <= this.health) {
                pd = pr;
                damagePd = damagePr;
            } else {
                pc = pr;
                damagePc = damagePr;
            }
        }

        // If we hit max iterations, return the safest feasible path
        return pd;
    }

    private void updateAggregatedGraph(double lambda) {
        // Clear and rebuild aggregated graph edges with formula: cλ = c + λ * d
        this.aggregatedGraph.edges.clear();

        for (Graph.Edge costEdge : this.costGraph.getAllEdges()) {
            Tile origin = costEdge.origin;
            Tile dest = costEdge.destination;

            double c = costEdge.weight;
            double d = findEdgeWeight(this.damageGraph, origin, dest);

            double aggregatedWeight = c + lambda * d;
            this.aggregatedGraph.addEdge(origin, dest, aggregatedWeight);
        }
    }

    private double findEdgeWeight(Graph graph, Tile origin, Tile dest) {
        for (Graph.Edge edge : graph.getAllEdges()) {
            if (edge.origin == origin && edge.destination == dest) {
                return edge.weight;
            }
        }
        return 0;
    }
}