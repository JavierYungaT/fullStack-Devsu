package banco.ms_cliente_persona.enums;

public enum ErrorMessagesEnum {

    CLIENT_NOT_FOUND("Cliente no existe"),
    FIELD_NOT_FOUND("Campo no encontrado"),
    ACCOUNTS_NOT_FOUND("No existen cuentas"),
    MOVEMENTS_NOT_FOUND("No existen movimientos en esta cuenta"),
    ACCOUNT_NOT_FOUND("No existe la cuenta"),
    CLIENT_ALREADY_EXISTS("Cliente ya existe"),
    ACCOUNT_ALREADY_EXISTS("La cuenta ya existe"),
    INSUFFICIENT_BALANCE("Saldo no disponible"),
    MOVEMENTS_NOT_EXISTS("No existen movimientos"),
    INVALID_MOVEMENT_TYPE("Tipo de movimiento invalido"),
    INVALID_DATE_FORMAT("Formato de fecha invalido"),
    FIELD_MODIFIED_NOT_ALLOWED("Campo no permitido para modificar.")
    ;


    private final String message;

    ErrorMessagesEnum(String message) {

        this.message = message;
    }

    public String getMessage() {

        return message;
    }
}
