package com.afcooney.rsvpclientsim.model;

/**
 * A class for point objects that contain a latitude and longitude value pair
 */
public class Point {

    private double latitude;
    private double longitude;

    /**
     *
     * @return the latitude of a point
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude the latitude to set for a point
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return the longitude of a point
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude the longitude to set for a point
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
