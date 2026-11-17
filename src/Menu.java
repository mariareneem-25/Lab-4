import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Menu {
    private final Cooperativa cooperativa;
    private final Reportes reportes;
    private final Scanner entrada = new Scanner(System.in);

    public Menu(Cooperativa cooperativa) {
        this.cooperativa = cooperativa;
        this.reportes = new Reportes(cooperativa);
    }

    public void iniciar() {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            try {
                continuar = ejecutar(leerTexto("Opcion: "));
            } catch (IllegalArgumentException e) {
                System.out.println("Operacion rechazada: " + e.getMessage());
            } catch (NoSuchElementException e) {
                continuar = false;
            }
        }
        System.out.println("Hasta luego.");
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("===== ACOPIOGT | Fecha de operacion: " + cooperativa.getFechaOperacion() + " =====");
        System.out.println("1. Pre-liquidar y recibir lote");
        System.out.println("2. Registrar bodega");
        System.out.println("3. Registrar pedido y despachar");
        System.out.println("4. Avanzar fecha de operacion");
        System.out.println("5. Consultar lotes");
        System.out.println("6. Consultar bodegas");
        System.out.println("7. Reporte: existencias por categoria y contenido de bodegas");
        System.out.println("8. Reporte: lotes que vencen en los proximos 3 dias");
        System.out.println("9. Reporte: totales por categoria");
        System.out.println("0. Salir");
    }

    private boolean ejecutar(String opcion) {
        switch (opcion.trim()) {
            case "1":
                recibirLote();
                return true;
            case "2":
                registrarBodega();
                return true;
            case "3":
                registrarPedidoYDespachar();
                return true;
            case "4":
                avanzarFecha();
                return true;
            case "5":
                consultarLotes();
                return true;
            case "6":
                imprimir(reportes.contenidoDeBodegas());
                return true;
            case "7":
                imprimir(reportes.existenciasPorCategoria());
                imprimir(reportes.contenidoDeBodegas());
                return true;
            case "8":
                imprimir(reportes.proximosAVencer());
                return true;
            case "9":
                imprimir(reportes.totalesPorCategoria());
                return true;
            case "0":
                return false;
            default:
                System.out.println("Opcion no valida.");
                return true;
        }
    }

    private void recibirLote() {
        FabricaLote fabrica = elegirCategoria();
        String codigo = leerTexto("Codigo del lote: ");
        String productor = leerTexto("Nombre del productor: ");
        double peso = Conversor.aDecimal(leerTexto("Peso recibido en kg: "), "El peso");
        Calidad calidad = Calidad.desdeTexto(leerTexto("Calidad (A, B o C): "));
        List<String> datos = new ArrayList<String>();
        for (String nombre : fabrica.getNombresDatos()) {
            datos.add(leerTexto(nombre + ": "));
        }
        Lote lote = cooperativa.preLiquidar(fabrica, codigo, productor, peso, calidad, datos);
        System.out.println("--- Pre-liquidacion (no se registra nada) ---");
        System.out.println("Lote: " + lote.getCodigo() + " | " + lote.getCategoria());
        System.out.println("Pago al productor: " + Formato.monto(lote.calcularPago()));
        System.out.println("Fecha de vencimiento: " + lote.getTextoVencimiento());
        String respuesta = leerTexto("El productor acepta? (s/n): ");
        if (respuesta.trim().equalsIgnoreCase("s")) {
            for (Bodega bodega : cooperativa.getBodegas()) {
                System.out.println(bodega.getDescripcion());
            }
            String codigoBodega = leerTexto("Codigo de la bodega: ");
            cooperativa.recibirLote(lote, codigoBodega);
            System.out.println("Lote recibido y almacenado. Pago registrado: " + Formato.monto(lote.calcularPago()));
        } else {
            System.out.println("Pre-liquidacion cancelada. No se registro nada.");
        }
    }

    private void registrarBodega() {
        String tipo = leerTexto("Tipo de bodega (1 = seca, 2 = camara fria): ").trim();
        String codigo = leerTexto("Codigo de la bodega: ");
        double capacidad = Conversor.aDecimal(leerTexto("Capacidad maxima en kg: "), "La capacidad");
        Bodega bodega;
        if (tipo.equals("1")) {
            bodega = new BodegaSeca(codigo, capacidad);
        } else if (tipo.equals("2")) {
            bodega = new CamaraFria(codigo, capacidad);
        } else {
            throw new IllegalArgumentException("El tipo de bodega debe ser 1 o 2.");
        }
        cooperativa.registrarBodega(bodega);
        System.out.println("Bodega registrada: " + bodega.getDescripcion());
    }

    private void registrarPedidoYDespachar() {
        FabricaLote fabrica = elegirCategoria();
        double kilos = Conversor.aDecimal(leerTexto("Kilogramos que necesita el comprador: "), "Los kilogramos");
        Pedido pedido = new Pedido(fabrica.getCategoria(), kilos);
        Despacho despacho = cooperativa.despachar(pedido);
        System.out.println("--- Pedido despachado ---");
        for (LineaDespacho linea : despacho.getLineas()) {
            System.out.println(linea.getDescripcion());
        }
        System.out.println("Total cobrado: " + Formato.monto(despacho.getTotalCobrado()));
    }

    private void avanzarFecha() {
        int dias = Conversor.aEntero(leerTexto("Dias a avanzar: "), "Los dias");
        List<String> mensajes = cooperativa.avanzarFecha(dias);
        System.out.println("Nueva fecha de operacion: " + cooperativa.getFechaOperacion());
        if (mensajes.isEmpty()) {
            System.out.println("No se descarto ningun lote.");
        }
        imprimir(mensajes);
    }

    private void consultarLotes() {
        imprimir(reportes.consultarLotes());
        String codigo = leerTexto("Codigo de un lote para ver su detalle (Enter para volver): ");
        if (!codigo.trim().isEmpty()) {
            System.out.println(cooperativa.buscarLote(codigo).getDescripcion());
        }
    }

    private FabricaLote elegirCategoria() {
        List<FabricaLote> fabricas = cooperativa.getFabricas();
        System.out.println("Categorias:");
        for (int i = 0; i < fabricas.size(); i++) {
            System.out.println((i + 1) + ". " + fabricas.get(i).getCategoria());
        }
        int numero = Conversor.aEntero(leerTexto("Categoria: "), "La categoria");
        if (numero < 1 || numero > fabricas.size()) {
            throw new IllegalArgumentException("La categoria elegida no existe.");
        }
        return fabricas.get(numero - 1);
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine();
    }

    private void imprimir(List<String> lineas) {
        for (String linea : lineas) {
            System.out.println(linea);
        }
    }
}
