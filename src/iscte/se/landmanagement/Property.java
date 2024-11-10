package iscte.se.landmanagement;

public class Property {
    private int propertyID;
    private double parcelID;
    private int parcelNum;
    private double shapeLength;
    private double shapeArea;
    private String propertyGeometry;
    private int ownerID;


    public Property(int ownerID, String propertyGeometry, double shapeArea, double shapeLength, int parcelNum, double parcelID, int propertyID) {
        this.ownerID = ownerID;
        this.propertyGeometry = propertyGeometry;
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

    public String getPropertyGeometry() {
        return propertyGeometry;
    }

    public void setPropertyGeometry(String propertyGeometry) {
        this.propertyGeometry = propertyGeometry;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

}

