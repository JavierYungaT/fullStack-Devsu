package banco.ms_cliente_persona.dto.client;



import banco.ms_cliente_persona.enums.MovementsTypeEnum;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ClientAccountEventDTO {

    private String identification;
    private String name;
    private MovementsTypeEnum accountType;
    private BigDecimal initialBalance;
}
