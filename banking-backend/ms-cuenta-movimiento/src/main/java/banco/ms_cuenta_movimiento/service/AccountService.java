package banco.ms_cuenta_movimiento.service;



import banco.ms_cuenta_movimiento.dto.ResponseData;
import banco.ms_cuenta_movimiento.dto.account.AccountDTO;
import banco.ms_cuenta_movimiento.dto.account.AccountNumberDTO;
import banco.ms_cuenta_movimiento.dto.account.SaveAccountDTO;
import banco.ms_cuenta_movimiento.exceptions.CustomValidationException;

import java.util.List;
import java.util.Map;
public interface AccountService {
    List<AccountDTO> findAllAccounts();
    AccountDTO getAccountByNumber(AccountNumberDTO accountNumber);
    ResponseData saveAccount(SaveAccountDTO accountDTO) throws CustomValidationException;
    ResponseData updateClient(Integer accountId, Map<String, Object> fields) throws CustomValidationException;
    ResponseData deleteAccount(AccountNumberDTO accountNumberDTO) throws CustomValidationException;
    ResponseData updateAccountAllData(AccountDTO accountDTO) throws CustomValidationException;
}
