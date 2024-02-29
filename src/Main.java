import java.util.List;
import java.util.function.Function;

public class Main {
    private static final int AFTER_COMMA = 2;

    public static void main(String[] args) {
        Function<Double, Double> f = x -> Math.pow(x, 2) - 9 * x;
        double x0 = 5.5;
        double step = 0.1;
        // Алгоритм Свена
        SvenSolver exSven = new SvenSolver(x0, step, f);
        Point[] svenPoints = exSven.solve();
        System.out.println("Sven algorithm:");
        for (int i = 0; i < svenPoints.length; i++) {
            System.out.println("x" + (i + 1) + ": " + svenPoints[i].toString());
        }

        double precision = 0.2;
        // Метод дихотомії
        DichotomySolver exDich = new DichotomySolver(svenPoints, precision, f);
        Point[] dichPoints = exDich.solve();
        System.out.println("\nDichotomy algorithm:");
        System.out.println("a: " + dichPoints[0]);
        System.out.println("b: " + dichPoints[1]);

        svenPoints = new Point[] {svenPoints[0], svenPoints[2]};
        // Метод "золотого перетину"
        GoldenSectionSolver exGold = new GoldenSectionSolver(svenPoints, precision, f);
        Point[] goldPoints = exGold.solve();
        System.out.println("\nGolden section algorithm:");
        System.out.println("a: " + goldPoints[0]);
        System.out.println("b: " + goldPoints[1]);

        precision = 0.01;
        // Метод ДСК-Пауелла
        DSKPowellSolver exDSKP = new DSKPowellSolver(x0, step, precision, f);
        System.out.println("\nDSK-Powell algorithm:");
        exDSKP.solve().forEach(
                arr -> {
                    for (int i = 0; i < arr.length - 1; i++) {
                        System.out.println("x" + (i + 1) + ": " + arr[i].toString());
                    }
                    System.out.println("x*: " + arr[arr.length - 1].toString());
                    System.out.println("-------------------");
                }
        );

        Function<Double, Double> f2 = x -> Math.pow(x, 2) + 9 / x;
        Function<Double, Double> f2FD = x -> 2 * x - 9 / Math.pow(x, 2);
        Function<Double, Double> f2SD = x -> 2 + 18 / Math.pow(x, 3);
        x0 = 0.1;
        // Можна поставити точність обчислень для методів, що базуються на розв'язанні нелінійних рівнянь,
        // рівною 0, бо пріоритет надаватиметься тому, щоб висвітлити лише перші 3 ітерації
        precision = 0;
        int iterationsNum = 3;
        // Метод Ньютона-Рафсона
        NewtonRaphsonSolver exNewt = new NewtonRaphsonSolver(x0, precision, f2FD, f2SD);
        System.out.println("\nNewton-Raphson algorithm:");
        List<double[]> resultNewt = exNewt.solve(iterationsNum);
        resultNewt.forEach(
                arr -> {
                    System.out.println("x_i: " + arr[0]);
                    System.out.println("f_i': " + arr[1]);
                    System.out.println("f_i'': " + arr[2]);
                    System.out.println("x_(i+1): " + arr[3]);
                    System.out.println("-------------------");
                }
        );
        double [] lastNewt = resultNewt.get(resultNewt.size() - 1);
        System.out.println("x and f(x) minimized:");
        System.out.print(lastNewt[lastNewt.length - 1]);
        System.out.println(" " + PrecisionRound.round(f2.apply(lastNewt[lastNewt.length - 1]), AFTER_COMMA));

        double[] interval = new double[] {0.1, 2};
        // Метод середньої точки
        MiddlePointSolver exMiddle = new MiddlePointSolver(interval, precision, f2FD);
        System.out.println("\nMiddle point algorithm:");
        List<Point[]> resultMiddle = exMiddle.solve(iterationsNum);
        resultMiddle.forEach(
                arr -> {
                    System.out.println("x1: " + arr[0]);
                    System.out.println("x2: " + arr[1]);
                    System.out.println("x_ср: " + arr[2]);
                    System.out.println("-------------------");
                }
        );
        Point[] lastMiddle = resultMiddle.get(resultMiddle.size() - 1);
        System.out.println("x and f(x) minimized:");
        System.out.print(lastMiddle[lastMiddle.length - 1].x());
        System.out.println(" " + PrecisionRound.round(f2.apply(lastMiddle[lastMiddle.length - 1].x()), AFTER_COMMA));

        // Метод січних
        SecantSolver exSecant = new SecantSolver(interval, precision, f2FD);
        System.out.println("\nSecant algorithm:");
        List<Point[]> resultSecant = exSecant.solve(iterationsNum);
        resultSecant.forEach(
                arr -> {
                    System.out.println("x1: " + arr[0]);
                    System.out.println("x2: " + arr[1]);
                    System.out.println("x*: " + arr[2]);
                    System.out.println("-------------------");
                }
        );
        Point[] lastSecant = resultSecant.get(resultSecant.size() - 1);
        System.out.println("x and f(x) minimized:");
        System.out.print(lastSecant[lastSecant.length - 1].x());
        System.out.println(" " + PrecisionRound.round(f2.apply(lastSecant[lastSecant.length - 1].x()), AFTER_COMMA));
    }
}