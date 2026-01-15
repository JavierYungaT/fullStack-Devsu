package banco.ms_cliente_persona.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseData {

    private String message;
    private String code;
    private String status;
}
