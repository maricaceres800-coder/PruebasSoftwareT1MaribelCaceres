package pe.edu.cibertec.pruebassoftwaret1.subcription;

import java.time.LocalDate;

public interface SubscriptionRepository {
    Subscription findById(Long id);
    void updateStatus(Long id, String status);
    void updateBillingDate(Long id, LocalDate newDate);
}
