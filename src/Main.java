public class Main {

    public static void main(String[] args) {
        CargadorDatos cargador = new CargadorDatos();
        Cooperativa cooperativa = new Cooperativa(cargador.getFechaInicial());
        cargador.cargar(cooperativa);
        Menu menu = new Menu(cooperativa);
        menu.iniciar();
    }
}
