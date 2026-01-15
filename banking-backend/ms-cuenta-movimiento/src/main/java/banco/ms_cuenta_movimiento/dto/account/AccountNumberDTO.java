package banco.ms_cuenta_movimiento.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountNumberDTO {

    @NotBlank(message = "El numero de cuenta es requerido")
    private String accountNumber;

    public AccountNumberDTO(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
