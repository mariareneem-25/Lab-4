import java.time.LocalDate;

public interface Perecedero {
    LocalDate getFechaVencimiento();

    default boolean estaVencido(LocalDate fechaOperacion) {
        return fechaOperacion.isAfter(getFechaVencimiento());
    }
}
