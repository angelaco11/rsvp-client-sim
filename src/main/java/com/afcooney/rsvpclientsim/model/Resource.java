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

    public int getInterstate() {
        return interstate;
    }

    public void setInterstate(int interstate) {
        this.interstate = interstate;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public List <Point> getPoints() {
        return points;
    }

    public void setPoints(List <Point> points) {
        this.points = points;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Point getNextPoint() {
        if (pointsHolder == null){
            pointsHolder = new PointsHolder(points);
        }
        return pointsHolder.getNextPoint();
    }
}