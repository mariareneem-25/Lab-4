public class Pedido {
    private final String categoria;
    private final double kilos;

    public Pedido(String categoria, double kilos) {
        this.categoria = Conversor.textoNoVacio(categoria, "La categoria del pedido");
        if (kilos <= 0) {
            throw new IllegalArgumentException("Los kilogramos del pedido deben ser mayores que cero.");
        }
        this.kilos = kilos;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getKilos() {
        return kilos;
    }
}
