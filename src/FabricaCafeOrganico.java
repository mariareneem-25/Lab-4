import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FabricaCafeOrganico extends FabricaLote {

    @Override
    public String getCategoria() {
        return CafeOrganico.nombreCategoria;
    }

    @Override
    public List<String> getNombresDatos() {
        return Arrays.asList("Humedad del grano en porcentaje (8 a 14)", "Entidad certificadora",
                "Fecha hasta la que el sello es valido (aaaa-mm-dd)");
    }

    @Override
    public Lote crear(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido,
            Calidad calidad, List<String> datos) {
        validarDatos(datos);
        double humedad = Conversor.aDecimal(datos.get(0), "La humedad");
        String entidad = datos.get(1);
        LocalDate fechaSello = Conversor.aFecha(datos.get(2), "La fecha del sello");
        return new CafeOrganico(codigo, productor, fechaIngreso, pesoRecibido, calidad, humedad, entidad, fechaSello);
    }
}
