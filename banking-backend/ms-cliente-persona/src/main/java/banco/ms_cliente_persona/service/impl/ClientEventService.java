package banco.ms_cliente_persona.service.impl;


import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import banco.ms_cliente_persona.dto.client.ClientAccountEventDTO;
import banco.ms_cliente_persona.events.ClientCreatedEvent;
import banco.ms_cliente_persona.events.Event;
import banco.ms_cliente_persona.events.EventType;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientEventService {

    //private final KafkaTemplate<String, Event<?>> producer;

    public void publish(ClientAccountEventDTO clientAccountEventDTO) {
        ClientCreatedEvent createdEvent = new ClientCreatedEvent();
        createdEvent.setData(clientAccountEventDTO);
        createdEvent.setId(UUID.randomUUID().toString());
        createdEvent.setType(EventType.CREATED);
        createdEvent.setDate(new Date());
        //producer.send("customers", createdEvent);
    }
}
