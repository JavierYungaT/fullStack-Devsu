package banco.ms_cuenta_movimiento.enums;

public enum MovementsTypeEnum {

    DEPOSIT("Deposito"),
    WITHDRAWAL("Retiro");

    private final String message;

    MovementsTypeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
