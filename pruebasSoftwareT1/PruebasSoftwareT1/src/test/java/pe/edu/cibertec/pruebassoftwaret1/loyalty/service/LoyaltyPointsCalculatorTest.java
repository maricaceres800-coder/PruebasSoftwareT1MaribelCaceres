package pe.edu.cibertec.pruebassoftwaret1.loyalty.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoyaltyPointsCalculatorTest {

    private final LoyaltyPointsCalculator calculator = new LoyaltyPointsCalculator();

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el monto de compra es nulo")
    void debeLanzarExcepcionCuandoMontoCompraEsNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateAccumulatedPoints(null, "VIP"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el monto de compra es cero o negativo")
    void debeLanzarExcepcionCuandoMontoCompraEsCeroONegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateAccumulatedPoints(BigDecimal.ZERO, "REGULAR"));

        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateAccumulatedPoints(new BigDecimal("-35.50"), "VIP"));
    }

    @Test
    @DisplayName("Debe asignar 1 punto por cada unidad entera para un cliente regular")
    void debeCalcularPuntosSimplesParaClienteRegular() {
        // 135.80 -> parte entera = 135 puntos

        int resultado = calculator.calculateAccumulatedPoints(
                new BigDecimal("135.80"), "REGULAR");

        assertEquals(135, resultado);
    }

    @Test
    @DisplayName("Debe duplicar los puntos y redondear hacia abajo para un cliente con estatus VIP")
    void debeCalcularPuntosDoblesParaClienteVip() {

        // 135.80 * 2 = 271.60 -> redondeado hacia abajo = 271 puntos
        int resultado = calculator.calculateAccumulatedPoints(
                new BigDecimal("135.80"), "VIP");

        assertEquals(271, resultado);
    }
}
