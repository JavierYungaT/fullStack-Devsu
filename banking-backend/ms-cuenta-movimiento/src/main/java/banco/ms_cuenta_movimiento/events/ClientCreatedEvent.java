package banco.ms_cuenta_movimiento.events;


import lombok.Data;
import lombok.EqualsAndHashCode;
import banco.ms_cuenta_movimiento.dto.ClientEventDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClientCreatedEvent extends Event<ClientEventDTO>{
}
