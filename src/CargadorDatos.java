import java.time.LocalDate;

public class CargadorDatos {

    public LocalDate getFechaInicial() {
        return LocalDate.of(2026, 11, 16);
    }

    public void cargar(Cooperativa cooperativa) {
        cargarCategorias(cooperativa);
        cargarBodegas(cooperativa);
        cargarLotes(cooperativa);
    }

    private void cargarCategorias(Cooperativa cooperativa) {
        cooperativa.registrarFabrica(new FabricaCafePergamino());
        cooperativa.registrarFabrica(new FabricaCafeOrganico());
        cooperativa.registrarFabrica(new FabricaCardamomo());
        cooperativa.registrarFabrica(new FabricaHortaliza());
        cooperativa.registrarFabrica(new FabricaQuesoFresco());
    }

    private void cargarBodegas(Cooperativa cooperativa) {
        cooperativa.registrarBodega(new BodegaSeca("B1", 1000));
        cooperativa.registrarBodega(new BodegaSeca("B2", 800));
        cooperativa.registrarBodega(new CamaraFria("F1", 500));
        cooperativa.registrarBodega(new CamaraFria("F2", 300));
    }

    private void cargarLotes(Cooperativa cooperativa) {
        cooperativa.cargarLoteInicial(new CafePergamino("CAF001", "Juan Perez", LocalDate.of(2026, 11, 2), 500,
                Calidad.A, 13.4), "B1");
        cooperativa.cargarLoteInicial(new CafePergamino("CAF002", "Rosa Lopez", LocalDate.of(2026, 11, 2), 300,
                Calidad.B, 11.0), "B1");
        cooperativa.cargarLoteInicial(new Cardamomo("CAR001", "Pedro Ramirez", LocalDate.of(2026, 10, 20), 80,
                Calidad.B, 90), "B1");

        cooperativa.cargarLoteInicial(new CafeOrganico("ORG001", "Maria Cifuentes", LocalDate.of(2026, 11, 5), 400,
                Calidad.A, 12.5, "OrganicGT", LocalDate.of(2027, 6, 30)), "B2");
        cooperativa.cargarLoteInicial(new CafeOrganico("ORG002", "Carlos Mendez", LocalDate.of(2026, 11, 10), 250,
                Calidad.B, 10.0, "OrganicGT", LocalDate.of(2027, 1, 31)), "B2");
        cooperativa.cargarLoteInicial(new Cardamomo("CAR002", "Elena Garcia", LocalDate.of(2026, 11, 1), 120,
                Calidad.A, 70), "B2");

        cooperativa.cargarLoteInicial(new Hortaliza("HOR001", "Ana Gomez", LocalDate.of(2026, 11, 14), 200,
                Calidad.A, 3), "F1");
        cooperativa.cargarLoteInicial(new QuesoFresco("QUE001", "Luis Mejia", LocalDate.of(2026, 11, 10), 60,
                Calidad.A, 25), "F1");
        cooperativa.cargarLoteInicial(new QuesoFresco("QUE002", "Luis Mejia", LocalDate.of(2026, 11, 12), 40,
                Calidad.B, 20), "F1");

        cooperativa.cargarLoteInicial(new Hortaliza("HOR002", "Sofia Ramos", LocalDate.of(2026, 11, 12), 150,
                Calidad.B, 7), "F2");
        cooperativa.cargarLoteInicial(new Hortaliza("HOR003", "Mario Diaz", LocalDate.of(2026, 11, 12), 50,
                Calidad.A, 7), "F2");
    }
}
