package pe.edu.cibertec.pruebassoftwaret1.loyalty.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

@DisplayName("Pruebas del servicio de puntos y fidelización")
public class LoyaltyServiceImplTest {

    @Mock
    private LoyaltyRepository loyaltyRepository;

    @InjectMocks
    private LoyaltyServiceImpl loyaltyService;

    @Test
    @DisplayName("Debe lanzar excepción y no registrar puntos cuando el monto es inválido")
    void shouldThrowExceptionAndNeverRegisterPointsWhenAmountIsInvalid() {

        // Arrange: preparar los datos
        Long userId = 1L;
        BigDecimal amount = BigDecimal.ZERO;

        // Act + Assert: comprobar la excepción
        assertThrows(IllegalArgumentException.class,
                () -> loyaltyService.calculateAndAssignPoints(
                        userId, amount));

        // Assert: verificar que no se registren puntos
        verify(loyaltyRepository, never())
                .addPoints(anyLong(), anyInt());

        // Verificar que el repositorio no tenga interacciones
        verifyNoInteractions(loyaltyRepository);
    }


    @Test
    @DisplayName("Debe asignar el doble de puntos y actualizar el repositorio cuando el cliente es VIP")
    void shouldAssignDoublePointsAndVerifyRepositoryForVipCustomer() {

        // Arrange: preparar los datos
        Long userId = 2L;
        BigDecimal amount = new BigDecimal("100.90");

        when(loyaltyRepository.isVipUser(userId))
                .thenReturn(true);

        // Act: ejecutar el método
        LoyaltyResponse response =
                loyaltyService.calculateAndAssignPoints(userId, amount);

        // Assert: comprobar los resultados
        assertEquals(200, response.pointsEarned());
        assertTrue(response.isVip());
        assertEquals(userId, response.userId());

        // Verificar las interacciones con el repositorio
        verify(loyaltyRepository).isVipUser(userId);

        verify(loyaltyRepository).addPoints(userId, 200);
    }
}