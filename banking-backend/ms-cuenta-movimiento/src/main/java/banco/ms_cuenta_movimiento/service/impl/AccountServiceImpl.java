package banco.ms_cuenta_movimiento.service.impl;

import banco.ms_cuenta_movimiento.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
//import org.apache.kafka.common.errors.ResourceNotFoundException;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import banco.ms_cuenta_movimiento.dto.ResponseData;
import banco.ms_cuenta_movimiento.dto.account.AccountDTO;
import banco.ms_cuenta_movimiento.dto.account.AccountNumberDTO;
import banco.ms_cuenta_movimiento.dto.account.SaveAccountDTO;
import banco.ms_cuenta_movimiento.enums.ErrorMessagesEnum;
import banco.ms_cuenta_movimiento.enums.SuccessCodesEnum;
import banco.ms_cuenta_movimiento.enums.SuccessMessagesEnum;
import banco.ms_cuenta_movimiento.exceptions.CustomValidationException;
import banco.ms_cuenta_movimiento.model.Account;
import banco.ms_cuenta_movimiento.repository.AccountRepository;
import banco.ms_cuenta_movimiento.service.AccountService;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    //private final KafkaTemplate<String, Long> kafkaTemplate;
    private final AccountRepository accountRepository;

//
//    public List<AccountDTO> findAllAccounts() {
//        List<Account> accounts = accountRepository.findAll();
//        if (accounts != null && !accounts.isEmpty()) {
//            return accounts.stream()
//                    .map(account -> new AccountDTO(
//                            account.getAccountNumber(),
//                            account.getAccountType(),
//                            account.getAccountBalance(),
//                            account.getClientIdentification(),
//
//                            account.getAccountState()))
//                    .collect(Collectors.toList());
//        } else {
//           throw new ResourceNotFoundException(ErrorMessagesEnum.ACCOUNTS_NOT_FOUND.getMessage());
//        }
//    }


    public List<AccountDTO> findAllAccounts() {
        List<Account> accounts = accountRepository.findAll();

        if (accounts != null && !accounts.isEmpty()) {
            return accounts.stream()
                    .map(account -> new AccountDTO(
                            account.getAccountId(),
                            account.getAccountNumber(),        // numeroCuenta
                            account.getAccountType(),          // tipoCuenta
                            account.getAccountBalance(),       // saldoInicial
                            account.getClientIdentification(), // clienteCedula
                            account.getAccountState(),         // estado
                            account.getClientName()            // nombreCliente
                    ))
                    .collect(Collectors.toList());
        } else {
            throw new ResourceNotFoundException(
                    ErrorMessagesEnum.ACCOUNTS_NOT_FOUND.getMessage()
            );
        }
    }


    public AccountDTO getAccountByNumber(AccountNumberDTO accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessagesEnum.ACCOUNT_NOT_FOUND.getMessage()));

        return new AccountDTO(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getAccountBalance(),
                account.getClientIdentification(),
                account.getAccountState(),
                account.getClientName()
        );
    }

    public ResponseData saveAccount(SaveAccountDTO accountDTO) throws CustomValidationException {


        Optional<Account> accountExist = accountRepository.findByAccountNumber(accountDTO.getNumeroCuenta());
        if (accountExist.isPresent()) {
            throw new CustomValidationException(ErrorMessagesEnum.ACCOUNT_ALREADY_EXISTS.getMessage());
        }

        Account account = new Account();
        account.setAccountNumber(accountDTO.getNumeroCuenta());
        account.setAccountType(accountDTO.getTipoCuenta());
        account.setAccountBalance(accountDTO.getSaldoInicial());
        account.setAccountState(accountDTO.getEstado());
        account.setClientIdentification(accountDTO.getIdentificacion());
        account.setClientName(accountDTO.getNombre());
        accountRepository.save(account);
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_CREATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

    public ResponseData updateClient(Integer accountId, Map<String, Object> fields) throws CustomValidationException {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new CustomValidationException(ErrorMessagesEnum.CLIENT_NOT_FOUND.getMessage());
        }
        if (fields.containsKey("accountNumber")) {
            throw new CustomValidationException(ErrorMessagesEnum.FIELD_MODIFIED_NOT_ALLOWED.getMessage());
        }
//        fields.forEach((key, value) -> {
//            Field field = ReflectionUtils.findField(Account.class, key);
//            if (field != null) {
//                field.setAccessible(true);
//                ReflectionUtils.setField(field, account.get(), value);
//            } else {
//                throw new ResourceNotFoundException(ErrorMessagesEnum.FIELD_NOT_FOUND.getMessage() + " Field: " + key);
//            }
//        });
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Account.class, key);
            if (field != null) {
                field.setAccessible(true);

                // Si es BigDecimal y viene un número, convertir
                if (field.getType().equals(BigDecimal.class) && value instanceof Number) {
                    ReflectionUtils.setField(field, account.get(), BigDecimal.valueOf(((Number) value).doubleValue()));
                } else {
                    ReflectionUtils.setField(field, account.get(), value);
                }

            } else {
                throw new ResourceNotFoundException(
                        ErrorMessagesEnum.FIELD_NOT_FOUND.getMessage() + " Field: " + key
                );
            }
        });

        accountRepository.save(account.get());
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_UPDATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

    public ResponseData updateAccountAllData(AccountDTO accountDTO) throws CustomValidationException {

        Optional<Account> accountExist = accountRepository.findByAccountNumber(accountDTO.getNumeroCuenta());
        if (accountExist.isPresent()) {
            throw new CustomValidationException(ErrorMessagesEnum.ACCOUNT_ALREADY_EXISTS.getMessage());
        }

        Account existingAccount = new Account();
        existingAccount.setAccountNumber(accountDTO.getNumeroCuenta());
        existingAccount.setAccountType(accountDTO.getTipoCuenta());
        existingAccount.setAccountBalance(accountDTO.getSaldoInicial());
        existingAccount.setAccountState(accountDTO.getEstado());
        accountRepository.save(existingAccount);

        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_UPDATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

//
//    public ResponseData updateAccountAllData(AccountDTO accountDTO) throws CustomValidationException {
//
//        // BUSCAR la cuenta (debe existir)
//        Account existingAccount = accountRepository.findByAccountNumber(accountDTO.getNumeroCuenta())
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        ErrorMessagesEnum.ACCOUNT_NOT_FOUND.getMessage()
//                ));
//
//        // ACTUALIZAR los campos editables
//        existingAccount.setAccountType(accountDTO.getTipoCuenta());
//        existingAccount.setAccountBalance(accountDTO.getSaldoInicial());
//        existingAccount.setAccountState(accountDTO.getEstado());
//
//        // Solo actualizar el nombre si viene en el DTO
//        if (accountDTO.getNombreCliente() != null) {
//            existingAccount.setClientName(accountDTO.getNombreCliente());
//        }
//
//        // NO actualizar: accountNumber ni clientIdentification (son inmutables)
//
//        accountRepository.save(existingAccount);
//
//        return new ResponseData(
//                SuccessMessagesEnum.SUCCESSFULLY_UPDATED.getMessage(),
//                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
//                SuccessMessagesEnum.STATUS_OK.getMessage()
//        );
//    }

    public ResponseData deleteAccount(AccountNumberDTO accountNumberDTO) throws CustomValidationException {
        Optional<Account> accountExist = accountRepository.findByAccountNumber(accountNumberDTO.getAccountNumber());
        if (accountExist.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessagesEnum.ACCOUNT_NOT_FOUND.getMessage());
        }

        accountRepository.delete(accountExist.get());
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_DELETED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }
}
