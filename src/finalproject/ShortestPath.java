package finalproject;


import finalproject.system.Tile;


public class ShortestPath extends PathFindingService {
    //TODO level 4: find distance prioritized path
    public ShortestPath(Tile start) {
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
                    double weight = neighbour.distanceCost;
                    if(g.checkIfMetroTile(t) && g.checkIfMetroTile(neighbour))
                        weight = g.converToMetroTile(neighbour).metroDistanceCost;
                    g.addEdge(t,neighbour,weight);
                }
            }
        }

	}
    
}