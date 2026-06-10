public class Point2D implements Comparable<Point2D> {
    public final int x;
    public final int y;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getDistance() {
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public int compareTo(Point2D other) {
        int distCmp = Double.compare(this.getDistance(), other.getDistance());
        if (distCmp != 0) return distCmp;
        if (this.x != other.x) return Integer.compare(this.x, other.x);
        return Integer.compare(this.y, other.y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}