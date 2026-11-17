public enum Calidad {
    A, B, C;

    public static Calidad desdeTexto(String texto) {
        if (texto == null) {
            throw new IllegalArgumentException("La calidad no puede estar vacia.");
        }
        String limpio = texto.trim().toUpperCase();
        if (limpio.equals("A")) {
            return A;
        }
        if (limpio.equals("B")) {
            return B;
        }
        if (limpio.equals("C")) {
            return C;
        }
        throw new IllegalArgumentException("La calidad debe ser A, B o C.");
    }
}
