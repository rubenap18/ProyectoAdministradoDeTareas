
//Clase donde agregar los metodos correspondientes al inicio de Sesion
public class MetodosInicioSesion {

    public static String iniciarSesion(String nombre, String contresena) {
        // codigo para validar inicio de sesion
        for (int i = 0; i < VariablesEstaticas.usuarios[0].length; i++) {
            if (VariablesEstaticas.usuarios[0][i] != null) {
                if (VariablesEstaticas.usuarios[1][i].equals(nombre)) {
                    if (VariablesEstaticas.usuarios[2][i].equals(contresena)) {
                        return "Iniciando Sesion...";
                    } else {
                        return "Contrasena Incorrecta";
                    }
                }
            }
        }
        return "El usuario no existe";

    }

    public static void crearPerfil(String nombre, String contrasena) {
        //codigo para agregar usuarios nuevos
        VariablesEstaticas.ID_contador+=1;

        
        //Agregando ID a al fila 0
        VariablesEstaticas.usuarios[0][VariablesEstaticas.ID_contador] =  String.valueOf(VariablesEstaticas.ID_contador);

        //Agregando Nombre a la fila 1
        VariablesEstaticas.usuarios[1][VariablesEstaticas.ID_contador] = nombre;

        //Agregando contrasena a la fila 2
        VariablesEstaticas.usuarios[2][VariablesEstaticas.ID_contador] = contrasena;


    }
}
