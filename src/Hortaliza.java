import java.time.LocalDate;
import java.util.Optional;

public class Hortaliza extends Lote implements Perecedero, RequiereCamaraFria {
    public static final String nombreCategoria = "Hortaliza";

    private static final int vidaUtilMinima = 3;
    private static final int vidaUtilMaxima = 14;
    private static final double porcentajePagado = 0.95;

    private final int vidaUtilDias;

    public Hortaliza(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido,
            Calidad calidad, int vidaUtilDias) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad);
        if (vidaUtilDias < vidaUtilMinima || vidaUtilDias > vidaUtilMaxima) {
            throw new IllegalArgumentException("La vida util de la hortaliza debe estar entre 3 y 14 dias.");
        }
        this.vidaUtilDias = vidaUtilDias;
    }

    public int getVidaUtilDias() {
        return vidaUtilDias;
    }

    @Override
    public String getCategoria() {
        return nombreCategoria;
    }

    @Override
    public double calcularPago() {
        return getPesoRecibido() * porcentajePagado * getPrecioPorKilo();
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
        switch (getCalidad()) {
            case A:
                return 4;
            case B:
                return 3;
            default:
                return 2;
        }
    }
}
