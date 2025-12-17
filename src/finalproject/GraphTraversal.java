package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class GraphTraversal
{


	//TODO level 1: implement BFS traversal starting from s
	public static ArrayList<Tile> BFS(Tile s)
	{
        if(!s.isWalkable())
            return null;

        ArrayList<Tile> result = new ArrayList<Tile>();
        LinkedList<Tile> queue = new LinkedList<Tile>();

        result.add(s);
        queue.add(s);
        //if the input is not walkable than return null


        while(!queue.isEmpty()){
            Tile curr = queue.removeFirst();

            //push all the walkable and not visited adjacent tile
            for(Tile tile : curr.adjacentTiles){
                if(!result.contains(tile) && tile.isWalkable()) {
                    result.add(tile);
                    queue.add(tile);
                }
            }
        }

        return result;
	}


	//TODO level 1: implement DFS traversal starting from s
	public static ArrayList<Tile> DFS(Tile s) {

        //if the input is not walkable than return null
        if(!s.isWalkable())
            return null;

        LinkedList<Tile> stack = new LinkedList<Tile>();
        ArrayList<Tile> result = new ArrayList<Tile>();

        stack.add(s);


        while(!stack.isEmpty()){
            Tile curr = stack.removeLast();
            result.add(curr);

            //push all the walkable and not visited adjacent tile
            for(Tile tile : curr.adjacentTiles){
                if(!result.contains(tile)  && tile.isWalkable()) {
                    stack.add(tile);
                }
            }
        }

		return result;
	}


}  