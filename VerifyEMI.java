import java.math.BigDecimal;
import java.math.RoundingMode;

public class VerifyEMI {
    public static void main(String[] args) {
        BigDecimal principal = new BigDecimal("1001");
        BigDecimal interestRatePercent = new BigDecimal("3");
        int tenure = 3;

        BigDecimal monthlyRate = interestRatePercent.divide(new BigDecimal("1200"), 15, RoundingMode.HALF_UP);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowerN = onePlusR.pow(tenure);

        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRPowerN);
        BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);

        BigDecimal installmentAmount = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = installmentAmount.multiply(new BigDecimal(tenure));
        BigDecimal accruedInterest = totalAmount.subtract(principal);

        System.out.println("Principal: " + principal);
        System.out.println("Monthly Rate: " + monthlyRate);
        System.out.println("EMI (rounded to 2): " + installmentAmount);
        System.out.println("Total Amount: " + totalAmount);
        System.out.println("Accrued Interest: " + accruedInterest);

        // Without early rounding of EMI
        BigDecimal preciseEMI = numerator.divide(denominator, 10, RoundingMode.HALF_UP);
        BigDecimal preciseTotalAmount = preciseEMI.multiply(new BigDecimal(tenure));
        BigDecimal preciseAccruedInterest = preciseTotalAmount.subtract(principal);

        System.out.println("\nPrecise EMI: " + preciseEMI);
        System.out.println("Precise Total: " + preciseTotalAmount);
        System.out.println("Precise Accrued Interest: " + preciseAccruedInterest);
    }
}
