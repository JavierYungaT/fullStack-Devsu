package banco.ms_cliente_persona.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MovementDetailDTO {

    private String movementDate;
    private String movementType;
    private BigDecimal movementAmount;
    private BigDecimal movementBalance;
}
