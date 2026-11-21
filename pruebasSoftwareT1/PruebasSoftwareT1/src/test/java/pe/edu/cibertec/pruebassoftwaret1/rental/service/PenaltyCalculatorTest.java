package pe.edu.cibertec.pruebassoftwaret1.rental.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PenaltyCalculatorTest {
    private final PenaltyCalculator calculator = new PenaltyCalculator();

    @Test
    @DisplayName("Debe lanzar IllegalStateException cuando los días de retraso son negativos")
    void debeLanzarExcepcionCuandoDiasRetrasoSonNegativos() {
        assertThrows(IllegalStateException.class,
                () -> calculator.calculateLatePenalty(-4));
    }

    @Test
    @DisplayName("Debe retornar cero cuando la devolucion se realiza a tiempo")
    void debeRetornarCeroCuandoDevolucionEsATiempo() {
       int resultado = calculator.calculateLatePenalty(0);
       assertEquals(0, resultado);
    }

    @Test
    @DisplayName("Debe calcular tarifa estándar de $15 por día para retrasos de hasta 7 días")
    void debeCalcularPenalidadEstandarParaRetrasoLeve() {
        // 4 días * $15 = $60
        int resultado = calculator.calculateLatePenalty(4);
        assertEquals(60, resultado);
    }
    @Test
    @DisplayName("Debe aplicar recargo por severidad a partir del octavo día sin superar el tope")
    void debeAplicarRecargoPorSeveridadParaRetrasoModerado() {
        // 10 días: (10 * $15) + (3 * $10) = 150 + 30 = $180
        int resultado = calculator.calculateLatePenalty(10);
        assertEquals(180, resultado);
    }
    @Test
    @DisplayName("Debe limitar la penalización al tope máximo comercial de $300")
    void debeAplicarTopeMaximoCuandoRetrasoEsProlongado() {
        // 20 días: (20 * $15) + (13 * $10) = 300 + 130 = 430 -> Tope: $300
        int resultado = calculator.calculateLatePenalty(20);
        assertEquals(300, resultado);
    }
}
