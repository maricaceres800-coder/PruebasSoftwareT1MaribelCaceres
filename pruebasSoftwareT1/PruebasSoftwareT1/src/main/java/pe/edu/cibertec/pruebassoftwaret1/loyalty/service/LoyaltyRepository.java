package pe.edu.cibertec.pruebassoftwaret1.loyalty.service;

public interface LoyaltyRepository {
    boolean isVipUser(Long userId);
    void addPoints(Long userId, int points);
}
