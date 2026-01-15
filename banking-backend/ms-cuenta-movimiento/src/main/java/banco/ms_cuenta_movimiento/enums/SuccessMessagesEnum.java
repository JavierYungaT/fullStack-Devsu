package banco.ms_cuenta_movimiento.enums;

public enum SuccessMessagesEnum {

    SUCCESSFULLY_CREATED("Creado exitosamente"),
    SUCCESSFULLY_UPDATED("Actualizado exitosamente"),
    SUCCESSFULLY_DELETED("Eliminado exitosamente"),
    SUCCESSFULLY_DISABLED("Deshabilitado exitosamente"),
    SUCCESSFULLY_CONFIRMED("Confirmado exitosamente"),
    TRANSACTION_SUCCESSFUL("Transaccion realizada exitosamente"),
    STATUS_OK("OK");


    private final String message;

    SuccessMessagesEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
