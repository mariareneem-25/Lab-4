import java.time.LocalDate;

public class CafePergamino extends Lote {
    public static final String nombreCategoria = "Cafe en pergamino";

    private static final double humedadMinima = 8;
    private static final double humedadMaxima = 14;
    private static final double humedadBase = 12;
    private static final double descuentoPorPunto = 0.03;

    private final double humedad;

    public CafePergamino(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido,
            Calidad calidad, double humedad) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad);
        if (humedad < humedadMinima || humedad > humedadMaxima) {
            throw new IllegalArgumentException("La humedad del cafe debe estar entre 8 y 14.");
        }
        this.humedad = humedad;
    }

    public double getHumedad() {
        return humedad;
    }

    @Override
    public String getCategoria() {
        return nombreCategoria;
    }

    @Override
    public double calcularPago() {
        double pago = getPesoRecibido() * getPrecioPorKilo();
        if (humedad > humedadBase) {
            double puntos = Math.ceil(humedad - humedadBase);
            pago = pago - pago * descuentoPorPunto * puntos;
        }
        return pago;
    }

    private double getPrecioPorKilo() {
        switch (getCalidad()) {
            case A:
                return 12;
            case B:
                return 9;
            default:
                return 6;
        }
    }
}
