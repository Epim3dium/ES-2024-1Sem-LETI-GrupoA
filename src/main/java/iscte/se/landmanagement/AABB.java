package iscte.se.landmanagement;

/**
 * Represents an Axis-Aligned Bounding Box (AABB), commonly used in geometric calculations
 * such as collision detection, spatial queries, and bounding volume hierarchies.
 * <p>
 * The AABB is defined by its top, bottom, left, and right edges in a 2D coordinate system.
 * It provides methods for calculating its center, dimensions, and for checking overlaps with other AABBs.
 */
public class AABB {
    private double top;
    private double bottom;
    private double left;
    private double right;

    /**
     * Creates an unbounded AABB. The initial bounds are set to extreme values
     * to ensure any valid point will adjust the bounds when expanding.
     */
    public AABB() {
        top = -1e100;
        bottom = 1e100;
        left = 1e100;
        right = -1e100;
    }

    /**
     * Creates an AABB with the specified bounds.
     *
     * @param t the top edge of the bounding box.
     * @param b the bottom edge of the bounding box.
     * @param l the left edge of the bounding box.
     * @param r the right edge of the bounding box.
     */
    public AABB(double t, double b, double l, double r) {
        top = t;
        bottom = b;
        left = l;
        right = r;
    }

    /**
     * Gets the Y-coordinate of the bottom edge of the bounding box.
     *
     * @return the bottom edge value.
     */
    public double getBottom() {
        return bottom;
    }

    /**
     * Gets the Y-coordinate of the top edge of the bounding box.
     *
     * @return the top edge value.
     */
    public double getTop() {
        return top;
    }

    /**
     * Gets the X-coordinate of the left edge of the bounding box.
     *
     * @return the left edge value.
     */
    public double getLeft() {
        return left;
    }

    /**
     * Gets the X-coordinate of the right edge of the bounding box.
     *
     * @return the right edge value.
     */
    public double getRight() {
        return right;
    }

    /**
     * Calculates the center of the bounding box.
     *
     * @return a {@link Coordinates} object representing the center of the bounding box.
     */
    public Coordinates center() {
        return new Coordinates(left + (right - left) / 2, bottom + (top - bottom) / 2);
    }

    /**
     * Calculates the dimensions of the bounding box.
     *
     * @return a {@link Coordinates} object where the X value represents the width
     *         and the Y value represents the height of the bounding box.
     */
    public Coordinates area() {
        return new Coordinates(right - left, top - bottom);
    }

    /**
     * Checks if this bounding box overlaps with another bounding box within a given threshold.
     * The threshold expands the dimensions of the boxes for the purpose of overlap detection.
     *
     * @param other     the other {@link AABB} to check for overlap.
     * @param threshold the margin by which the bounding boxes are expanded for the check.
     *                  A positive threshold allows for detection of near overlaps.
     * @return {@code true} if the bounding boxes overlap (considering the threshold),
     *         {@code false} otherwise.
     */
    public boolean isOverlapping(AABB other, double threshold) {
        if (other == null) return false;
        return (left <= other.right + threshold && right + threshold >= other.left &&
                bottom <= other.top + threshold && top + threshold >= bottom);
    }


    /**
     * Expands the bounding box (AABB) to include the given coordinates.
     * If the coordinates are outside the current bounding box, the bounding box will be expanded
     * to encompass the coordinates. The expansion will update the top, bottom, left, and right edges
     * of the bounding box to include the new coordinates.
     *
     * @param coord The {@link Coordinates} object that will be used to expand the AABB.
     *              The AABB will be adjusted to include this point if necessary.
     */

    public void expandToContain(Coordinates coord) {
        top = Math.max(top, coord.getY());
        bottom = Math.min(bottom, coord.getY());
        right = Math.max(right, coord.getX());
        left = Math.min(left, coord.getX());
    }

}
