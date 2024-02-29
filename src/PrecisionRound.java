public class PrecisionRound {
    public static Double round(Double num, int precision) {
        double nominator = num * Math.pow(10, precision);
        if (Math.abs(nominator - (int)nominator) == 0.5) {
            if (nominator < 0)
                return ((int)nominator - 1) / Math.pow(10, precision);
            else
                return ((int)nominator + 1) / Math.pow(10, precision);
        }

        return Math.round(nominator) / Math.pow(10, precision);
    }
}
