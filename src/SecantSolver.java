import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SecantSolver {
    private final int AFTER_COMMA = 2;

    private final double[] interval;

    private final double precision;

    private final Function<Double, Point> getFstDeriv;

    public SecantSolver(double[] interval, double precision, Function<Double, Double> fstDeriv) {
        this.interval = interval;
        this.precision = precision;
        this.getFstDeriv = x -> {
            x = PrecisionRound.round(x, this.AFTER_COMMA);
            return new Point(x, PrecisionRound.round(fstDeriv.apply(x), this.AFTER_COMMA));
        };
    }

    private int signum(double x) {
        if (x > 0)
            return 1;
        else if (x < 0)
            return -1;
        else
            return 0;
    }

    public List<Point[]> solve(int iterationsNum) {
        List<Point[]> res = new ArrayList<>();
        Point left = this.getFstDeriv.apply(interval[0]);
        Point right = this.getFstDeriv.apply(interval[1]);
        Point mid = this.getFstDeriv.apply(right.x() - right.y() * (right.x() - left.x()) / (right.y() - left.y()));
        res.add(new Point[]{left, right, mid});
        while (Math.abs(mid.y()) > this.precision) {
            if (signum(left.y()) * signum(mid.y()) < 0) {
                right = mid;
            } else {
                left = mid;
            }
            mid = this.getFstDeriv.apply(right.x() - right.y() * (right.x() - left.x()) / (right.y() - left.y()));
            res.add(new Point[]{left, right, mid});
            if (res.size() == iterationsNum) {
                break;
            }
        }

        return res;
    }
}
