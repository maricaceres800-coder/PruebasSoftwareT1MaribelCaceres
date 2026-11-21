package pe.edu.cibertec.pruebassoftwaret1.subcription;

import java.time.LocalDate;

public record Subscription(Long id, String status, LocalDate nextBillingDate) {
}
