public class MetodosDeBusqueda {
    public static String buscarIdPorNombre(String nombre) {
        for (int i = 0; i < VariablesEstaticas.usuarios[0].length; i++) {
            if (VariablesEstaticas.usuarios[1][i] != null) {
                if (VariablesEstaticas.usuarios[1][i].equals(nombre)) {
                    return VariablesEstaticas.usuarios[0][i];
                }
            }
        }
        return "";
    }
}
