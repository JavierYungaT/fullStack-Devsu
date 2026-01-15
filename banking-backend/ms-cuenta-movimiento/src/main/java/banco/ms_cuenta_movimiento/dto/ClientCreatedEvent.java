package banco.ms_cuenta_movimiento.dto;

import lombok.Data;

@Data
public class ClientCreatedEvent {

    private Integer clientId;
    private ClientEventDTO client;
}
