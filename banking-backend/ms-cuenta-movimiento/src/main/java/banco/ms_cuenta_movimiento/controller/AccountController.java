package banco.ms_cuenta_movimiento.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import banco.ms_cuenta_movimiento.dto.ResponseData;
import banco.ms_cuenta_movimiento.dto.account.AccountDTO;
import banco.ms_cuenta_movimiento.dto.account.AccountNumberDTO;
import banco.ms_cuenta_movimiento.dto.account.SaveAccountDTO;
import banco.ms_cuenta_movimiento.exceptions.CustomValidationException;
import banco.ms_cuenta_movimiento.service.AccountService;

import java.util.List;
import java.util.Map;

//@CrossOrigin(
//        origins = "http://localhost:4200",
//        allowedHeaders = "*",
//        methods = { RequestMethod.GET, RequestMethod.POST,
//                RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE,
//                RequestMethod.OPTIONS }
//)
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @GetMapping("/findAllAccounts")
    public ResponseEntity<List<AccountDTO>> getAllClients() {
        List<AccountDTO> clients = accountService.findAllAccounts();
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/getAccountByNumber")
    public ResponseEntity<AccountDTO> getAccountByNumber(@RequestBody @Valid AccountNumberDTO accountNumber) {
        AccountDTO account = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/saveAccount")
    public ResponseEntity<ResponseData> saveAccount(@RequestBody @Valid SaveAccountDTO accountDTO) throws CustomValidationException {
        ResponseData account = accountService.saveAccount(accountDTO);
        return ResponseEntity.ok(account);
    }

    @PatchMapping("/updateAccount/{accountId}")
    public ResponseEntity<ResponseData> updateClient(@PathVariable Integer accountId, @RequestBody Map<String, Object> fields) throws CustomValidationException, CustomValidationException {
        ResponseData account = accountService.updateClient(accountId, fields);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/updateAccountAllData")
    public ResponseEntity<ResponseData> updateAccountAllData(@RequestBody @Valid AccountDTO accountDTO) throws CustomValidationException {
        ResponseData account = accountService.updateAccountAllData(accountDTO);
        return ResponseEntity.ok(account);
    }

//    @DeleteMapping("/deleteAccount")
//    public ResponseEntity<ResponseData> deleteAccount(@RequestBody @Valid AccountNumberDTO accountNumberDTO) throws CustomValidationException {
//        ResponseData account = accountService.deleteAccount(accountNumberDTO);
//        return ResponseEntity.ok(account);
//    }

    @DeleteMapping("/deleteAccount/{accountNumber}")
    public ResponseEntity<ResponseData> deleteAccount(
            @PathVariable String accountNumber
    ) throws CustomValidationException {

        ResponseData account = accountService.deleteAccount(
                new AccountNumberDTO(accountNumber)
        );

        return ResponseEntity.ok(account);
    }



}
