package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;

public class FastestPath extends PathFindingService {
    //TODO level 6: find time prioritized path
    public FastestPath(Tile start) {
        super(start);
        generateGraph();
    }

	@Override
	public void generateGraph() {


        this.g = new Graph(GraphTraversal.BFS(this.source));

        //add Edges
        for(Tile t:g.vertices){
            for(Tile neighbour : t.adjacentTiles){
                if(neighbour.isWalkable()){

                    double weight = neighbour.timeCost;
                    if(g.checkIfMetroTile(t) && g.checkIfMetroTile(neighbour))
                        weight = g.converToMetroTile(neighbour).metroTimeCost;
                    g.addEdge(t,neighbour,weight);
                }
            }
        }
		
	}

}
