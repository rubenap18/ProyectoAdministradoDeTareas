import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        //Bienvenida
        System.out.println("-------------------------------------------");
        System.out.println("| BIENVENIDO A ADMINISTRADOR DE PROYECTOS |");
        System.out.println("-------------------------------------------");

        Scanner entrada = new Scanner(System.in);
        boolean salir = false;
        do {
            System.out.println();
            System.out.println("---> 1. Iniciar Sesion");
            System.out.println("---> 2. Crear Perfil");
            System.out.println("---> 3. Salir del Programa");
            System.out.print("---> Ingresa tu opcion: ");
            int opcion = entrada.nextInt();

            if (opcion == 1) {
                // Iniciar sesion
                Scanner entrada1 = new Scanner(System.in);
                System.out.println("------------------------------");
                System.out.print("Ingresa tu nombre de usuario: ");
                String nombre = entrada1.nextLine();
                System.out.print("Ingresa tu password: ");
                String contrasena = entrada1.nextLine();

                // Llamando a metodo que valide los datos
                System.out.println("------------------------------------");
                System.out.println("|   " + MetodosInicioSesion.iniciarSesion(nombre, contrasena) + "   |");
                System.out.println("------------------------------------");

                MenuSesionUsuario.mostrarMenuSesionUsuario(MetodosDeBusqueda.buscarIdPorNombre(nombre));

            } else if (opcion == 2) {
                // Crear perfil
                Scanner entrada1 = new Scanner(System.in);

                System.out.println();
                System.out.print("Ingresa el nombre de usuario:");
                String nombre = entrada1.nextLine();

                System.out.print("Ingresa la contrasena:");
                String constrasena = entrada1.nextLine();

                MetodosInicioSesion.crearPerfil(nombre, constrasena);

                System.out.println("--------------------------------");
                System.out.println("| Perfil creado exitosamente.  |");
                System.out.println("--------------------------------");


            } else if (opcion == 3) {
                // Salir del Programa
                System.out.println("-------------------------------------------");
                System.out.println("|               Saliendo...               |");
                System.out.println("-------------------------------------------");
                salir = true;
            }
        } while (!salir);
        entrada.close();

    }


    // Muestra los datos del perfil de usuario
    public static void mostrarInformacionPerfil(int ID_usuario) {
        System.out.println("-------------------------------------------");
        System.out.println("| BIENVENIDO A ADMINISTRADOR DE PROYECTOS |");
        System.out.println("-------------------------------------------");
    }
}
