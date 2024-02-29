import java.util.function.Function;

public class GoldenSectionSolver {
    private final int AFTER_COMMA = 2;

    private final Point[] interval;

    private final double precision;

    private final Function<Double, Point> getPoint;

    private final double LEFT_CONST = 0.382;

    private final double RIGHT_CONST = 0.618;

    // interval must contain 2 points - a and b - obtained from SvenSolver.solve() but without the mid point
    public GoldenSectionSolver(Point[] interval, double precision, Function<Double, Double> func) {
        this.interval = interval;
        this.precision = precision;
        this.getPoint = x -> {
            x = PrecisionRound.round(x, this.AFTER_COMMA);
            return new Point(x, PrecisionRound.round(func.apply(x), this.AFTER_COMMA));
        };
    }

    public Point[] solve() {
        double len = this.interval[1].x() - this.interval[0].x();
        if (len <= this.precision) {
            return this.interval;
        }

        Point a = this.interval[0];
        Point b = this.interval[1];
        Point left, right;

        left = this.getPoint.apply(a.x() + this.LEFT_CONST * len);
        right = this.getPoint.apply(a.x() + this.RIGHT_CONST * len);
        do {
            if (left.y() <= right.y()) {
                b = right;
                len = b.x() - a.x();
                right = left;
                left = this.getPoint.apply(a.x() + this.LEFT_CONST * len);
            } else {
                a = left;
                len = b.x() - a.x();
                left = right;
                right = this.getPoint.apply(a.x() + this.RIGHT_CONST * len);
            }
        } while (len > precision);

        return new Point[]{a, b};
    }
}
