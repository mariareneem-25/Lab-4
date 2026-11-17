import java.util.ArrayList;
import java.util.List;

public class Despacho {
    private final Pedido pedido;
    private final List<LineaDespacho> lineas = new ArrayList<LineaDespacho>();
    private double kilosAsignados;

    public Despacho(Pedido pedido) {
        this.pedido = pedido;
        this.kilosAsignados = 0;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public double getKilosAsignados() {
        return kilosAsignados;
    }

    public double getKilosFaltantes() {
        return pedido.getKilos() - kilosAsignados;
    }

    public boolean estaCompleto() {
        return getKilosFaltantes() <= Lote.tolerancia;
    }

    public void asignar(Lote lote) {
        double kilos = Math.min(getKilosFaltantes(), lote.getPesoDisponible());
        if (kilos > 0) {
            lineas.add(new LineaDespacho(lote, kilos));
            kilosAsignados = kilosAsignados + kilos;
        }
    }

    public double getTotalCobrado() {
        double total = 0;
        for (LineaDespacho linea : lineas) {
            total = total + linea.getMonto();
        }
        return total;
    }

    public void confirmar() {
        for (LineaDespacho linea : lineas) {
            linea.retirar();
        }
    }

    public List<LineaDespacho> getLineas() {
        return new ArrayList<LineaDespacho>(lineas);
    }
}
