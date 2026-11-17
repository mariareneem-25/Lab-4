import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reportes {
    private static final int diasAviso = 3;

    private final Cooperativa cooperativa;

    public Reportes(Cooperativa cooperativa) {
        this.cooperativa = cooperativa;
    }

    public List<String> consultarLotes() {
        List<String> lineas = new ArrayList<String>();
        lineas.add("LOTES ALMACENADOS EN ORDEN DE SALIDA");
        int numero = 1;
        for (Lote lote : cooperativa.getLotesAlmacenados()) {
            lineas.add(numero + ". " + lote.getDescripcion());
            numero++;
        }
        if (numero == 1) {
            lineas.add("No hay lotes almacenados.");
        }
        return lineas;
    }

    public List<String> contenidoDeBodegas() {
        List<String> lineas = new ArrayList<String>();
        lineas.add("CONTENIDO DE CADA BODEGA EN ORDEN DE SALIDA");
        for (Bodega bodega : cooperativa.getBodegas()) {
            lineas.add(bodega.getDescripcion());
            int numero = 1;
            for (Lote lote : bodega) {
                lineas.add("  " + numero + ". " + lote.getDescripcion());
                numero++;
            }
            if (numero == 1) {
                lineas.add("  Sin lotes.");
            }
        }
        return lineas;
    }

    public List<String> existenciasPorCategoria() {
        List<String> lineas = new ArrayList<String>();
        lineas.add("EXISTENCIAS POR CATEGORIA (fecha de operacion " + cooperativa.getFechaOperacion() + ")");
        for (FabricaLote fabrica : cooperativa.getFabricas()) {
            lineas.add(fabrica.getCategoria() + ":");
            double total = 0;
            int numero = 1;
            for (Lote lote : cooperativa.getLotesDisponibles(fabrica.getCategoria())) {
                lineas.add("  " + numero + ". " + lote.getDescripcion());
                total = total + lote.getPesoDisponible();
                numero++;
            }
            if (numero == 1) {
                lineas.add("  Sin existencias.");
            }
            lineas.add("  Total disponible: " + Formato.kilos(total));
        }
        return lineas;
    }

    public List<String> proximosAVencer() {
        LocalDate hoy = cooperativa.getFechaOperacion();
        LocalDate limite = hoy.plusDays(diasAviso);
        List<String> lineas = new ArrayList<String>();
        lineas.add("LOTES PERECEDEROS QUE VENCEN HASTA EL " + limite + " (fecha de operacion " + hoy + ")");
        int numero = 1;
        for (Lote lote : cooperativa.getLotesAlmacenados()) {
            if (lote.getPerecedero().isPresent() && !lote.estaVencidoEn(hoy)
                    && !lote.getFechaSalida().isAfter(limite)) {
                lineas.add(numero + ". " + lote.getDescripcion());
                numero++;
            }
        }
        if (numero == 1) {
            lineas.add("No hay lotes que venzan en los proximos " + diasAviso + " dias.");
        }
        return lineas;
    }

    public List<String> totalesPorCategoria() {
        Contabilidad contabilidad = cooperativa.getContabilidad();
        List<String> lineas = new ArrayList<String>();
        lineas.add("TOTALES POR CATEGORIA");
        double pagado = 0;
        double cobrado = 0;
        double perdidas = 0;
        for (FabricaLote fabrica : cooperativa.getFabricas()) {
            String categoria = fabrica.getCategoria();
            lineas.add(categoria + " | Pagado " + Formato.monto(contabilidad.getTotalPagado(categoria))
                    + " | Cobrado " + Formato.monto(contabilidad.getTotalCobrado(categoria))
                    + " | Perdidas " + Formato.monto(contabilidad.getTotalPerdidas(categoria)));
            pagado = pagado + contabilidad.getTotalPagado(categoria);
            cobrado = cobrado + contabilidad.getTotalCobrado(categoria);
            perdidas = perdidas + contabilidad.getTotalPerdidas(categoria);
        }
        lineas.add("TOTAL GENERAL | Pagado " + Formato.monto(pagado) + " | Cobrado " + Formato.monto(cobrado)
                + " | Perdidas " + Formato.monto(perdidas));
        return lineas;
    }
}
