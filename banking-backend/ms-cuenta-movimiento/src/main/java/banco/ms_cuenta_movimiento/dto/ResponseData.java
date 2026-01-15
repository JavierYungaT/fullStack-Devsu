package banco.ms_cuenta_movimiento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseData {

    private String message;
    private String code;
    private String status;
}
