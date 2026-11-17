import java.util.Iterator;
import java.util.TreeSet;

public abstract class Bodega implements Iterable<Lote> {
    private final String codigo;
    private final double capacidadMaxima;
    private final TreeSet<Lote> lotes = new TreeSet<Lote>();

    protected Bodega(String codigo, double capacidadMaxima) {
        this.codigo = Conversor.textoNoVacio(codigo, "El codigo de la bodega").toUpperCase();
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad de la bodega debe ser mayor que cero.");
        }
        this.capacidadMaxima = capacidadMaxima;
    }

    public abstract String getTipo();

    public abstract boolean admiteLote(Lote lote);

    public String getCodigo() {
        return codigo;
    }

    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public double getPesoOcupado() {
        double total = 0;
        for (Lote lote : lotes) {
            total = total + lote.getPesoDisponible();
        }
        return total;
    }

    public double getCapacidadLibre() {
        return capacidadMaxima - getPesoOcupado();
    }

    public void validarRecepcion(Lote lote) {
        if (!admiteLote(lote)) {
            throw new IllegalArgumentException("El lote " + lote.getCodigo() + " (" + lote.getCategoria()
                    + ") no es compatible con la bodega " + codigo + " (" + getTipo() + ").");
        }
        if (lote.getPesoRecibido() > getCapacidadLibre() + Lote.tolerancia) {
            throw new IllegalArgumentException("El lote " + lote.getCodigo() + " ("
                    + Formato.kilos(lote.getPesoRecibido()) + ") excede la capacidad libre de la bodega "
                    + codigo + " (" + Formato.kilos(getCapacidadLibre()) + ").");
        }
    }

    public void almacenar(Lote lote) {
        validarRecepcion(lote);
        lotes.add(lote);
    }

    public String getDescripcion() {
        return "Bodega " + codigo + " | " + getTipo() + " | Capacidad " + Formato.kilos(capacidadMaxima)
                + " | Ocupado " + Formato.kilos(getPesoOcupado()) + " | Libre " + Formato.kilos(getCapacidadLibre());
    }

    @Override
    public Iterator<Lote> iterator() {
        return lotes.iterator();
    }
}
