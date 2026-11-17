import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FabricaCafePergamino extends FabricaLote {

    @Override
    public String getCategoria() {
        return CafePergamino.nombreCategoria;
    }

    @Override
    public List<String> getNombresDatos() {
        return Arrays.asList("Humedad del grano en porcentaje (8 a 14)");
    }

    @Override
    public Lote crear(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido,
            Calidad calidad, List<String> datos) {
        validarDatos(datos);
        double humedad = Conversor.aDecimal(datos.get(0), "La humedad");
        return new CafePergamino(codigo, productor, fechaIngreso, pesoRecibido, calidad, humedad);
    }
}
