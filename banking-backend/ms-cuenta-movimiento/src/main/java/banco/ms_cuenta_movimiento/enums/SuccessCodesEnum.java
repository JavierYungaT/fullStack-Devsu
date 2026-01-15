package banco.ms_cuenta_movimiento.enums;

public enum SuccessCodesEnum {

    SUCCESS_CODE("200"),
    SUCCESSFUL_USE("000"),
    FAILED_SERIALIZE_TRANSACTION("111"),
    FAILED_TRANSACTION("222");

    private final String message;

    SuccessCodesEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
