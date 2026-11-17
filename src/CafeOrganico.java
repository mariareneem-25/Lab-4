import java.time.LocalDate;

public class CafeOrganico extends CafePergamino {
    public static final String nombreCategoria = "Cafe organico";

    private static final double bonificacionPorKilo = 0.75;

    private final String entidadCertificadora;
    private final LocalDate fechaValidezSello;

    public CafeOrganico(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido,
            Calidad calidad, double humedad, String entidadCertificadora, LocalDate fechaValidezSello) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad, humedad);
        this.entidadCertificadora = Conversor.textoNoVacio(entidadCertificadora, "La entidad certificadora");
        if (fechaValidezSello == null) {
            throw new IllegalArgumentException("La fecha de validez del sello no puede estar vacia.");
        }
        if (fechaValidezSello.isBefore(fechaIngreso)) {
            throw new IllegalArgumentException("El sello de certificacion esta vencido en la fecha de ingreso.");
        }
        this.fechaValidezSello = fechaValidezSello;
    }

    public String getEntidadCertificadora() {
        return entidadCertificadora;
    }

    public LocalDate getFechaValidezSello() {
        return fechaValidezSello;
    }

    @Override
    public String getCategoria() {
        return nombreCategoria;
    }

    @Override
    public double calcularPago() {
        return super.calcularPago() + getPesoRecibido() * bonificacionPorKilo;
    }
}
