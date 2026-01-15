package banco.ms_cuenta_movimiento.controller;

import banco.ms_cuenta_movimiento.service.impl.PdfServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import banco.ms_cuenta_movimiento.dto.ResponseData;
import banco.ms_cuenta_movimiento.dto.UserDniDTO;
import banco.ms_cuenta_movimiento.dto.account.AccountNumberDTO;
import banco.ms_cuenta_movimiento.dto.movement.MovementDTO;
import banco.ms_cuenta_movimiento.dto.movement.MovementId;
import banco.ms_cuenta_movimiento.dto.movement.MovementRequestDTO;
import banco.ms_cuenta_movimiento.dto.reports.ClientReportDTO;
import banco.ms_cuenta_movimiento.dto.reports.ReportRequestDTO;
import banco.ms_cuenta_movimiento.exceptions.CustomValidationException;
import banco.ms_cuenta_movimiento.service.MovementService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movement")
@RequiredArgsConstructor
public class MovementController {

    private final MovementService movementService;
    private final PdfServiceImpl pdfService;

    @PostMapping("/getMovementByAccount")
    public ResponseEntity<List<MovementDTO>> getAccountByNumber(@RequestBody @Valid AccountNumberDTO accountNumber) {
        List<MovementDTO> account = movementService.findMovementByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/getAllMovements")
    public ResponseEntity<List<MovementDTO>> getAllMovements() throws CustomValidationException {
        List<MovementDTO> movements = movementService.findAll();
        if (movements.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movements);
    }

    @PostMapping("/getMovementByDni")
    public ResponseEntity<List<MovementDTO>> getMovementByDni(@RequestBody @Valid UserDniDTO userDniDTO) throws CustomValidationException {
        List<MovementDTO> movements = movementService.findMovementByDni(userDniDTO);
        return ResponseEntity.ok(movements);
    }

    @PostMapping("/makeMovement")
    public ResponseEntity<ResponseData> makeMovement(@RequestBody @Valid MovementRequestDTO movementDTO) throws CustomValidationException {
        ResponseData response = movementService.handleMovement(movementDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generateReport")
    public ResponseEntity<ClientReportDTO> generateReport(@RequestBody @Valid ReportRequestDTO reportRequestDTO) throws CustomValidationException {
        ClientReportDTO report = movementService.generateReport(reportRequestDTO);
        return ResponseEntity.ok(report);
    }

    @PatchMapping("/updateMovement/{movementId}")
    public ResponseEntity<ResponseData> updateMovement(@PathVariable Integer movementId, @RequestBody Map<String, Object> fields) throws CustomValidationException {
        ResponseData response = movementService.updateMovement(movementId, fields);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteMovement")
    public ResponseEntity<ResponseData> deleteMovement(@RequestBody MovementId movementId) throws CustomValidationException {
        ResponseData response = movementService.deleteMovement(movementId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generateReportPdf")
    public ResponseEntity<byte[]> generateReportPdf(
            @RequestBody @Valid ReportRequestDTO reportRequestDTO
    ) throws CustomValidationException {

        ClientReportDTO report = movementService.generateReport(reportRequestDTO);

        byte[] pdfBytes = pdfService.generateReportPdf(
                report,
                reportRequestDTO.getInitialDate().toString(),
                reportRequestDTO.getFinalDate().toString()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
