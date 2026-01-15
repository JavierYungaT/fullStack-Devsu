package banco.ms_cliente_persona.enums;

public enum ErrorCodesEnum {

    BAD_REQUEST_CODE("400"),
    FORBIDDEN_CODE("403"),
    NOT_FOUND_CODE("404"),
    UNAUTHORIZED_CODE("401"),
    INTERNAL_SERVER_ERROR_CODE("500");


    private final String message;

    ErrorCodesEnum(String message) {

        this.message = message;
    }
    public String getMessage() {

        return message;
    }
}
