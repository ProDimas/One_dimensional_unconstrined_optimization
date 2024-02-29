import java.util.function.Function;

// For unimodal functions
public class SvenSolver {
    private final int AFTER_COMMA = 2;

    private final double x0;

    private double step;

    private final Function<Double, Point> getPoint;

    public SvenSolver(double x0, double step, Function<Double, Double> func) {
        this.x0 = PrecisionRound.round(x0, this.AFTER_COMMA);
        this.step = PrecisionRound.round(step, this.AFTER_COMMA);
        this.getPoint = x -> {
            x = PrecisionRound.round(x, this.AFTER_COMMA);
            return new Point(x, PrecisionRound.round(func.apply(x), this.AFTER_COMMA));
        };
    }

    private void reverseArray(Point[] arr) {
        Point temp;
        for (int i = 0; i < arr.length / 2; i++) {
            temp = arr[i];
            arr[i] = arr[arr.length - 1 - i];
            arr[arr.length - 1 - i] = temp;
        }
    }

    public Point[] solve() {
        Point cur = this.getPoint.apply(this.x0);
        Point left = this.getPoint.apply(this.x0 - this.step);
        Point right = this.getPoint.apply(this.x0 + this.step);
        Point prev, next;

        prev = cur;
        if ((left.y() < cur.y()) && (right.y() > cur.y())) {
            this.step = -this.step;
            cur = left;
        } else if ((left.y() > cur.y()) || (right.y() < cur.y())) {
            cur = right;
        } else {
            return new Point[]{left, cur, right};
        }

        int k = 1;
        next = this.getPoint.apply(cur.x() + this.step * Math.pow(2, k));
        while (next.y() < cur.y()) {
            k++;
            prev = cur;
            cur = next;
            next = this.getPoint.apply(cur.x() + this.step * Math.pow(2, k));
        }

        Point[] interval;
        Point mid = this.getPoint.apply((cur.x() + next.x()) / 2);
        if (next.y() == cur.y()) {
            interval = new Point[]{cur, mid, next};
        } else {
            if (mid.y() == cur.y()) {
                interval = new Point[]{
                        cur,
                        this.getPoint.apply((cur.x() + mid.x()) / 2),
                        mid
                };
            } else {
                interval = new Point[]{prev, cur, mid};
            }
        }

        if (this.step < 0) {
            this.reverseArray(interval);
        }

        return interval;
    }
}