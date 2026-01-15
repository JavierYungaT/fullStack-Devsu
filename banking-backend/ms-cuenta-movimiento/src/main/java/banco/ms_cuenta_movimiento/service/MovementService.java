package banco.ms_cuenta_movimiento.service;



import banco.ms_cuenta_movimiento.dto.ResponseData;
import banco.ms_cuenta_movimiento.dto.UserDniDTO;
import banco.ms_cuenta_movimiento.dto.account.AccountNumberDTO;
import banco.ms_cuenta_movimiento.dto.movement.MovementDTO;
import banco.ms_cuenta_movimiento.dto.movement.MovementId;
import banco.ms_cuenta_movimiento.dto.movement.MovementRequestDTO;
import banco.ms_cuenta_movimiento.dto.reports.ClientReportDTO;
import banco.ms_cuenta_movimiento.dto.reports.ReportRequestDTO;
import banco.ms_cuenta_movimiento.exceptions.CustomValidationException;

import java.util.List;
import java.util.Map;

public interface MovementService {

    List<MovementDTO> findMovementByAccountNumber(AccountNumberDTO accountNumberDTO);
    List<MovementDTO> findAll() throws CustomValidationException;
    List<MovementDTO> findMovementByDni(UserDniDTO userDniDTO) throws CustomValidationException;
    ResponseData handleMovement(MovementRequestDTO movementDTO) throws CustomValidationException;
    ClientReportDTO generateReport(ReportRequestDTO reportRequestDTO) throws CustomValidationException;
    ResponseData updateMovement(Integer userId, Map<String, Object> fields) throws CustomValidationException;
    ResponseData deleteMovement(MovementId movementId) throws CustomValidationException;
}
