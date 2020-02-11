package com.afcooney.rsvpclientsim.model;

import java.util.ArrayList;
import java.util.List;

public class PointsHolder {

    private List<Point> points = new ArrayList<>();
    private int i = 0;
    private Direction direction = Direction.UP;

    public PointsHolder(List<Point> points) {
        this.points = points;
    }

    public Point getNextPoint(){
        Point point = null;

        if (points.size() < 3){
            if (direction == Direction.UP){
                direction = Direction.DOWN;
                return points.get(0);
            }
            else if (direction == Direction.DOWN){
                direction = Direction.UP;
                return points.get(1);
            }
        }

        if (i >= 0 && i < points.size()){
            point = points.get(i);
            if (direction == Direction.UP){
                i++;
            }
            else if (direction == Direction.DOWN){
                i--;
            }
        }
        else {
            if (direction == Direction.UP){
                direction = Direction.DOWN;
                point = points.get(points.size() - 2);
                i = points.size() - 3;
            }
            else if (direction == Direction.DOWN){
                direction = Direction.UP;
                point = points.get(1);
                i = 2;
            }
        }
        return point;
    }

    enum Direction {
        UP, DOWN
    }
}
