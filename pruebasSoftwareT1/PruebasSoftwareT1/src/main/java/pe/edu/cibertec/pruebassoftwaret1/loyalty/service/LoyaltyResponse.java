package pe.edu.cibertec.pruebassoftwaret1.loyalty.service;

public record LoyaltyResponse(Long userId, int pointsEarned, boolean isVip) {
}
