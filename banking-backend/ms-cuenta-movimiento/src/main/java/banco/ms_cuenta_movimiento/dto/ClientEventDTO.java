package banco.ms_cuenta_movimiento.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClientEventDTO {

    private String identification;
    private String name;
    private String accountType;
    private BigDecimal initialBalance;
}
