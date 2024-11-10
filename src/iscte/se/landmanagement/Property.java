package iscte.se.landmanagement;

import java.util.ArrayList;

public class Property {
    private int propertyID;
    private double parcelID;
    private int parcelNum;
    private double shapeLength;
    private double shapeArea;
    private ArrayList<Coordinates> corners;
    private int ownerID;


    public Property(int propertyID,double parcelID,int ParcelNum,double shapeLength,double shapeArea,ArrayList<Coordinates> coorners,int ownerID) {
        this.ownerID = ownerID;
        this.corners = corners;
          this.shapeArea = shapeArea;
        this.shapeLength = shapeLength;
        this.parcelNum = parcelNum;
        this.parcelID = parcelID;
        this.propertyID = propertyID;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }

    public double getParcelID() {
        return parcelID;
    }

    public void setParcelID(double parcelID) {
        this.parcelID = parcelID;
    }

    public int getParcelNum() {
        return parcelNum;
    }

    public void setParcelNum(int parcelNum) {
        this.parcelNum = parcelNum;
    }

    public double getShapeLength() {
        return shapeLength;
    }

    public void setShapeLength(double shapeLength) {
        this.shapeLength = shapeLength;
    }

    public double getShapeArea() {
        return shapeArea;
    }

    public void setShapeArea(double shapeArea) {
        this.shapeArea = shapeArea;
    }

    public ArrayList<Coordinates> getCorners() {
        return corners;
    }

    public void setCorners(ArrayList<Coordinates> corners) {
        this.corners = corners;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

}

