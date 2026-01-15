package banco.ms_cuenta_movimiento.dto.movement;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementRequestDTO {

    @NotNull(message = "El numero de cuenta es requerido")
    private String accountNumber;
    @Min(value = 1,message = "El valor de la transaccion debe ser mayor a 0")
    @NotNull(message = "El valor del movimiento es requerido")
    private BigDecimal value;
    @NotNull(message = "El tipo de movimiento es requerido")
    private String movementType;
}
