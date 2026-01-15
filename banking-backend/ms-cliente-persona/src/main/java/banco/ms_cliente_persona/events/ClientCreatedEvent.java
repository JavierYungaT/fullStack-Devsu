package banco.ms_cliente_persona.events;



import lombok.Data;
import lombok.EqualsAndHashCode;
import banco.ms_cliente_persona.dto.client.ClientAccountEventDTO;


@Data
@EqualsAndHashCode(callSuper = true)
public class ClientCreatedEvent extends Event<ClientAccountEventDTO> {
}
