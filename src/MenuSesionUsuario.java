public class MenuSesionUsuario {

    public static String ID_UsuarioActualSesion = ""; 

    public static void mostrarMenuSesionUsuario(String ID_usuario) {

        ID_UsuarioActualSesion = ID_usuario;
        //En esta clase iran los metodos de cuando el usuario ha iniciado sesion de manera satisfactoria.
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Bienvenido, " + VariablesEstaticas.usuarios[1][Integer.parseInt(ID_usuario)]);
        mostrarOpciones();
    }

    private static void mostrarOpciones() {
        //este metodo muestra el menu de opiones para la sesion del usuario
        System.out.println("");
        System.out.println("---> 1. Informacion de mi perfil");
        System.out.println("---> 2. Crear Proyecto");
        System.out.println("---> 3. Crear Equipo");
        System.out.println("---> 4. Mostrar Mi Lista de Proyectos");
        System.out.println("---> 5. Mostrar Mi Lista de Equipos");
        System.out.println("---> 6. Cerrar Sesion");


    }
}
