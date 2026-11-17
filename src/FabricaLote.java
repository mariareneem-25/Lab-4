import java.time.LocalDate;
import java.util.List;

public abstract class FabricaLote {

    public abstract String getCategoria();

    public abstract List<String> getNombresDatos();

    public abstract Lote crear(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido,
            Calidad calidad, List<String> datos);

    protected void validarDatos(List<String> datos) {
        if (datos == null || datos.size() != getNombresDatos().size()) {
            throw new IllegalArgumentException("Faltan datos propios de la categoria " + getCategoria() + ".");
        }
    }
}
