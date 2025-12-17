package finalproject;

import java.util.ArrayList;
import java.util.Arrays;


import finalproject.system.Tile;

public class TilePriorityQ {
	// TODO level 3: Add fields that can help you implement this data type
    Tile[] priorityQ;
    int size = 0;
	// TODO level 3: implement the constructor for the priority queue
	public TilePriorityQ (ArrayList<Tile> vertices) {
	    priorityQ = new Tile[vertices.size() + 1];
        buildPriorityQ(vertices);
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void buildPriorityQ(ArrayList<Tile> vertices){
        for(int i = 0; i < vertices.size(); i++){
            add(vertices.get(i));
        }
    }

    public void add(Tile newTile){
        size = size + 1;
        priorityQ[size] = newTile;
        upHeap(size);


    }

    private void upHeap(int i){
        while(i > 1 && priorityQ[i].costEstimate < priorityQ[i/2].costEstimate){
            Tile temp = priorityQ[i];
            priorityQ[i] = priorityQ[i/2];
            priorityQ[i/2] = temp;
            i = i/2;
        }
    }

	
	// TODO level 3: implement remove min as seen in class
	public Tile removeMin() {
        if(size==0) return null;
		Tile tile = priorityQ[1];
        priorityQ[1] = priorityQ[size];
        priorityQ[size] = null;
        size = size - 1;
        downHeap(1,size);
        return tile;

	}

    private void downHeap(int start, int maxIndex){

        int i = start;
        while(i*2 <= maxIndex) {
            int child = 2 * i;
            if (child < maxIndex) {
                if (priorityQ[child + 1].costEstimate < priorityQ[child].costEstimate)
                    child = child + 1;
            }
            if (priorityQ[child].costEstimate < priorityQ[i].costEstimate) {
                Tile temp = priorityQ[child];
                priorityQ[child] = priorityQ[i];
                priorityQ[i] = temp;
                i = child;
                continue;
            }
            break;
        }
    }
	
	// TODO level 3: implement updateKeys as described in the pdf
	public void updateKeys(Tile t, Tile newPred, double newEstimate) {
        t.predecessor = newPred;
        t.costEstimate = newEstimate;

        int curIndex = 0;
        for(int i = 1; i <= size; i++){
            if(priorityQ[i] == t){
                curIndex=i;
                break;
            }
        }

        if(curIndex != 0){
            upHeap(curIndex);
        }
	}
	
}
