import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class DSKPowellSolver {
    private final int AFTER_COMMA = 2;

    private final Point[] interval;

    private final double precision;

    private final Function<Double, Point> getPoint;

    public DSKPowellSolver(Point[] interval, double precision, Function<Double, Double> func) {
        this.interval = interval;
        this.precision = precision;
        this.getPoint = x -> {
            x = PrecisionRound.round(x, this.AFTER_COMMA);
            return new Point(x, PrecisionRound.round(func.apply(x), this.AFTER_COMMA));
        };
    }

    public DSKPowellSolver(double x0, double step, double precision, Function<Double, Double> func) {
        this.interval = new SvenSolver(x0, step, func).solve();
        this.precision = precision;
        this.getPoint = x -> {
            x = PrecisionRound.round(x, this.AFTER_COMMA);
            return new Point(x, PrecisionRound.round(func.apply(x), this.AFTER_COMMA));
        };
    }

    private double fractionForA(Point first, Point second) {
        return (first.y() - second.y()) / (first.x() - second.x());
    }

    public List<Point[]> solve() {
        List<Point[]> res = new ArrayList<>();
        Point left = this.interval[0];
        Point mid = this.interval[1];
        Point right = this.interval[2];

        double delta = mid.x() - left.x();
        Point polynomialMinimal = this.getPoint.apply(
                mid.x() + delta * (left.y() - right.y()) / (2 * (left.y() - 2 * mid.y() + right.y()))
        );
        res.add(new Point[]{left, mid, right, polynomialMinimal});

        while (!((Math.abs(mid.y() - polynomialMinimal.y()) <= this.precision)
                &&
                (Math.abs(mid.x() - polynomialMinimal.x()) <= this.precision))) {
            Point[] points = new Point[]{left, mid, right, polynomialMinimal};
            Arrays.sort(points, Comparator.comparingDouble(Point::y));
            points = Arrays.copyOfRange(points, 0, points.length - 1);
            Arrays.sort(points, Comparator.comparingDouble(Point::x));
            left = points[0];
            mid = points[1];
            right = points[2];

            double a1 = PrecisionRound.round(fractionForA(mid, left), this.AFTER_COMMA);
            double a2 = PrecisionRound.round(
                    1 / (right.x() - mid.x()) * (fractionForA(right, left) - fractionForA(mid, left)),
                    this.AFTER_COMMA);
            polynomialMinimal = this.getPoint.apply((left.x() + mid.x()) / 2 - a1 / (2 * a2));
            res.add(new Point[]{left, mid, right, polynomialMinimal});
        }

        return res;
    }
}
