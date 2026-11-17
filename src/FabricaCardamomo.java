import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FabricaCardamomo extends FabricaLote {

    @Override
    public String getCategoria() {
        return Cardamomo.nombreCategoria;
    }

    @Override
    public List<String> getNombresDatos() {
        return Arrays.asList("Porcentaje de grano verde (0 a 100)");
    }

    @Override
    public Lote crear(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido,
            Calidad calidad, List<String> datos) {
        validarDatos(datos);
        double granoVerde = Conversor.aDecimal(datos.get(0), "El grano verde");
        return new Cardamomo(codigo, productor, fechaIngreso, pesoRecibido, calidad, granoVerde);
    }
}
