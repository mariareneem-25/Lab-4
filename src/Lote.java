import java.time.LocalDate;
import java.util.Optional;

public abstract class Lote implements Comparable<Lote> {
    public static final double tolerancia = 0.000001;

    private final String codigo;
    private final String productor;
    private final LocalDate fechaIngreso;
    private final double pesoRecibido;
    private final Calidad calidad;
    private double pesoDisponible;
    private EstadoLote estado;

    protected Lote(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad) {
        this.codigo = Conversor.textoNoVacio(codigo, "El codigo del lote").toUpperCase();
        this.productor = Conversor.textoNoVacio(productor, "El nombre del productor");
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso no puede estar vacia.");
        }
        if (pesoRecibido <= 0) {
            throw new IllegalArgumentException("El peso recibido debe ser mayor que cero.");
        }
        if (calidad == null) {
            throw new IllegalArgumentException("La calidad no puede estar vacia.");
        }
        this.fechaIngreso = fechaIngreso;
        this.pesoRecibido = pesoRecibido;
        this.calidad = calidad;
        this.pesoDisponible = pesoRecibido;
        this.estado = EstadoLote.DISPONIBLE;
    }

    public abstract String getCategoria();

    public abstract double calcularPago();

    public String getCodigo() {
        return codigo;
    }

    public String getProductor() {
        return productor;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public double getPesoRecibido() {
        return pesoRecibido;
    }

    public Calidad getCalidad() {
        return calidad;
    }

    public double getPesoDisponible() {
        return pesoDisponible;
    }

    public EstadoLote getEstado() {
        return estado;
    }

    public double getCostoPorKilo() {
        return calcularPago() / pesoRecibido;
    }

    public Optional<Perecedero> getPerecedero() {
        return Optional.empty();
    }

    public boolean requiereCamaraFria() {
        return false;
    }

    public boolean estaVencidoEn(LocalDate fechaOperacion) {
        Optional<Perecedero> perecedero = getPerecedero();
        return perecedero.isPresent() && perecedero.get().estaVencido(fechaOperacion);
    }

    public LocalDate getFechaSalida() {
        Optional<Perecedero> perecedero = getPerecedero();
        if (perecedero.isPresent()) {
            return perecedero.get().getFechaVencimiento();
        }
        return fechaIngreso;
    }

    public String getTextoVencimiento() {
        Optional<Perecedero> perecedero = getPerecedero();
        if (perecedero.isPresent()) {
            return perecedero.get().getFechaVencimiento().toString();
        }
        return "No se vence";
    }

    public void retirar(double kilos) {
        if (estado != EstadoLote.DISPONIBLE) {
            throw new IllegalArgumentException("El lote " + codigo + " no esta disponible.");
        }
        if (kilos <= 0) {
            throw new IllegalArgumentException("Los kilogramos a retirar deben ser mayores que cero.");
        }
        if (kilos > pesoDisponible + tolerancia) {
            throw new IllegalArgumentException("El lote " + codigo + " no tiene suficientes kilogramos.");
        }
        pesoDisponible = pesoDisponible - kilos;
        if (pesoDisponible <= tolerancia) {
            pesoDisponible = 0;
            estado = EstadoLote.AGOTADO;
        }
    }

    public double descartar() {
        if (estado != EstadoLote.DISPONIBLE) {
            throw new IllegalArgumentException("El lote " + codigo + " no esta disponible.");
        }
        double kilosDescartados = pesoDisponible;
        pesoDisponible = 0;
        estado = EstadoLote.DESCARTADO;
        return kilosDescartados;
    }

    public String getDescripcion() {
        return codigo + " | " + getCategoria() + " | " + productor + " | Calidad " + calidad
                + " | Ingreso " + fechaIngreso + " | Disponible " + Formato.kilos(pesoDisponible)
                + " de " + Formato.kilos(pesoRecibido) + " | Vence: " + getTextoVencimiento()
                + " | Estado " + estado;
    }

    private int getPrioridadSalida() {
        if (getPerecedero().isPresent()) {
            return 0;
        }
        return 1;
    }

    @Override
    public int compareTo(Lote otro) {
        int resultado = Integer.compare(getPrioridadSalida(), otro.getPrioridadSalida());
        if (resultado == 0) {
            resultado = getFechaSalida().compareTo(otro.getFechaSalida());
        }
        if (resultado == 0) {
            resultado = calidad.compareTo(otro.calidad);
        }
        if (resultado == 0) {
            resultado = codigo.compareTo(otro.codigo);
        }
        return resultado;
    }
}
