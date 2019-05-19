package com.example.paualtet.squared;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class GridStep {

    public int size;
    public int steps;
    public int[] grid;
    public int[][] table;

    public GridStep(int siz, int ste, int[] gri){
        this.size = siz;
        this.steps = ste;
        this.grid = gri;

        table = new int[size][size];
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                table[i][j] = grid[i*size + j];
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public PriorityQueue<GridStep> possibleMoves(){

        PriorityQueue<GridStep> ret = new PriorityQueue<GridStep>(new GridComparator());

        boolean left = true;
        boolean right = true;
        boolean up = true;
        boolean down = true;


        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (table[i][j] == 0) {

                    if (i == 0) up = false;
                    if (i == size - 1) down = false;

                    if (j == 0) left = false;
                    if (j == size - 1) right = false;

                    if (up) {
                        ret.add(new GridStep(size, steps + 1, swap(grid, size, i, j, i - 1, j)));
                    }
                    if (down) {
                        ret.add(new GridStep(size, steps + 1, swap(grid, size, i, j, i + 1, j)));
                    }

                    if (left) {
                        ret.add(new GridStep(size, steps + 1, swap(grid, size, i, j, i, j - 1)));
                    }
                    if (right) {
                        ret.add(new GridStep(size, steps + 1, swap(grid, size, i, j, i, j + 1)));
                    }

                    return ret;
                }
            }
        }
        return ret;
    }

    public int[] swap (int[] values, int size, int x0, int y0, int x1, int y1){
        int value0 = values[x0*size + y0];
        int value1 = values[x1*size + y1];
        values[x0*size + y0] = value1;
        values[x1*size + y1] = value0;

        return values;
    }

    public boolean stepSolved(){

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                int value = grid[i*size +j];
                int expected = i*size +j+1;
                if (value != expected%grid.length) return false;
            }
        }
        return true;
    }
}
