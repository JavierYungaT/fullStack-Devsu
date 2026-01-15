package banco.ms_cuenta_movimiento.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDniDTO {

    @NotBlank(message = "La cedula del usuario es requerida")
    private String userDni;
}
