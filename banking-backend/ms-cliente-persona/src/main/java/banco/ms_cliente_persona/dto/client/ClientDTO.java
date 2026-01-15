package banco.ms_cliente_persona.dto.client;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import banco.ms_cliente_persona.dto.validation.UpdateGroup;
import banco.ms_cliente_persona.enums.MovementsTypeEnum;

import java.math.BigDecimal;

@Data
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
public class ClientDTO {

    private Integer personId;
    @NotBlank(message = "La cedula de la persona no puede ser nula o vacia")
    private String personDni;
    @NotBlank(message = "El nombre de la persona no puede ser nulo o vacio")
    private String personName;
    @NotBlank(message = "El genero de la persona no puede ser nulo o vacio")
    private String personGenre;
    @NotNull(message = "La edad de la persona no puede ser nula")
    private Integer personAge;
    @NotBlank(message = "La direccion de la persona no puede ser nula o vacia")
    private String personAddress;
    @NotBlank(message = "El numero de telefono de la persona no puede ser nulo o vacio")
    private String personPhoneNumber;
    @NotNull(message = "El estado del cliente no puede ser nulo")
    private Boolean clientState;
    @NotBlank(message = "La contraseña del cliente no puede ser nula o vacia")
    private String clientPassword;

    @NotNull(message = "El tipo de cuenta es requerido" ,groups = UpdateGroup.class)
    private MovementsTypeEnum tipoCuenta;

    @NotNull(message = "El saldo inicial de la cuenta es requerido" ,groups = UpdateGroup.class)
    @Min(value = 1,message = "El saldo inicial debe ser mayor o igual que 1")
    private BigDecimal saldoInicial;
}
