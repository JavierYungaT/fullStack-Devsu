package banco.ms_cliente_persona.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.Date;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientCreatedEvent.class, name = "ClientCreatedEvent"),
})
public abstract class Event <T> {

    private String id;
    private Date date;
    private EventType type;
    private T data;
}
