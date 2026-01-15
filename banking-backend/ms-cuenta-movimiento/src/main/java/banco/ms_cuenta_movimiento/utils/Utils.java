package banco.ms_cuenta_movimiento.utils;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Data
public class Utils {


    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(CUSTOM_FORMATTER);

    }

    public static String generateAccountNumber(String identification) {
        String idPart = identification.length() >= 4 ? identification.substring(identification.length() - 4) : identification;
        Random random = new Random();
        String suffix = String.format("%04d", random.nextInt(10000));
        return idPart + suffix;
    }
}
