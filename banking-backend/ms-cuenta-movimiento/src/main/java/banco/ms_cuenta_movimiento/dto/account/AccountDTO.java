package banco.ms_cuenta_movimiento.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
public class AccountDTO {

    private Long  accountId;
    @NotNull(message = "El numero de cuenta es requerido")
    private String numeroCuenta;
    @NotNull(message = "El tipo de cuenta es requerido")
    private String tipoCuenta;
    @Min(value = 1,message = "El saldo inicial debe ser mayor o igual que 1")
    @NotNull(message = "El saldo inicial es requerido")
    private BigDecimal saldoInicial;
    @NotNull(message = "La identificacion del client es requerida")
    private String clienteCedula;
    @NotNull(message = "El estado es requerido")
    private Boolean estado;
    private String nombreCliente;
}
