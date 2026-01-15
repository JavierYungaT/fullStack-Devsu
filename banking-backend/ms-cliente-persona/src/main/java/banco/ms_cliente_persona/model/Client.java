package banco.ms_cliente_persona.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Client extends Person
{
    @Column(name = "cli_state")
    private Boolean clientState;

    @Column(name = "cli_password", nullable = false)
    @NotNull(message = "La contrasenia es requerida")
    private String clientPassword;


}
