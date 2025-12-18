package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class PathFindingService {
	Tile source;
	Graph g;
	
	public PathFindingService(Tile start) {
    	this.source = start;
        this.generateGraph();
    }

	public abstract void generateGraph();

    private void init_single_source(ArrayList<Tile> verticies, Tile source) {

        for(Tile tile : verticies){
            tile.costEstimate = Integer.MAX_VALUE;
            tile.predecessor = null;
        }
        source.costEstimate = 0;
    }

    private void relax(Tile node, Tile predecessor, double edgeWeight, TilePriorityQ priorityQ){
        double newCost = predecessor.costEstimate + edgeWeight;

        if(node.costEstimate > newCost){
            priorityQ.updateKeys(node, predecessor, newCost);
        }
    }

    protected void DikstraAlgorithm(Tile start, Tile end){
        init_single_source(g.vertices, start);

        // visited set to track finalized vertices
        HashMap<Tile, Boolean> visited = new HashMap<>();

        TilePriorityQ priorityQ = new TilePriorityQ(g.vertices);

        while(!priorityQ.isEmpty()){
            Tile curr = priorityQ.removeMin();

            // Mark as visited (finalized)
            visited.put(curr,true);

            // Early termination if we reached the destination
            if(curr == end) break;

            // Relax all outgoing edges
            for(Graph.Edge edge : g.getEdgesOfTile(curr)){
                Tile neighbor = edge.getEnd();

                // Only relax if neighbor hasn't been finalized yet
                if(!visited.containsKey(neighbor)){
                    relax(neighbor, curr, edge.weight, priorityQ);
                }
            }
        }
    }

    //TODO level 4: Implement basic dijkstra's algorithm to find a path to the final unknown destination
    public ArrayList<Tile> findPath(Tile startNode) {

        DikstraAlgorithm(startNode,g.getEnd());

        ArrayList<Tile> tracePath = new ArrayList<>();

        backTrackingPath(g.getEnd(),tracePath);

        return tracePath;

    }
    
    //TODO level 5: Implement basic dijkstra's algorithm to path find to a known destination
    public ArrayList<Tile> findPath(Tile start, Tile end) {

        DikstraAlgorithm(start,end);

        ArrayList<Tile> tracePath = new ArrayList<>();

        backTrackingPath(end,tracePath);

        return tracePath;
    }

    //This implementation includes the end tile within the tracePath
    private void backTrackingPath(Tile traceCurr,ArrayList<Tile> tracePath){
        while(traceCurr!=null) {
            tracePath.addFirst(traceCurr);
            traceCurr = traceCurr.predecessor;
        }
    }

    //Doesn't include the end tile within the tracePath
    private ArrayList<Tile> backTrackingPathExcludeEndTile(Tile traceCurr){
        ArrayList<Tile> path = new ArrayList<>();
        while(traceCurr.predecessor!=null) {
            traceCurr = traceCurr.predecessor;
            path.addFirst(traceCurr);
        }

        return path;
    }



    //TODO level 5: Implement basic dijkstra's algorithm to path find to the final destination passing through given waypoints
    public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints){
        ArrayList<Tile> tracePath = new ArrayList<>();

        waypoints.addFirst(start);
        for(int i = 0; i < waypoints.size()-1; i++){
            includePathNoEnd(tracePath, waypoints.get(i),waypoints.get(i+1));
        }

        tracePath.addAll(findPath(waypoints.getLast()));

        return tracePath;

    }

    protected void includePathNoEnd(ArrayList<Tile> tracePath, Tile start, Tile end){

        DikstraAlgorithm(start,end);

        ArrayList<Tile> path = backTrackingPathExcludeEndTile(end);
        tracePath.addAll(path);
    }


        
}

