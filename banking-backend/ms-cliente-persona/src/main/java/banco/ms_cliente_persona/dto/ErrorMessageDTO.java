package banco.ms_cliente_persona.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageDTO {

    private HttpStatus status;
    private String code;
    private String message;
}
