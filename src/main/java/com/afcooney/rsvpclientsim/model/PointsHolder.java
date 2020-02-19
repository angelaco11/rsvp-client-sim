package com.afcooney.rsvpclientsim.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that takes a list of Point objects to be traversed
 */

public class PointsHolder {

    private List<Point> points;
    private int i = 0;
    private Direction direction = Direction.UP;

    /**
     * Constructor for PointsHolder Class
     * @param points - the list of points given by a resource
     */
    public PointsHolder(List<Point> points) {
        this.points = points;
    }

    /**
     * Given a list Point objects that contain longitude and latitude values,
     * traverses the list of points one at a time back and forth
     * @return point - the next point in the traversal of the given points list
     */
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
