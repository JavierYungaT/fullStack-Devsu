package banco.ms_cliente_persona.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorValidationMessageDTO {

    private HttpStatus status;
    private String code;
    private Map<String, Object> message;
}
