import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FabricaHortaliza extends FabricaLote {

    @Override
    public String getCategoria() {
        return Hortaliza.nombreCategoria;
    }

    @Override
    public List<String> getNombresDatos() {
        return Arrays.asList("Vida util en dias (3 a 14)");
    }

    @Override
    public Lote crear(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido,
            Calidad calidad, List<String> datos) {
        validarDatos(datos);
        int vidaUtil = Conversor.aEntero(datos.get(0), "La vida util");
        return new Hortaliza(codigo, productor, fechaIngreso, pesoRecibido, calidad, vidaUtil);
    }
}
