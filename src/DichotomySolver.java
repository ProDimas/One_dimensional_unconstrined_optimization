import java.util.function.Function;

public class DichotomySolver {
    private final int AFTER_COMMA = 2;

    private final Point[] interval;

    private final double precision;

    private final Function<Double, Point> getPoint;

    // interval must contain 3 points - a, xm and b - obtained from SvenSolver.solve()
    public DichotomySolver(Point[] interval, double precision, Function<Double, Double> func) {
        this.interval = interval;
        this.precision = precision;
        this.getPoint = x -> {
            x = PrecisionRound.round(x, this.AFTER_COMMA);
            return new Point(x, PrecisionRound.round(func.apply(x), this.AFTER_COMMA));
        };
    }

    public Point[] solve() {
        Point a = this.interval[0];
        Point mid = this.interval[1];
        Point b = this.interval[2];
        Point left, right;

        while (b.x() - a.x() > this.precision) {
            left = this.getPoint.apply((a.x() + mid.x()) / 2);
            right = this.getPoint.apply((mid.x() + b.x()) / 2);
            if (left.y() < mid.y()) {
                b = mid;
                mid = left;
            } else if (right.y() < mid.y()) {
                a = mid;
                mid = right;
            } else {
                a = left;
                b = right;
            }
        }

        return new Point[]{a, b};
    }
}
