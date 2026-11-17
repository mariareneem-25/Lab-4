public class LineaDespacho {
    public static final double margen = 0.35;

    private final Lote lote;
    private final double kilos;
    private final double costoPorKilo;

    public LineaDespacho(Lote lote, double kilos) {
        this.lote = lote;
        this.kilos = kilos;
        this.costoPorKilo = lote.getCostoPorKilo();
    }

    public double getKilos() {
        return kilos;
    }

    public double getMonto() {
        return kilos * costoPorKilo * (1 + margen);
    }

    public void retirar() {
        lote.retirar(kilos);
    }

    public String getDescripcion() {
        return lote.getCodigo() + ": " + Formato.kilos(kilos) + " x " + Formato.monto(costoPorKilo)
                + " + " + Math.round(margen * 100) + "% = " + Formato.monto(getMonto());
    }
}
