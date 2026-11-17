public class BodegaSeca extends Bodega {

    public BodegaSeca(String codigo, double capacidadMaxima) {
        super(codigo, capacidadMaxima);
    }

    @Override
    public String getTipo() {
        return "Bodega seca";
    }

    @Override
    public boolean admiteLote(Lote lote) {
        return !lote.requiereCamaraFria();
    }
}
