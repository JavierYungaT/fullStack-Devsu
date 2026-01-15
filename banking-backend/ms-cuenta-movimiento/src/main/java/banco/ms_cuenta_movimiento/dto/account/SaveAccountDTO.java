package banco.ms_cuenta_movimiento.dto.account;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveAccountDTO {

    private String numeroCuenta;
    @NotNull(message = "El tipo de cuenta es requerido")
    private String tipoCuenta;
    @Min(value = 1, message = "El saldo inicial debe ser mayor o igual que 1")
    @NotNull(message = "El saldo inicial es requerido")
    private BigDecimal saldoInicial;
    @NotNull(message = "El estado es requerido")
    private Boolean estado;
    @NotNull(message = "El tipo de cuenta es requerido")
    private String identificacion;
    @NotNull(message = "El tipo de cuenta es requerido")
    private String nombre;
}
