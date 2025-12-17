package finalproject;

import java.util.ArrayList;
import java.util.HashSet;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.*;

public class Graph {
	// TODO level 2: Add fields that can help you implement this data type
    ArrayList<Tile> vertices = new ArrayList<Tile>();
    ArrayList<Edge> edges = new ArrayList<Edge>();

    private Tile start = null;
    private Tile end = null;

    private void setStartAndGoal(){
        for(Tile tile :vertices){
            if(tile.isStart)
                start = tile;
            else if(tile.isDestination)
                end = tile;
        }
    }

    public boolean checkIfMetroTile(Tile t){
        return t instanceof MetroTile;
    }

    public MetroTile converToMetroTile(Tile t){
        return (MetroTile) t;
    }

    public Tile getStart(){
        return this.start;
    }

    public Tile getEnd(){
        return this.end;
    }

    // TODO level 2: initialize and assign all variables inside the constructor
	public Graph(ArrayList<Tile> vertices) {
		this.vertices = vertices;
        setStartAndGoal();
	}
	
    // TODO level 2: add an edge to the graph
    public void addEdge(Tile origin, Tile destination, double weight){
        if(!origin.adjacentTiles.contains(destination))
            origin.adjacentTiles.add(destination);
        this.edges.add(new Edge(origin,destination,weight));
    }
    
    // TODO level 2: return a list of all edges in the graph
	public ArrayList<Edge> getAllEdges() {
		return this.edges;
	}

    public ArrayList<Edge> getEdgesOfTile(Tile t){
        ArrayList<Edge> neighbourEdge = new ArrayList<>();
        for(Edge edge : this.edges)
        {
            if(edge.origin==t){
                neighbourEdge.add(edge);
            }
        }
        return neighbourEdge;
    }
  
	// TODO level 2: return list of tiles adjacent to t
	public ArrayList<Tile> getNeighbors(Tile t) {
    	return t.adjacentTiles;
    }
	
	// TODO level 2: return total cost for the input path
	public double computePathCost(ArrayList<Tile> path) {
        double totalCost = 0;
        for(int i = 0; i<path.size()-1; i++) {
           Edge edge = findEdge(path.get(i),path.get(i+1));
           totalCost += edge.weight;
        }

        return totalCost;
	}

    private Edge findEdge(Tile start,Tile destination){
        for (Edge currEdge : this.edges) {
            if (currEdge.getStart() == start && currEdge.getEnd() == destination)
                return currEdge;
        }
        return null;
    }
	
   
    public static class Edge{
    	Tile origin;
    	Tile destination;
    	double weight;

        // TODO level 2: initialize appropriate fields
        public Edge(Tile s, Tile d, double cost){
        	this.origin = s;
            this.destination = d;
            this.weight = cost;
        }
        
        // TODO level 2: getter function 1
        public Tile getStart(){
            return origin;
        }

        
        // TODO level 2: getter function 2
        public Tile getEnd() {
            return destination;
        }
        
    }
    
}