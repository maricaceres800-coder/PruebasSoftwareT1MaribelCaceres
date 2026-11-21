package pe.edu.cibertec.pruebassoftwaret1.subscription;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.pruebassoftwaret1.subcription.ExpiredCardException;
import pe.edu.cibertec.pruebassoftwaret1.subcription.PaymentGatewayClient;
import pe.edu.cibertec.pruebassoftwaret1.subcription.Subscription;
import pe.edu.cibertec.pruebassoftwaret1.subcription.SubscriptionRepository;
import pe.edu.cibertec.pruebassoftwaret1.subcription.SubscriptionServiceImpl;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private PaymentGatewayClient paymentGatewayClient;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Test
    @DisplayName("Debe suspender la suscripcion y debe lanzar ExpiredCardException cuando la tarjeta venció")
    void debeSuspenderCuentaYLanzarExcepcionCuandoTarjetaEstaVencida() {
        Long suscripcionId = 50L;
        Subscription suscripcionActual = new Subscription(suscripcionId, "ACTIVE", LocalDate.of(2026, 11, 15));

        when(subscriptionRepository.findById(suscripcionId)).thenReturn(suscripcionActual);
        when(paymentGatewayClient.isCardValid(suscripcionId)).thenReturn(false);

        assertThrows(ExpiredCardException.class,
                () -> subscriptionService.renewSubscription(suscripcionId));

        // Verifica suspensión y cero interacciones con el cobro
        verify(subscriptionRepository).updateStatus(suscripcionId, "SUSPENDED");
        verify(paymentGatewayClient, never()).chargeMonthlyFee(anyLong());
        verify(subscriptionRepository, never()).updateBillingDate(anyLong(), any());
    }

    @Test
    @DisplayName("Debe procesar el cobro y extender la fecha de facturación en un mes cuando la tarjeta es válida")
    void debeCobrarYExtenderFechaUnMesCuandoTarjetaEsVigente() {
        Long suscripcionId = 80L;
        LocalDate fechaActual = LocalDate.of(2026, 11, 20);
        LocalDate fechaEsperada = LocalDate.of(2026, 12, 20);
        Subscription suscripcionActual = new Subscription(suscripcionId, "ACTIVE", fechaActual);

        when(subscriptionRepository.findById(suscripcionId)).thenReturn(suscripcionActual);
        when(paymentGatewayClient.isCardValid(suscripcionId)).thenReturn(true);

        Subscription resultado = subscriptionService.renewSubscription(suscripcionId);

        assertEquals("ACTIVE", resultado.status());
        assertEquals(fechaEsperada, resultado.nextBillingDate());
        verify(paymentGatewayClient).chargeMonthlyFee(suscripcionId);
        verify(subscriptionRepository).updateBillingDate(suscripcionId, fechaEsperada);
    }
}
