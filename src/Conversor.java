import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Conversor {

    public static String textoNoVacio(String texto, String nombre) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(nombre + " no puede estar vacio.");
        }
        return texto.trim();
    }

    public static double aDecimal(String texto, String nombre) {
        String limpio = textoNoVacio(texto, nombre).replace(',', '.');
        if (!limpio.matches("-?[0-9]+(\\.[0-9]+)?")) {
            throw new IllegalArgumentException("Formato incorrecto (" + nombre + "): se esperaba un numero valido.");
        }
        return Double.parseDouble(limpio);
    }

    public static int aEntero(String texto, String nombre) {
        String limpio = textoNoVacio(texto, nombre);
        if (!limpio.matches("-?[0-9]+")) {
            throw new IllegalArgumentException("Formato incorrecto (" + nombre + "): se esperaba un numero entero valido.");
        }
        try {
            return Integer.parseInt(limpio);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Numero fuera de rango (" + nombre + ").");
        }
    }

    public static LocalDate aFecha(String texto, String nombre) {
        String limpio = textoNoVacio(texto, nombre);
        try {
            return LocalDate.parse(limpio);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato incorrecto (" + nombre + "): se esperaba una fecha con formato aaaa-mm-dd.");
        }
    }
}
