import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class NewtonRaphsonSolver {
    private final int AFTER_COMMA = 2;

    private final double x0;

    private final double precision;

    private final Function<Double, Double> getFstDeriv;

    private final Function<Double, Double> getSndDeriv;

    public NewtonRaphsonSolver(double x0, double precision,
                               Function<Double, Double> fstDeriv,
                               Function<Double, Double> sndDeriv) {
        this.x0 = x0;
        this.precision = precision;
        this.getFstDeriv = fstDeriv.andThen(x -> PrecisionRound.round(x, this.AFTER_COMMA));
        this.getSndDeriv = sndDeriv.andThen(x -> PrecisionRound.round(x, this.AFTER_COMMA));
    }

    public List<double[]> solve(int iterationsNum) {
        List<double[]> res = new ArrayList<>();
        double cur = x0;
        double curFstDeriv = this.getFstDeriv.apply(cur);
        double curSndDeriv = this.getSndDeriv.apply(cur);
        double next = PrecisionRound.round(cur - curFstDeriv / curSndDeriv, this.AFTER_COMMA);
        res.add(new double[]{cur, curFstDeriv, curSndDeriv, next});
        while (Math.abs(curFstDeriv) > this.precision) {
            cur = next;
            curFstDeriv = this.getFstDeriv.apply(cur);
            curSndDeriv = this.getSndDeriv.apply(cur);
            next = PrecisionRound.round(cur - curFstDeriv / curSndDeriv, this.AFTER_COMMA);
            res.add(new double[]{cur, curFstDeriv, curSndDeriv, next});
            if (res.size() == iterationsNum) {
                break;
            }
        }

        return res;
    }
}
