import java.net.*;
import java.io.*;
import java.util.Scanner;

public class App {

    static Socket socket;
    static PrintWriter out;
    static BufferedReader in;
    static Scanner entrada = new Scanner(System.in);
    public static void main(String[] args) {

        try {
            // Conectar al servidor (localhost, puerto 5000)
            socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("┌────────────────────────────────────────────────────────┐");
            System.out.println("│                                                        │");
            System.out.println("│         BIENVENIDO A ADMINISTRADOR DE PROYECTOS        │");
            System.out.println("│                                                        │");
            System.out.println("└────────────────────────────────────────────────────────┘");
        
            boolean salir = false;

            while (!salir) {
                System.out.println();
                System.out.println("==========================================================\n");
                System.out.println("┌────────────────┐  ┌────────────────┐  ┌────────────────┐");
                System.out.println("│  1. Perfiles   │  |   2. Equipos   |  |  3. Proyectos  |");
                System.out.println("└────────────────┘  └────────────────┘  └────────────────┘");
                System.out.println();
                System.out.println("┌────────────────┐  ┌────────────────┐  ┌────────────────┐");
                System.out.println("│   4. Tareas    │  |   5. Reportes  |  |    6. Salir    |");
                System.out.println("└────────────────┘  └────────────────┘  └────────────────┘");
                System.out.println();
                System.out.print("                 ▶ Selecciona una opción: ");
                int opcion = entrada.nextInt();
                System.out.println();
                entrada.nextLine();

                switch (opcion) {
                    case 1:
                        menuPerfiles();
                        break;
                    case 2:
                        menuEquipos();
                        break;
                    case 3:
                        menuProyectos();
                        break;
                    case 4:
                        menuTareas();
                        break;
                    case 5:
                        menuReportes();
                        break;
                    case 6:
                        salir = true;
                        break;
                    default:
                        System.out.println("            ┌──────────────────────────────────┐");
                        System.out.println("            │         ¡Opción no válida!       │");
                        System.out.println("            ├──────────────────────────────────┤");
                        System.out.println("            │                                  │");
                        System.out.println("            │     Por favor, seleccione una    │");
                        System.out.println("            │     de las opciones del menú.    │");
                        System.out.println("            │                                  │");
                        System.out.println("            └──────────────────────────────────┘");
                }
            }

            // Cerrrar conexión de servidor
            out.println("SALIR");
            socket.close();
            entrada.close();
            System.out.println("┌────────────────────────────────────────────────────────┐");
            System.out.println("│                                                        │");
            System.out.println("│                 SALIENDO DEL PROGRAMA...               │");
            System.out.println("│                                                        │");
            System.out.println("└────────────────────────────────────────────────────────┘");

        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }

    // ---------------------- MÉTODOS PARA PERFILES ----------------------
    static void menuPerfiles() throws IOException {
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│                                                        │");
        System.out.println("│                   GESTION DE PERFILES                  │");
        System.out.println("│                                                        │");
        System.out.println("└────────────────────────────────────────────────────────┘");

        System.out.println();
        System.out.println("         ┌──────────────────┐  ┌──────────────────┐");
        System.out.println("         │  1. Crear Perfil |  │  2. Ver Perfiles |");
        System.out.println("         └──────────────────┘  └──────────────────┘");
        System.out.println();
        System.out.println("         ┌────────────────────────────────────────┐");
        System.out.println("         │        3. Volver al Menú Principal     │");
        System.out.println("         └────────────────────────────────────────┘");
        System.out.println();
        System.out.print("                 ▶ Selecciona una opción: ");
        int opcion = entrada.nextInt();
        System.out.println();
        entrada.nextLine();
        System.out.println("==========================================================\n");


        switch (opcion) {
            case 1:
                System.out.print("                 ▶ Nombre del perfil: ");
                String nombre = entrada.nextLine();
                System.out.println();

                System.out.print("                 ▶ Rol: ");
                String rol = entrada.nextLine();
                System.out.println();


                //Haciendo peticion al servidor
                out.println("CREAR_PERFIL|" + nombre + "|" + rol);
                System.out.println("                 [ " + in.readLine() + " ]");
                break;

            case 2:
                //Haciendo peticion al servidor
                out.println("LISTAR_PERFILES");
                String respuesta = in.readLine();
                if (respuesta.startsWith("ERROR")) {
                    System.out.println(respuesta.split("\\|")[1]);
                } else {
                    System.out.println("┌────────────────────────────────────────────────────────┐");
                    System.out.println("│                    LISTA DE PERFILES                   │");
                    System.out.println("└────────────────────────────────────────────────────────┘");
                    String[] perfiles = respuesta.split(";");

                    for (String perfil : perfiles) {
                        String[] datos = perfil.split(",");
                    
                    System.out.println();
                    System.out.println("• ID: " + datos[0] + " | Nombre: " + datos[1] + " | Rol: " + datos[2]);

                    }
                }
                break;

            case 3:
                //Mostrando menu principal 
                return;

            default:
                System.out.println("            ┌──────────────────────────────────┐");
                System.out.println("            │         ¡Opción no válida!       │");
                System.out.println("            ├──────────────────────────────────┤");
                System.out.println("            │                                  │");
                System.out.println("            │     Por favor, seleccione una    │");
                System.out.println("            │     de las opciones del menú.    │");
                System.out.println("            │                                  │");
                System.out.println("            └──────────────────────────────────┘");
        }
    }

    // ---------------------- MÉTODOS PARA EQUIPOS ----------------------
    static void menuEquipos() throws IOException {

        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│                                                        │");
        System.out.println("│                   GESTION DE PERFILES                  │");
        System.out.println("│                                                        │");
        System.out.println("└────────────────────────────────────────────────────────┘");

        System.out.println();
        System.out.println("        ┌──────────────────┐  ┌───────────────────┐");
        System.out.println("        │  1. Crear Equipo |  │ 2. Listar Equipos |");
        System.out.println("        └──────────────────┘  └───────────────────┘");
        System.out.println();
        System.out.println("        ┌─────────────────────────────────────────┐");
        System.out.println("        │        3. Volver al Menú Principal      │");
        System.out.println("        └─────────────────────────────────────────┘");
        System.out.println();
        System.out.print("                 ▶ Selecciona una opción: ");
        int opcion = entrada.nextInt();
        System.out.println();
        entrada.nextLine();
        System.out.println("==========================================================\n");

        switch (opcion) {
            case 1:
                System.out.print("                 ▶ Nombre del Equipo: ");
                String nombre = entrada.nextLine();
                System.out.println();

                out.println("CREAR_EQUIPO|" + nombre);
                System.out.println("                 [ " + in.readLine() + " ]");
                break;

            case 2:
                out.println("LISTAR_EQUIPOS");
                String respuesta = in.readLine();

                if (respuesta.startsWith("ERROR")) {
                    System.out.println(respuesta.split("\\|")[1]);
                } else {
                    System.out.println("┌────────────────────────────────────────────────────────┐");
                    System.out.println("│                     LISTA DE EQUIPOS                   │");
                    System.out.println("└────────────────────────────────────────────────────────┘");
                    String[] equipos = respuesta.split(";");

                    for (String equipo : equipos) {
                        String[] datos = equipo.split(",");
                        System.out.println();
                        System.out.println("• ID: " + datos[0] + " | Nombre: " + datos[1]);
                    }
                }
                break;

            case 3:
                return;

            default:
                System.out.println("            ┌──────────────────────────────────┐");
                System.out.println("            │         ¡Opción no válida!       │");
                System.out.println("            ├──────────────────────────────────┤");
                System.out.println("            │                                  │");
                System.out.println("            │     Por favor, seleccione una    │");
                System.out.println("            │     de las opciones del menú.    │");
                System.out.println("            │                                  │");
                System.out.println("            └──────────────────────────────────┘");
        }
    }

    // ---------------------- MÉTODOS PARA PROYECTOS ----------------------
    static void menuProyectos() throws IOException {

        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│                                                        │");
        System.out.println("│                   GESTION DE PROYECTOS                 │");
        System.out.println("│                                                        │");
        System.out.println("└────────────────────────────────────────────────────────┘");

        System.out.println();
        System.out.println("       ┌───────────────────┐  ┌─────────────────────┐");
        System.out.println("       │ 1. Crear Proyecto |  │ 2. Listar Proyectos |");
        System.out.println("       └───────────────────┘  └─────────────────────┘");
        System.out.println();
        System.out.println(" ┌──────────────────────────────┐ ┌─────────────────────┐");
        System.out.println(" │ 3. Asignar Equipo A Proyecto │ | 4. Volver Menu Prin.|");
        System.out.println(" └──────────────────────────────┘ └─────────────────────┘");
        System.out.println();
        System.out.print("                 ▶ Selecciona una opción: ");
        int opcion = entrada.nextInt();
        System.out.println();
        entrada.nextLine();
        System.out.println("==========================================================\n");

        switch (opcion) {
            case 1:
                System.out.print("                 ▶ Nombre del Proyecto: ");
                String nombre = entrada.nextLine();
                System.out.println();

                out.println("CREAR_PROYECTO|" + nombre);
                System.out.println("                 [ " + in.readLine() + " ]");
                break;

            case 2:
                out.println("LISTAR_PROYECTOS");
                String respuesta = in.readLine();

                if (respuesta.startsWith("ERROR")) {
                    System.out.println(respuesta.split("\\|")[1]);
                } else {
                    System.out.println("┌────────────────────────────────────────────────────────┐");
                    System.out.println("│                    LISTA DE PROYECTOS                  │");
                    System.out.println("└────────────────────────────────────────────────────────┘");
                    String[] proyectos = respuesta.split(";");

                    for (String proyecto : proyectos) {
                        String[] datos = proyecto.split(",");

                        System.out.println();
                        System.out.println("• ID: " + datos[0] +
                                " | Nombre: " + datos[1] +
                                " | Equipo: " + (datos[2].equals("null") ? "Sin asignar" : datos[2]) +
                                " | Estado: " + datos[3]);
                    }
                }
                break;

            case 3:
                System.out.print("                 ▶ ID del Proyecto: ");
                int proyectoID = entrada.nextInt();
                System.out.println();

                System.out.print("                 ▶ ID del Equipo: ");
                int equipoID = entrada.nextInt();
                entrada.nextLine();
                System.out.println();


                out.println("ASIGNAR_EQUIPO|" + proyectoID + "|" + equipoID);
                System.out.println("                 [ " + in.readLine() + " ]");
                break;

            case 4:
                return;

            default:
                System.out.println("            ┌──────────────────────────────────┐");
                System.out.println("            │         ¡Opción no válida!       │");
                System.out.println("            ├──────────────────────────────────┤");
                System.out.println("            │                                  │");
                System.out.println("            │     Por favor, seleccione una    │");
                System.out.println("            │     de las opciones del menú.    │");
                System.out.println("            │                                  │");
                System.out.println("            └──────────────────────────────────┘");
        }
    }

    // ---------------------- MÉTODOS PARA TAREAS ----------------------

    static void menuTareas() throws IOException {

        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│                                                        │");
        System.out.println("│                    GESTION DE TAREAS                   │");
        System.out.println("│                                                        │");
        System.out.println("└────────────────────────────────────────────────────────┘");

        System.out.println();
        System.out.println("           ┌────────────────┐  ┌──────────────────┐       ");
        System.out.println("           │ 1. Crear Tarea |  │ 2. Listar Tareas |       ");
        System.out.println("           └────────────────┘  └──────────────────┘       ");
        System.out.println();
        System.out.println("  ┌────────────────────────────┐ ┌─────────────────────┐  ");
        System.out.println("  │ 3. Cambiar Estado de Tarea │ | 4. Volver Menu Prin.|  ");
        System.out.println("  └────────────────────────────┘ └─────────────────────┘  ");
        System.out.println();
        System.out.print("                 ▶ Selecciona una opción: ");
        int opcion = entrada.nextInt();
        System.out.println();
        entrada.nextLine();
        System.out.println("==========================================================\n");

        switch (opcion) {
            case 1:
                System.out.print("                 ▶ Título de la tarea: ");
                String titulo = entrada.nextLine();
                System.out.println();

                System.out.print("                 ▶ ID del Proyecto: ");
                int proyectoID = entrada.nextInt();
                entrada.nextLine();
                System.out.println();


                out.println("CREAR_TAREA|" + proyectoID + "|" + titulo); // Mostrar al cliente
                System.out.println("                 [ " + in.readLine() + " ]");
                break;

            case 2:
                out.println("LISTAR_TAREAS");
                String respuesta = in.readLine();
                if (respuesta.startsWith("ERROR")) {
                    System.out.println(respuesta.split("\\|")[1]);
                } else {
                    System.out.println("┌────────────────────────────────────────────────────────┐");
                    System.out.println("│                      LISTA DE TAREAS                   │");
                    System.out.println("└────────────────────────────────────────────────────────┘");
                    String[] tareas = respuesta.split(";");

                    for (String tarea : tareas) { // buble for each para mostrar matriz de tareas
                        String[] datos = tarea.split(",");
                        System.out.println("• ID: " + datos[0] +
                                " | Proyecto: " + datos[1] +
                                " | Título: " + datos[2] +
                                " | Estado: " + datos[3] +
                                " | Asignado a: " + datos[4] +
                                " | Avance: " + datos[5] + "%");
                    }
                }
                break;

            case 3:
                System.out.print("                 ▶ ID de la tarea: ");
                int tareaID = entrada.nextInt();
                entrada.nextLine();
                System.out.println();

                System.out.println("┌────────────────┐ ┌──────────────────┐ ┌───────────────┐");
                System.out.println("│  1. Pendiente  | │  2. En Progreso  | | 3. Completado |");
                System.out.println("└────────────────┘ └──────────────────┘ └───────────────┘");
                System.out.print("                 ▶ Nuevo Estado: ");;
                int estado = entrada.nextInt();
                entrada.nextLine();

                System.out.println();
                out.println("CAMBIAR_ESTADO_TAREA|" + tareaID + "|" + estado);
                System.out.println("                 [ " + in.readLine() + " ]");
                break;

            case 4:
                return;

            default:
                System.out.println("            ┌──────────────────────────────────┐");
                System.out.println("            │         ¡Opción no válida!       │");
                System.out.println("            ├──────────────────────────────────┤");
                System.out.println("            │                                  │");
                System.out.println("            │     Por favor, seleccione una    │");
                System.out.println("            │     de las opciones del menú.    │");
                System.out.println("            │                                  │");
                System.out.println("            └──────────────────────────────────┘");
        }
    }

    // ---------------------- MÉTODOS PARA REPORTES ----------------------
    static void menuReportes() throws IOException {

        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│                                                        │");
        System.out.println("│                   GESTION DE REPORTES                  │");
        System.out.println("│                                                        │");
        System.out.println("└────────────────────────────────────────────────────────┘");

        System.out.println();
        System.out.println("  ┌──────────────────────────┐  ┌──────────────────────┐");
        System.out.println("  │   1. Avance de Proyecto  │  | 2. Tareas por Estado |");
        System.out.println("  └──────────────────────────┘  └──────────────────────┘");
        System.out.println(); 
        System.out.println("        ┌─────────────────────────────────────────┐");
        System.out.println("        │        3. Volver al Menú Principal      │");
        System.out.println("        └─────────────────────────────────────────┘");
        System.out.println();
        System.out.print("                 ▶ Selecciona una opción: ");
        int opcion = entrada.nextInt();
        System.out.println();
        entrada.nextLine();
        System.out.println("==========================================================\n");

        switch (opcion) {
            case 1:
                System.out.print("                 ▶ ID del Proyecto: ");
                int proyectoID = entrada.nextInt();
                entrada.nextLine();
                System.out.println();

                out.println("REPORTE_AVANCE|" + proyectoID); // Comunicacion entre cliente y servidor
                System.out.println(in.readLine());
                break;

            case 2:
                out.println("REPORTE_ESTADOS");
                System.out.println("                 [ " + in.readLine() + " ]");
                break;

            case 3:
                return;

            default:
                System.out.println("            ┌──────────────────────────────────┐");
                System.out.println("            │         ¡Opción no válida!       │");
                System.out.println("            ├──────────────────────────────────┤");
                System.out.println("            │                                  │");
                System.out.println("            │     Por favor, seleccione una    │");
                System.out.println("            │     de las opciones del menú.    │");
                System.out.println("            │                                  │");
                System.out.println("            └──────────────────────────────────┘");
        }
    }
}
