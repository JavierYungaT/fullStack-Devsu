package banco.ms_cuenta_movimiento.config;


import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import banco.ms_cuenta_movimiento.dto.account.SaveAccountDTO;
import banco.ms_cuenta_movimiento.events.ClientCreatedEvent;
import banco.ms_cuenta_movimiento.events.Event;
import banco.ms_cuenta_movimiento.exceptions.CustomValidationException;
import banco.ms_cuenta_movimiento.service.AccountService;
import banco.ms_cuenta_movimiento.utils.Utils;

@Component
@RequiredArgsConstructor
public class ClientCreatedEventListener {
    private final AccountService accountService;

//    @KafkaListener(topics = "customers",
//            containerFactory = "kafkaListenerContainerFactory",
//            groupId = "customers")
//    public void consumer(Event<?> event) throws CustomValidationException {
//        if (event.getClass().isAssignableFrom(ClientCreatedEvent.class)) {
//
//            ClientCreatedEvent clientCreatedEvent = (ClientCreatedEvent) event;
//            SaveAccountDTO accountDTO = new SaveAccountDTO(
//                    Utils.generateAccountNumber(clientCreatedEvent.getData().getIdentification()),
//                    clientCreatedEvent.getData().getAccountType(),
//                    clientCreatedEvent.getData().getInitialBalance(),
//                    Boolean.TRUE,
//                    clientCreatedEvent.getData().getIdentification(),
//                    clientCreatedEvent.getData().getName()
//            );
//            accountService.saveAccount(accountDTO);
//        }
//    }
}
