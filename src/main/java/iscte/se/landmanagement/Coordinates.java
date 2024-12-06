package iscte.se.landmanagement;

import java.util.ArrayList;
import java.util.List;

public class Coordinates {
    private double x;
    private double y;

    static Coordinates avg(List<Coordinates> coords) {
        Coordinates result = new Coordinates(0, 0);
        for (Coordinates coord : coords) {
            result.setX(result.getX() + coord.getX());
            result.setY(result.getY() + coord.getY());
        }
        result.setX(result.getX() / coords.size());
        result.setY(result.getY() / coords.size());
        return result;
    }

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates:" + "x-> " + x + ", y-> " + y + " " ;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coordinates coords = (Coordinates) obj;
        if(coords.x != this.x) {
            return false;
        }
        return coords.y == this.y;
    }
}
