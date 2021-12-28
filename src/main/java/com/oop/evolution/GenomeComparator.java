package com.oop.evolution;

import java.util.ArrayList;
import java.util.Comparator;

public class GenomeComparator implements Comparator<ArrayList<Integer>> {

    @Override
    public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
        for(int i=0;i<32;i++){
            if(o1.get(i)<o2.get(i)){
                return 1;
            }
            else if(o1.get(i)>o2.get(i)){
                return -1;
            }
        }
        return 0;
    }
}
