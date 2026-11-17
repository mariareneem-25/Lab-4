import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Cooperativa {
    private LocalDate fechaOperacion;
    private final Map<String, Bodega> bodegas = new LinkedHashMap<String, Bodega>();
    private final Map<String, Lote> lotes = new LinkedHashMap<String, Lote>();
    private final List<FabricaLote> fabricas = new ArrayList<FabricaLote>();
    private final Contabilidad contabilidad = new Contabilidad();

    public Cooperativa(LocalDate fechaInicial) {
        if (fechaInicial == null) {
            throw new IllegalArgumentException("La fecha de operacion no puede estar vacia.");
        }
        this.fechaOperacion = fechaInicial;
    }

    public LocalDate getFechaOperacion() {
        return fechaOperacion;
    }

    public Contabilidad getContabilidad() {
        return contabilidad;
    }

    public void registrarFabrica(FabricaLote fabrica) {
        fabricas.add(fabrica);
    }

    public List<FabricaLote> getFabricas() {
        return new ArrayList<FabricaLote>(fabricas);
    }

    public List<Bodega> getBodegas() {
        return new ArrayList<Bodega>(bodegas.values());
    }

    public void registrarBodega(Bodega bodega) {
        if (bodegas.containsKey(bodega.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una bodega con el codigo " + bodega.getCodigo() + ".");
        }
        bodegas.put(bodega.getCodigo(), bodega);
    }

    public Bodega buscarBodega(String codigo) {
        String limpio = Conversor.textoNoVacio(codigo, "El codigo de la bodega").toUpperCase();
        Bodega bodega = bodegas.get(limpio);
        if (bodega == null) {
            throw new IllegalArgumentException("No existe la bodega " + limpio + ".");
        }
        return bodega;
    }

    public Lote buscarLote(String codigo) {
        String limpio = Conversor.textoNoVacio(codigo, "El codigo del lote").toUpperCase();
        Lote lote = lotes.get(limpio);
        if (lote == null) {
            throw new IllegalArgumentException("No existe el lote " + limpio + ".");
        }
        return lote;
    }

    public void cargarLoteInicial(Lote lote, String codigoBodega) {
        validarCodigoNuevo(lote.getCodigo());
        if (lote.getFechaIngreso().isAfter(fechaOperacion)) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser posterior a la fecha de operacion.");
        }
        Bodega bodega = buscarBodega(codigoBodega);
        bodega.almacenar(lote);
        lotes.put(lote.getCodigo(), lote);
    }

    public Lote preLiquidar(FabricaLote fabrica, String codigo, String productor, double pesoRecibido,
            Calidad calidad, List<String> datos) {
        validarCodigoNuevo(codigo);
        return fabrica.crear(codigo, productor, fechaOperacion, pesoRecibido, calidad, datos);
    }

    public void recibirLote(Lote lote, String codigoBodega) {
        validarCodigoNuevo(lote.getCodigo());
        if (!lote.getFechaIngreso().equals(fechaOperacion)) {
            throw new IllegalArgumentException("El lote debe ingresar en la fecha de operacion.");
        }
        Bodega bodega = buscarBodega(codigoBodega);
        bodega.almacenar(lote);
        lotes.put(lote.getCodigo(), lote);
        contabilidad.registrarPago(lote.getCategoria(), lote.calcularPago());
    }

    public List<String> avanzarFecha(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException(
                    "La fecha de operacion solo puede avanzar. Ingrese una cantidad de dias mayor que cero.");
        }
        LocalDate nuevaFecha = fechaOperacion.plusDays(dias);
        List<String> mensajes = new ArrayList<String>();
        for (Bodega bodega : bodegas.values()) {
            Iterator<Lote> iterador = bodega.iterator();
            while (iterador.hasNext()) {
                Lote lote = iterador.next();
                if (lote.estaVencidoEn(nuevaFecha)) {
                    double costoPorKilo = lote.getCostoPorKilo();
                    double kilos = lote.descartar();
                    double perdida = kilos * costoPorKilo;
                    iterador.remove();
                    contabilidad.registrarPerdida(lote.getCategoria(), perdida);
                    mensajes.add("Lote " + lote.getCodigo() + " descartado de la bodega " + bodega.getCodigo()
                            + ": " + Formato.kilos(kilos) + " x " + Formato.monto(costoPorKilo)
                            + " = perdida de " + Formato.monto(perdida));
                }
            }
        }
        fechaOperacion = nuevaFecha;
        return mensajes;
    }

    public Despacho despachar(Pedido pedido) {
        List<Lote> candidatos = getLotesDisponibles(pedido.getCategoria());
        if (candidatos.isEmpty()) {
            throw new IllegalArgumentException("Pedido rechazado: no hay existencias de " + pedido.getCategoria() + ".");
        }
        Despacho despacho = new Despacho(pedido);
        for (Lote lote : candidatos) {
            if (!despacho.estaCompleto()) {
                despacho.asignar(lote);
            }
        }
        if (!despacho.estaCompleto()) {
            throw new IllegalArgumentException("Pedido rechazado: solo hay " + Formato.kilos(despacho.getKilosAsignados())
                    + " disponibles de " + pedido.getCategoria() + " y se pidieron " + Formato.kilos(pedido.getKilos()) + ".");
        }
        despacho.confirmar();
        retirarAgotados();
        contabilidad.registrarCobro(pedido.getCategoria(), despacho.getTotalCobrado());
        return despacho;
    }

    public List<Lote> getLotesAlmacenados() {
        List<Lote> resultado = new ArrayList<Lote>();
        for (Bodega bodega : bodegas.values()) {
            for (Lote lote : bodega) {
                resultado.add(lote);
            }
        }
        Collections.sort(resultado);
        return resultado;
    }

    public List<Lote> getLotesDisponibles(String categoria) {
        List<Lote> resultado = new ArrayList<Lote>();
        for (Lote lote : getLotesAlmacenados()) {
            if (lote.getCategoria().equals(categoria)) {
                resultado.add(lote);
            }
        }
        return resultado;
    }

    private void validarCodigoNuevo(String codigo) {
        String limpio = Conversor.textoNoVacio(codigo, "El codigo del lote").toUpperCase();
        if (lotes.containsKey(limpio)) {
            throw new IllegalArgumentException("Ya existe un lote con el codigo " + limpio + ".");
        }
    }

    private void retirarAgotados() {
        for (Bodega bodega : bodegas.values()) {
            Iterator<Lote> iterador = bodega.iterator();
            while (iterador.hasNext()) {
                if (iterador.next().getEstado() == EstadoLote.AGOTADO) {
                    iterador.remove();
                }
            }
        }
    }
}
