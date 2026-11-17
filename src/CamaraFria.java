public class CamaraFria extends Bodega {

    public CamaraFria(String codigo, double capacidadMaxima) {
        super(codigo, capacidadMaxima);
    }

    @Override
    public String getTipo() {
        return "Camara fria";
    }

    @Override
    public boolean admiteLote(Lote lote) {
        return lote.requiereCamaraFria();
    }
}
