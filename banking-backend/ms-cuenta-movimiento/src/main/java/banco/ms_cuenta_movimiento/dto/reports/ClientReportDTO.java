package banco.ms_cuenta_movimiento.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClientReportDTO {



    private String clientDni;
    private String clientName;
    private List<AccountReportDTO> accounts;
}
