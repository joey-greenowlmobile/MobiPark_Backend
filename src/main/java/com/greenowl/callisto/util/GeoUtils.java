package com.greenowl.callisto.util;

import com.google.common.base.Preconditions;
import com.greenowl.callisto.web.rest.dto.Location;

public class GeoUtils {

    private static final double R = 6371;

    /**
     * Determines the distance between two points.
     *
     * @param lat1 The latitude of the initial point.
     * @param lng1 The longitude of the initial point.
     * @param lat2 The latitude of the destination point.
     * @param lng2 The longitude of the destination point.
     * @return The distance between the two points entered in meters.
     */
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    /**
     * Determines the distance between two locations.
     *
     * @param loc1 Location 1.
     * @param loc2 Location 2.
     * @return Distance between two locations in meters.
     */
    public static double distFrom(Location loc1, Location loc2) {
        Preconditions.checkNotNull(loc1, loc2);
        return distFrom(loc1.getLat(), loc1.getLon(), loc2.getLat(), loc2.getLon());
    }

    /**
     * gets a Location object :distance meters away from :lat and :lon NW direction away.
     */
    public static Location getSEPointAway(double lat, double lon, double distance) {
        double radius = distance / 1000; // km

        double x = lat - Math.toDegrees(radius / R);
        double y = lon + Math.toDegrees(radius / R / Math.cos(Math.toRadians(lat)));
        return new Location(x, y);
    }

    /**
     * gets a Location object :distance meters away from :lat and :lon SE direction away.
     */
    public static Location getNWPointAway(double lat, double lon, double distance) {
        double radius = distance / 1000; // km

        double x = lat + Math.toDegrees(radius / R);
        double y = lon - Math.toDegrees(radius / R / Math.cos(Math.toRadians(lat)));

        return new Location(x, y);
    }


}
