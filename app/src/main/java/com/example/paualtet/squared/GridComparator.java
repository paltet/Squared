package com.example.paualtet.squared;

import java.util.Comparator;

public class GridComparator implements Comparator<GridStep> {

    @Override
    public int compare(GridStep o1, GridStep o2) {

        int ste1 = o1.steps;
        int ste2 = o2.steps;

        if (ste1 < ste2) return -1;
        else if (ste1 > ste2) return 1;
        else return 0;
    }
}
