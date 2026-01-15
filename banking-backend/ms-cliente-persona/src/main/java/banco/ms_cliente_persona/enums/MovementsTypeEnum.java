package banco.ms_cliente_persona.enums;

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
