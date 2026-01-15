package banco.ms_cuenta_movimiento.service.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import banco.ms_cuenta_movimiento.dto.ResponseData;
import banco.ms_cuenta_movimiento.dto.UserDniDTO;
import banco.ms_cuenta_movimiento.dto.account.AccountNumberDTO;
import banco.ms_cuenta_movimiento.dto.movement.MovementDTO;
import banco.ms_cuenta_movimiento.dto.movement.MovementId;
import banco.ms_cuenta_movimiento.dto.movement.MovementRequestDTO;
import banco.ms_cuenta_movimiento.dto.reports.AccountReportDTO;
import banco.ms_cuenta_movimiento.dto.reports.ClientReportDTO;
import banco.ms_cuenta_movimiento.dto.reports.MovementDetailDTO;
import banco.ms_cuenta_movimiento.dto.reports.ReportRequestDTO;
import banco.ms_cuenta_movimiento.enums.ErrorMessagesEnum;
import banco.ms_cuenta_movimiento.enums.MovementsTypeEnum;
import banco.ms_cuenta_movimiento.enums.SuccessCodesEnum;
import banco.ms_cuenta_movimiento.enums.SuccessMessagesEnum;
import banco.ms_cuenta_movimiento.exceptions.CustomValidationException;
import banco.ms_cuenta_movimiento.exceptions.ResourceNotFoundException;
import banco.ms_cuenta_movimiento.model.Account;
import banco.ms_cuenta_movimiento.model.Movement;
import banco.ms_cuenta_movimiento.repository.AccountRepository;
import banco.ms_cuenta_movimiento.repository.MovementRepository;
import banco.ms_cuenta_movimiento.service.MovementService;
import banco.ms_cuenta_movimiento.utils.Utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovementServiceImpl implements MovementService {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Value("${banco.limite-diario-retiro}")
    private BigDecimal limiteDiarioRetiro;

    public MovementServiceImpl(AccountRepository accountRepository, MovementRepository movementRepository) {
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
    }

    public List<MovementDTO> findMovementByAccountNumber(AccountNumberDTO accountNumberDTO) {

        Optional<Account> account = accountRepository.findByAccountNumber(accountNumberDTO.getAccountNumber());

        if (account.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessagesEnum.ACCOUNT_NOT_FOUND.getMessage());
        }
        List<Movement> movements = movementRepository.findByAccountAccountNumber(accountNumberDTO.getAccountNumber());
        if (movements.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessagesEnum.MOVEMENTS_NOT_FOUND.getMessage());
        }
        return getMovementDTOS(movements);
    }

    public List<MovementDTO> findAll() throws CustomValidationException {
        List<Movement> movements = movementRepository.findAll();
        return getMovementDTOS(movements);
    }


    public List<MovementDTO> findMovementByDni(UserDniDTO userDniDTO) throws CustomValidationException {
        List<Movement> movements = movementRepository.findByAccountClientPersonDni(userDniDTO.getUserDni());
        if (movements.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessagesEnum.MOVEMENTS_NOT_FOUND.getMessage());
        }
        return getMovementDTOS(movements);
    }

//    @Transactional
//    public ResponseData handleMovement(MovementRequestDTO movementDTO) throws CustomValidationException {
//
//        Account account = accountRepository.findByAccountNumber(movementDTO.getAccountNumber())
//                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessagesEnum.ACCOUNT_NOT_FOUND.getMessage()));
//
//
//        BigDecimal currentBalance = account.getAccountBalance();
//        BigDecimal updatedBalance;
//
//        if (movementDTO.getMovementType().equalsIgnoreCase(MovementsTypeEnum.DEPOSIT.getMessage())) {
//            updatedBalance = currentBalance.add(movementDTO.getValue());
//        } else if (movementDTO.getMovementType().equalsIgnoreCase(MovementsTypeEnum.WITHDRAWAL.getMessage())) {
//            BigDecimal balanceAfterWithdrawal = currentBalance.subtract(movementDTO.getValue());
//            if (balanceAfterWithdrawal.compareTo(BigDecimal.ZERO) < 0) {
//                throw new CustomValidationException(ErrorMessagesEnum.INSUFFICIENT_BALANCE.getMessage());
//            }
//            updatedBalance = balanceAfterWithdrawal;
//        } else {
//            throw new CustomValidationException(ErrorMessagesEnum.INVALID_MOVEMENT_TYPE.getMessage());
//        }
//
//        Movement movement = new Movement(
//                null,
//                LocalDateTime.now(),
//                movementDTO.getMovementType(),
//                movementDTO.getValue(),
//                updatedBalance,
//                true,
//                account
//        );
//        movementRepository.save(movement);
//        account.setAccountBalance(updatedBalance);
//        accountRepository.save(account);
//        return new ResponseData(
//                SuccessMessagesEnum.TRANSACTION_SUCCESSFUL.getMessage(),
//                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
//                SuccessMessagesEnum.STATUS_OK.getMessage()
//        );
//    }

    @Transactional
    public ResponseData handleMovement(MovementRequestDTO movementDTO) throws CustomValidationException {

        Account account = accountRepository.findByAccountNumber(movementDTO.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessagesEnum.ACCOUNT_NOT_FOUND.getMessage()));

        BigDecimal currentBalance = account.getAccountBalance();
        BigDecimal updatedBalance;

        if (movementDTO.getMovementType().equalsIgnoreCase(MovementsTypeEnum.DEPOSIT.getMessage())) {
            updatedBalance = currentBalance.add(movementDTO.getValue());

        } else if (movementDTO.getMovementType().equalsIgnoreCase(MovementsTypeEnum.WITHDRAWAL.getMessage())) {

            // VALIDACIÓN 1: Verificar saldo disponible
            BigDecimal balanceAfterWithdrawal = currentBalance.subtract(movementDTO.getValue());
            if (balanceAfterWithdrawal.compareTo(BigDecimal.ZERO) < 0) {
                throw new CustomValidationException(ErrorMessagesEnum.INSUFFICIENT_BALANCE.getMessage());
            }

            // VALIDACIÓN 2: Verificar límite diario de retiros
            BigDecimal retirosDelDia = calcularRetirosDelDia(account);
            BigDecimal nuevoTotalRetiros = retirosDelDia.add(movementDTO.getValue());

            if (nuevoTotalRetiros.compareTo(limiteDiarioRetiro) > 0) {
                throw new CustomValidationException("Cupo diario Excedido");
            }

            updatedBalance = balanceAfterWithdrawal;

        } else {
            throw new CustomValidationException(ErrorMessagesEnum.INVALID_MOVEMENT_TYPE.getMessage());
        }

        Movement movement = new Movement(
                null,
                LocalDateTime.now(),
                movementDTO.getMovementType(),
                movementDTO.getValue(),
                updatedBalance,
                true,
                account
        );

        movementRepository.save(movement);
        account.setAccountBalance(updatedBalance);
        accountRepository.save(account);

        return new ResponseData(
                SuccessMessagesEnum.TRANSACTION_SUCCESSFUL.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

    private List<MovementDTO> getMovementDTOS(List<Movement> movements) {
        Map<Account, List<Movement>> movementsByAccount = movements.stream()
                .collect(Collectors.groupingBy(Movement::getAccount));

        return movements.stream()
                .map(movement -> {
                    MovementDTO dto = modelMapper.map(movement, MovementDTO.class);
                    dto.setMovementId(movement.getMovementId());
                    dto.setPersonDni(movement.getAccount().getClientIdentification());
                    dto.setPersonName(movement.getAccount().getClientName());
                    dto.setAccountType(movement.getAccount().getAccountType());
                    dto.setMovementDate(Utils.convertLocalDateTimeToString(movement.getMovementDate()));
                    if (movement.getMovementType().equalsIgnoreCase(MovementsTypeEnum.WITHDRAWAL.getMessage())) {
                        dto.setMovementAmount(movement.getMovementAmount().negate());
                    } else {
                        dto.setMovementAmount(movement.getMovementAmount());
                    }
                    BigDecimal initialBalance = calculateInitialBalance(movementsByAccount.get(movement.getAccount()), movement);
                    BigDecimal availableBalance = calculateAvailableBalance(movementsByAccount.get(movement.getAccount()), movement);
                    dto.setInitialBalance(initialBalance);
                    dto.setAvailableBalance(availableBalance);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    private BigDecimal calculateInitialBalance(List<Movement> accountMovements, Movement currentMovement) {
        BigDecimal initialBalance = BigDecimal.ZERO;

        for (Movement movement : accountMovements) {
            if (movement.equals(currentMovement)) {
                break;
            }
            if (movement.getMovementType().equalsIgnoreCase(MovementsTypeEnum.DEPOSIT.getMessage())) {
                initialBalance = initialBalance.add(movement.getMovementAmount());
            } else if (movement.getMovementType().equalsIgnoreCase(MovementsTypeEnum.WITHDRAWAL.getMessage())) {
                initialBalance = initialBalance.subtract(movement.getMovementAmount());
            }
        }
        return initialBalance;
    }

    private BigDecimal calculateAvailableBalance(List<Movement> accountMovements, Movement currentMovement) {
        BigDecimal availableBalance = BigDecimal.ZERO;

        for (Movement movement : accountMovements) {
            if (movement.getMovementType().equalsIgnoreCase(MovementsTypeEnum.DEPOSIT.getMessage())) {
                availableBalance = availableBalance.add(movement.getMovementAmount());
            } else if (movement.getMovementType().equalsIgnoreCase(MovementsTypeEnum.WITHDRAWAL.getMessage())) {
                availableBalance = availableBalance.subtract(movement.getMovementAmount());
            }
            if (movement.equals(currentMovement)) {
                break;
            }
        }
        return availableBalance;
    }

    public ResponseData updateMovement(Integer userId, Map<String, Object> fields) throws CustomValidationException {

        Optional<Movement> moves = movementRepository.findById(userId);
        if (moves.isEmpty()) {
            throw new CustomValidationException(ErrorMessagesEnum.CLIENT_NOT_FOUND.getMessage());
        }
        fields.forEach((key, value) -> {
            if (key.equals("movementDate")) {
                try {
                    throw new CustomValidationException(ErrorMessagesEnum.FIELD_MODIFIED_NOT_ALLOWED.getMessage() + " Field: " + key);
                } catch (CustomValidationException e) {
                    throw new RuntimeException(e);
                }
            }
            Field field = ReflectionUtils.findField(Movement.class, key);
            if (field != null) {
                if (field.getType().equals(BigDecimal.class)) {
                    value = new BigDecimal(value.toString());
                }
                field.setAccessible(true);
                ReflectionUtils.setField(field, moves.get(), value);
            } else {
                throw new ResourceNotFoundException(ErrorMessagesEnum.FIELD_NOT_FOUND.getMessage() + " Field: " + key);
            }
        });
        movementRepository.save(moves.get());
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_UPDATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );

    }


    public ClientReportDTO generateReport(ReportRequestDTO reportRequestDTO) {
        List<Account> accounts = accountRepository.findByClientIdentification(reportRequestDTO.getClientDni());

        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessagesEnum.ACCOUNT_NOT_FOUND.getMessage());
        }

        List<AccountReportDTO> accountReports = accounts.stream().map(account -> {
//            List<Movement> movements = movementRepository.findByAccountAndMovementDateBetween(
//                    account,
//                    reportRequestDTO.getInitialDate(),
//                    reportRequestDTO.getFinalDate());

            List<Movement> movements = movementRepository.findByAccountAndMovementDateBetween(
                    account,
                    reportRequestDTO.getInitialDate().atStartOfDay(),
                    reportRequestDTO.getFinalDate().atTime(23, 59, 59)
            );

            List<MovementDetailDTO> movementDetails = movements.stream().map(movement -> {
                BigDecimal movementAmount = movement.getMovementAmount();
                if (movement.getMovementType().equalsIgnoreCase(MovementsTypeEnum.WITHDRAWAL.getMessage())) {
                    movementAmount = movementAmount.negate();
                }

                return new MovementDetailDTO(
                        Utils.convertLocalDateTimeToString(movement.getMovementDate()),
                        movement.getMovementType(),
                        movementAmount,
                        movement.getMovementBalance()
                );
            }).collect(Collectors.toList());

            return new AccountReportDTO(
                    account.getAccountNumber(),
                    account.getAccountType(),
                    account.getAccountBalance(),
                    movementDetails
            );
        }).collect(Collectors.toList());

        return new ClientReportDTO(
                accounts.get(0).getClientIdentification(),
                accounts.get(0).getClientName(),
                accountReports
        );
    }



//    public ResponseData deleteMovement(MovementId movementId) {
//        Movement movement = movementRepository.findById(movementId.getMovementId())
//                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessagesEnum.MOVEMENTS_NOT_FOUND.getMessage()));
//        movementRepository.delete(movement);
//        return new ResponseData(
//                SuccessMessagesEnum.SUCCESSFULLY_DELETED.getMessage(),
//                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
//                SuccessMessagesEnum.STATUS_OK.getMessage()
//        );
//    }

    @Transactional
    public ResponseData deleteMovement(MovementId movementId) {
        if (!movementRepository.existsById(movementId.getMovementId())) {
            throw new ResourceNotFoundException(ErrorMessagesEnum.MOVEMENTS_NOT_FOUND.getMessage());
        }

        movementRepository.deleteById(movementId.getMovementId()); // método directo del repository

        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_DELETED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }


    private BigDecimal calcularRetirosDelDia(Account account) {
        LocalDateTime inicioDelDia = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime finDelDia = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);

        List<Movement> retirosHoy = movementRepository.findByAccountAndMovementDateBetween(
                account,
                inicioDelDia,
                finDelDia
        );

        return retirosHoy.stream()
                .filter(m -> m.getMovementType().equalsIgnoreCase(MovementsTypeEnum.WITHDRAWAL.getMessage()))
                .map(Movement::getMovementAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }



}
