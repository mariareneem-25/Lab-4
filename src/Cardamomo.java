import java.time.LocalDate;
import java.util.Optional;

public class Cardamomo extends Lote implements Perecedero {
    public static final String nombreCategoria = "Cardamomo";

    private static final int vidaUtilDias = 180;
    private static final double granoVerdeParaBono = 85;
    private static final double bono = 0.10;

    private final double granoVerde;

    public Cardamomo(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido,
            Calidad calidad, double granoVerde) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad);
        if (granoVerde < 0 || granoVerde > 100) {
            throw new IllegalArgumentException("El porcentaje de grano verde debe estar entre 0 y 100.");
        }
        this.granoVerde = granoVerde;
    }

    public double getGranoVerde() {
        return granoVerde;
    }

    @Override
    public String getCategoria() {
        return nombreCategoria;
    }

    @Override
    public double calcularPago() {
        double pago = getPesoRecibido() * getPrecioPorKilo();
        if (granoVerde >= granoVerdeParaBono) {
            pago = pago + pago * bono;
        }
        return pago;
    }

    @Override
    public Optional<Perecedero> getPerecedero() {
        return Optional.of(this);
    }

    @Override
    public LocalDate getFechaVencimiento() {
        return getFechaIngreso().plusDays(vidaUtilDias);
    }

    private double getPrecioPorKilo() {
        switch (getCalidad()) {
            case A:
                return 60;
            case B:
                return 48;
            default:
                return 35;
        }
    }
}
