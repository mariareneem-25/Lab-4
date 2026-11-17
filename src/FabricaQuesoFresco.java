import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FabricaQuesoFresco extends FabricaLote {

    @Override
    public String getCategoria() {
        return QuesoFresco.nombreCategoria;
    }

    @Override
    public List<String> getNombresDatos() {
        return Arrays.asList("Porcentaje de grasa (10 a 40)");
    }

    @Override
    public Lote crear(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido,
            Calidad calidad, List<String> datos) {
        validarDatos(datos);
        double grasa = Conversor.aDecimal(datos.get(0), "La grasa");
        return new QuesoFresco(codigo, productor, fechaIngreso, pesoRecibido, calidad, grasa);
    }
}
