package banco.ms_cuenta_movimiento.dto;

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
