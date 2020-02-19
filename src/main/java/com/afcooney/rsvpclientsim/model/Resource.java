package com.afcooney.rsvpclientsim.model;

import java.util.List;

/**
 * POJO for json file containing Resource information
 */
public class Resource {

    private int interstate;
    private long interval;
    private List<Point> points;
    private PointsHolder pointsHolder;
    private List<String> tags;

    /**
     *
     * @return the interstate of a resource
     */
    public int getInterstate() {
        return interstate;
    }

    /**
     *
     * @param interstate the interstate to set for a resource
     */
    public void setInterstate(int interstate) {
        this.interstate = interstate;
    }

    /**
     *
     * @return the interval of a resource
     */
    public long getInterval() {
        return interval;
    }

    /**
     *
     * @param interval the interval to set for a resource
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     *
     * @return the list of points of a resource
     */
    public List <Point> getPoints() {
        return points;
    }

    /**
     *
     * @param points the list of points to set for a resource
     */
    public void setPoints(List <Point> points) {
        this.points = points;
    }

    /**
     *
     * @return the list of tags of a resource
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags the list list of tags to set for a resource
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Initializes a PointsHolder object if not already initialized and
     * calls a function that traverses back and forth to get the next point
     * from a list of Point objects
     * @return the next point in the traversal of the points list
     */
    public Point getNextPoint() {
        if (pointsHolder == null){
            pointsHolder = new PointsHolder(points);
        }
        return pointsHolder.getNextPoint();
    }
}