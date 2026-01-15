package banco.ms_cuenta_movimiento.dto.reports;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReportRequestDTO {

    private String clientDni;
//    private LocalDateTime initialDate;
//    private LocalDateTime finalDate;

    private LocalDate initialDate;
    private LocalDate finalDate;
}
