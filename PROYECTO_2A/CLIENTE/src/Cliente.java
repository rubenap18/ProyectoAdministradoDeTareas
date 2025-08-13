import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        
        try {
            // Conexión al servidor
            Socket socket = new Socket("192.168.140.12", 1800); 
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner entrada = new Scanner(System.in);

            System.out.println("-------------------------------------------");
            System.out.println("| BIENVENIDO A ADMINISTRADOR DE PROYECTOS |");
            System.out.println("-------------------------------------------");

            boolean salir = false;
            
            // Bucle while para menu con salir validado

            while (!salir) {
                System.out.println("\n---> 1. Perfiles");
                System.out.println("---> 2. Equipos");
                System.out.println("---> 3. Proyectos");
                System.out.println("---> 4. Tareas");
                System.out.println("---> 5. Reportes");
                System.out.println("---> 6. Salir");
                System.out.print("-----> Ingresa una opción: ");
                int opcion = entrada.nextInt();
                entrada.nextLine();

                switch (opcion) {
                    case 1: menuPerfiles(out, in, entrada); break; // Reciben los parametros requeridos
                    case 2: menuEquipos(out, in, entrada); break;
                    case 3: menuProyectos(out, in, entrada); break;
                    case 4: menuTareas(out, in, entrada); break;
                    case 5: menuReportes(out, in, entrada); break;
                    case 6: salir = true; break;
                    default: System.out.println("Opción no válida");
                }
            }

            // Cerrar conexión
            out.println("SALIR");
            socket.close();
            entrada.close();
            System.out.println("Gracias por utilizar el programa");

        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }

    // ---------------------- FUNCION PARA PERFILES ----------------------
    static void menuPerfiles(PrintWriter out, BufferedReader in, Scanner entrada) throws IOException {
        System.out.println("\n--- GESTIÓN DE PERFILES ---");
        System.out.println("1. Crear perfil");
        System.out.println("2. Ver perfiles");
        System.out.println("3. Volver al menú principal");
        System.out.print("Opción: ");
        int opcion = entrada.nextInt();
        entrada.nextLine();

        switch (opcion) {
            case 1:
                System.out.print("Nombre del perfil: ");
                String nombre = entrada.nextLine();

                System.out.print("Rol: ");
                String rol = entrada.nextLine();

                out.println("CREAR_PERFIL|"+nombre+"|"+rol);
                System.out.println(in.readLine());
                break;

            case 2:
                out.println("LISTAR_PERFILES");
                String respuesta = in.readLine();

                if (respuesta.startsWith("ERROR")) {
                    System.out.println(respuesta.split("\\|")[1]); // Separar el arreglo con simbolo

                } else {
                    System.out.println("\n--- LISTA DE PERFILES ---");
                    String[] perfiles = respuesta.split(";");

                    for (String perfil:perfiles) {
                        String[] datos = perfil.split(",");
                        System.out.println("ID: "+datos[0]+" | Nombre: "+datos[1]+" | Rol: "+datos[2]);
                    }
                }
                break;

            case 3:
                return;

            default:
                System.out.println("Opción no válida");
        }
    }

    // ---------------------- FUNCION PARA EQUIPOS ----------------------
    static void menuEquipos(PrintWriter out, BufferedReader in, Scanner entrada) throws IOException {
        System.out.println("\n--- GESTIÓN DE EQUIPOS ---");
        System.out.println("1. Crear equipo");
        System.out.println("2. Listar equipos");
        System.out.println("3. Volver al menú principal");
        System.out.print("Opción: ");
        int opcion = entrada.nextInt();
        entrada.nextLine();

        switch (opcion) {
            case 1:
                System.out.print("Nombre del equipo: ");
                String nombre = entrada.nextLine();
                out.println("CREAR_EQUIPO|" + nombre);
                System.out.println(in.readLine());
                break;

            case 2:
                out.println("LISTAR_EQUIPOS");
                String respuesta = in.readLine();
                if (respuesta.startsWith("ERROR")) {
                    System.out.println(respuesta.split("\\|")[1]);
                } else {
                    System.out.println("\n--- LISTA DE EQUIPOS ---");
                    String[] equipos = respuesta.split(";");
                    for (String equipo : equipos) {
                        String[] datos = equipo.split(",");
                        System.out.println("ID: " + datos[0] + " | Nombre: " + datos[1]);
                    }
                }
                break;

            case 3:
                return;

            default:
                System.out.println("Opción no válida");
        }
    }

    // ---------------------- FUNCION PARA PROYECTOS ----------------------
    static void menuProyectos(PrintWriter out, BufferedReader in, Scanner entrada) throws IOException {
        System.out.println("\n--- GESTIÓN DE PROYECTOS ---");
        System.out.println("1. Crear proyecto");
        System.out.println("2. Listar proyectos");
        System.out.println("3. Asignar equipo a proyecto");
        System.out.println("4. Volver al menú principal");
        System.out.print("Opción: ");
        int opcion = entrada.nextInt();
        entrada.nextLine();

        switch (opcion) {
            case 1:
                System.out.print("Nombre del proyecto: ");
                String nombre = entrada.nextLine();
                out.println("CREAR_PROYECTO|" + nombre);
                System.out.println(in.readLine());
                break;

            case 2:
                out.println("LISTAR_PROYECTOS");
                String respuesta = in.readLine();
                if (respuesta.startsWith("ERROR")) {
                    System.out.println(respuesta.split("\\|")[1]);
                } else {
                    System.out.println("\n--- LISTA DE PROYECTOS ---");
                    String[] proyectos = respuesta.split(";");
                    for (String proyecto : proyectos) {
                        String[] datos = proyecto.split(",");
                        System.out.println("ID: " + datos[0] + 
                                         " | Nombre: " + datos[1] + 
                                         " | Equipo: " + (datos[2].equals("null") ? "Sin asignar" : datos[2]) + 
                                         " | Estado: " + datos[3]);
                    }
                }
                break;

            case 3:
                System.out.print("ID del proyecto: ");
                int proyectoID = entrada.nextInt();
                System.out.print("ID del equipo: ");
                int equipoID = entrada.nextInt();
                entrada.nextLine();
                out.println("ASIGNAR_EQUIPO|" + proyectoID + "|" + equipoID);
                System.out.println(in.readLine());
                break;

            case 4:
                return;

            default:
                System.out.println("Opción no válida");
        }
    }

    // ---------------------- FUNCION PARA TAREAS ----------------------
    static void menuTareas(PrintWriter out, BufferedReader in, Scanner entrada) throws IOException {
        System.out.println("\n--- GESTIÓN DE TAREAS ---");
        System.out.println("1. Crear tarea");
        System.out.println("2. Listar tareas");
        System.out.println("3. Cambiar estado de tarea");
        System.out.println("4. Volver al menú principal");
        System.out.print("Opción: ");
        int opcion = entrada.nextInt();
        entrada.nextLine();

        switch (opcion) {
            case 1:
                System.out.print("Título de la tarea: ");
                String titulo = entrada.nextLine();
                System.out.print("ID del proyecto: ");
                int proyectoID = entrada.nextInt();
                entrada.nextLine();
                out.println("CREAR_TAREA|" + proyectoID + "|" + titulo);
                System.out.println(in.readLine());
                break;

            case 2:
                out.println("LISTAR_TAREAS");
                String respuesta = in.readLine();
                if (respuesta.startsWith("ERROR")) {
                    System.out.println(respuesta.split("\\|")[1]);
                } else {
                    System.out.println("\n--- LISTA DE TAREAS ---");
                    String[] tareas = respuesta.split(";"); // Dividir el vector por ;
                    for (String tarea : tareas) {
                        String[] datos = tarea.split(","); // Dividir el vector por ,
                        System.out.println("ID: " + datos[0] + 
                                       " | Proyecto: " + datos[1] +
                                       " | Título: " + datos[2] + 
                                       " | Estado: " + datos[3] +
                                       " | Asignado a: " + datos[4] +
                                       " | Avance: " + datos[5] + "%");
                    }
                }
                break;

            case 3:
                System.out.print("ID de la tarea: ");
                int tareaID = entrada.nextInt();
                entrada.nextLine();
                System.out.println("1. Pendiente");
                System.out.println("2. En progreso");
                System.out.println("3. Completado");
                System.out.print("Nuevo estado: ");
                int estado = entrada.nextInt();
                entrada.nextLine();
                out.println("CAMBIAR_ESTADO_TAREA|" + tareaID + "|" + estado);
                System.out.println(in.readLine());
                break;

            case 4:
                return;

            default:
                System.out.println("Opción no válida");
        }
    }

    // ---------------------- FUNCION PARA REPORTES ----------------------
    static void menuReportes(PrintWriter out, BufferedReader in, Scanner entrada) throws IOException {
        System.out.println("\n--- REPORTES ---");
        System.out.println("1. Avance de proyecto");
        System.out.println("2. Tareas por estado");
        System.out.println("3. Volver al menú principal");
        System.out.print("Opción: ");
        int opcion = entrada.nextInt();
        entrada.nextLine();

        switch(opcion){
            case 1:
                System.out.print("ID del proyecto: ");
                int proyectoID = entrada.nextInt();
                entrada.nextLine();
                out.println("REPORTE_AVANCE|" + proyectoID);
                System.out.println(in.readLine());
                break;

            case 2:
                out.println("REPORTE_ESTADOS");
                System.out.println(in.readLine());
                break;

            case 3:
                return;

            default:
                System.out.println("Opción no válida");
        }
    }
}