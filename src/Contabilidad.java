import java.util.HashMap;
import java.util.Map;

public class Contabilidad {
    private final Map<String, Double> pagos = new HashMap<String, Double>();
    private final Map<String, Double> cobros = new HashMap<String, Double>();
    private final Map<String, Double> perdidas = new HashMap<String, Double>();

    public void registrarPago(String categoria, double monto) {
        pagos.put(categoria, getTotalPagado(categoria) + monto);
    }

    public void registrarCobro(String categoria, double monto) {
        cobros.put(categoria, getTotalCobrado(categoria) + monto);
    }

    public void registrarPerdida(String categoria, double monto) {
        perdidas.put(categoria, getTotalPerdidas(categoria) + monto);
    }

    public double getTotalPagado(String categoria) {
        return pagos.getOrDefault(categoria, 0.0);
    }

    public double getTotalCobrado(String categoria) {
        return cobros.getOrDefault(categoria, 0.0);
    }

    public double getTotalPerdidas(String categoria) {
        return perdidas.getOrDefault(categoria, 0.0);
    }
}
