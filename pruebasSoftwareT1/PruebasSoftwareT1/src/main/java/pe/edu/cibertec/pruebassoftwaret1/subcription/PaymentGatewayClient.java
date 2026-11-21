package pe.edu.cibertec.pruebassoftwaret1.subcription;

public interface PaymentGatewayClient {
    boolean isCardValid(Long id);
    void chargeMonthlyFee(Long id);
}
