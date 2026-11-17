import java.time.LocalDate;
import java.util.Optional;

public class QuesoFresco extends Lote implements Perecedero, RequiereCamaraFria {
    public static final String nombreCategoria = "Queso fresco";

    private static final int vidaUtilDias = 21;
    private static final double empaquePorKilo = 1.50;

    private final double grasa;

    public QuesoFresco(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido,
            Calidad calidad, double grasa) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad);
        if (calidad == Calidad.C) {
            throw new IllegalArgumentException("No se acepta queso fresco de calidad C.");
        }
        if (grasa < 10 || grasa > 40) {
            throw new IllegalArgumentException("El porcentaje de grasa debe estar entre 10 y 40.");
        }
        this.grasa = grasa;
    }

    public double getGrasa() {
        return grasa;
    }

    @Override
    public String getCategoria() {
        return nombreCategoria;
    }

    @Override
    public double calcularPago() {
        return getPesoRecibido() * (getPrecioPorKilo() - empaquePorKilo);
    }

    @Override
    public Optional<Perecedero> getPerecedero() {
        return Optional.of(this);
    }

    @Override
    public LocalDate getFechaVencimiento() {
        return getFechaIngreso().plusDays(vidaUtilDias);
    }

    @Override
    public boolean requiereCamaraFria() {
        return true;
    }

    private double getPrecioPorKilo() {
        if (getCalidad() == Calidad.A) {
            return 38;
        }
        return 30;
    }
}
