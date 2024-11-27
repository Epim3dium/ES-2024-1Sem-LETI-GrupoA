package iscte.se.landmanagement;

public class AABB {
    private double top;
    private double bottom;
    private double left;
    private double right;

    public double getBottom() {
        return bottom;
    }

    public double getTop() {
        return top;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }
    public Coordinates center() {
        return new Coordinates(left + (right - left) / 2, bottom + (top - bottom) / 2);
    }
    public Coordinates area() {
        return new Coordinates(right - left, top - bottom);
    }
    public boolean isOverlapping(AABB other, double threshold ){
        if(other == null) return false;
        return (left <= other.right + threshold  && right + threshold >= other.left &&
                    bottom <= other.top + threshold && top + threshold >= bottom);
    }

    public AABB() {
        top = -1e100;
        bottom = 1e100;
        left = 1e100;
        right = -1e100;
    }
    public void expandToContain(Coordinates coord) {
        top = Math.max(top, coord.getY());
        bottom = Math.min(bottom, coord.getY());
        right = Math.max(right, coord.getX());
        left = Math.min(left, coord.getX());
    }
    public AABB(double t, double b, double l, double r) {
        top = t;
        bottom = b;
        left = l;
        right = r;
    }
}
