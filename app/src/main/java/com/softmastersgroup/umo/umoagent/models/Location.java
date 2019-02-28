package com.softmastersgroup.umo.umoagent.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Location implements Serializable {
    @Expose private String type;
    @Expose private String city;
    @Expose private String streetaddress;
    @Expose private String country;
    @Expose private String buildingno;
    @Expose private String housenumber;
    @Expose private String unitno;
    @Expose private String popularname;
    @Expose private String region;
    @Expose private double latitude;
    @Expose private double longitude;

    public Location() {
    }

    public Location(String type, String city, String streetaddress, String country, String buildingno, String housenumber, String unitno, String popularname, String region, double latitude, double longitude) {
        this.type = type;
        this.city = city;
        this.streetaddress = streetaddress;
        this.country = country;
        this.buildingno = buildingno;
        this.housenumber = housenumber;
        this.unitno = unitno;
        this.popularname = popularname;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetaddress() {
        return streetaddress;
    }

    public void setStreetaddress(String streetaddress) {
        this.streetaddress = streetaddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBuildingno() {
        return buildingno;
    }

    public void setBuildingno(String buildingno) {
        this.buildingno = buildingno;
    }

    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }

    public String getPopularname() {
        return popularname;
    }

    public void setPopularname(String popularname) {
        this.popularname = popularname;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }
}
