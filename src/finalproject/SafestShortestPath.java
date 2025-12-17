package finalproject;


import java.util.ArrayList;
import java.util.LinkedList;

import finalproject.system.Tile;
import finalproject.tiles.MetroTile;

public class SafestShortestPath extends ShortestPath {
	public int health;
	public Graph costGraph;
	public Graph damageGraph;
	public Graph aggregatedGraph;

	//TODO level 8: finish class for finding the safest shortest path with given health constraint
	public SafestShortestPath(Tile start, int health) {

		super(start);
		this.health = health;
	}


    private void generateCostGraph(ArrayList<Tile> nodes){

        this.g = costGraph;

        for(Tile t:nodes){
            for(Tile neighbour : t.adjacentTiles){
                if(neighbour.isWalkable()){

                    double weight = neighbour.distanceCost;
                    if(t instanceof MetroTile && neighbour instanceof  MetroTile)
                        weight = ((MetroTile) neighbour).metroDistanceCost;
                    this.costGraph.addEdge(t,neighbour,weight);
                }
            }
        }
    }

    private void generateDamageGraph(ArrayList<Tile> nodes){

        this.damageGraph = new Graph(nodes);

        for(Tile t:nodes){
            for(Tile neighbour : t.adjacentTiles){
                if(neighbour.isWalkable()){

                    double weight = neighbour.damageCost;
                    this.damageGraph.addEdge(t,neighbour,weight);
                }
            }
        }
    }

    private void generateAggregatedGraph(ArrayList<Tile> nodes){

        this.aggregatedGraph = new Graph(nodes);

        for(Tile t:nodes){
            for(Tile neighbour : t.adjacentTiles){
                if(neighbour.isWalkable()){

                    double weight = neighbour.damageCost;
                    this.aggregatedGraph.addEdge(t,neighbour,weight);
                }
            }
        }
    }


	
	public void generateGraph() {
        ArrayList<Tile> nodes = GraphTraversal.BFS(this.source);


        this.costGraph = new Graph(nodes);

       generateCostGraph(nodes);
       generateDamageGraph(nodes);
       generateAggregatedGraph(nodes);
    }

    @Override
    public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints){

    return null;
    }


}
