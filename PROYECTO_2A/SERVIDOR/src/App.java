import java.net.*;
import java.io.*;

public class App {

    // Arreglos para almacenar los datos
    static String[][] perfiles = new String[100][3]; // ID, Nombre, Rol
    static String[][] equipos = new String[50][2]; // ID, Nombre
    static String[][] proyectos = new String[50][4]; // ID, Nombre, EquipoID, Estado
    static String[][] tareas = new String[500][6]; // ID, ProyectoID, Título, Estado, AsignadoA, Porcentaje

    // Contadores para IDs
    static int contadorPerfiles = 0;
    static int contadorEquipos = 0;
    static int contadorProyectos = 0;
    static int contadorTareas = 0;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(5000)) { // Servodor conectado a puerto 5000
            System.out.println("Iniciando el servidor....");

            while (true) {

                try (Socket clientSocket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                    String comando;

                    while ((comando = in.readLine()) != null) {
                        System.out.println("Comando recibido: " + comando);
                        String respuesta = procesarComando(comando);
                        out.println(respuesta);
                    }

                } catch (IOException e) {
                    System.err.println("Error con cliente: " + e.getMessage());
                }

            }

        } catch (IOException e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        }
    }

    
    //Metodo Procesar comando
    static String procesarComando(String comando) {
        String[] partes = comando.split("\\|");
        String tipoComando = partes[0];

        try {
            switch (tipoComando) {

                // ---- PERFILES ----
                case "CREAR_PERFIL":
                    if (partes.length != 3)
                        return "ERROR|Formato: CREAR_PERFIL|nombre|rol";
                    perfiles[contadorPerfiles][0] = String.valueOf(contadorPerfiles);
                    perfiles[contadorPerfiles][1] = partes[1]; // Nombres
                    perfiles[contadorPerfiles][2] = partes[2]; // Roles

                    return "Perfil creado con ID: " + contadorPerfiles++;

                case "LISTAR_PERFILES":
                    if (contadorPerfiles == 0)
                        return "ERROR|No hay perfiles registrados";
                    StringBuilder sbPerfiles = new StringBuilder();
                    for (int i = 0; i < contadorPerfiles; i++) {
                        sbPerfiles.append(String.join(",", perfiles[i]));
                        if (i < contadorPerfiles - 1)
                            sbPerfiles.append(";");
                    }

                    return "OK|" + sbPerfiles.toString();

                // ---- EQUIPOS ----
                case "CREAR_EQUIPO":
                    if (partes.length != 2)
                        return "ERROR|Formato: CREAR_EQUIPO|nombre";
                    equipos[contadorEquipos][0] = String.valueOf(contadorEquipos);
                    equipos[contadorEquipos][1] = partes[1]; // Nombre

                    return "OK|Equipo creado con ID: " + contadorEquipos++;

                case "LISTAR_EQUIPOS":
                    if (contadorEquipos == 0)
                        return "ERROR|No hay equipos registrados";
                    StringBuilder sbEquipos = new StringBuilder();

                    for (int i = 0; i < contadorEquipos; i++) {
                        sbEquipos.append(String.join(",", equipos[i]));
                        if (i < contadorEquipos - 1)
                            sbEquipos.append(";");
                    }
                    return "OK|" + sbEquipos.toString();

                // ---- PROYECTOS ----
                case "CREAR_PROYECTO":
                    if (partes.length != 2)
                        return "ERROR|Formato: CREAR_PROYECTO|nombre";
                    proyectos[contadorProyectos][0] = String.valueOf(contadorProyectos);
                    proyectos[contadorProyectos][1] = partes[1]; // Nombre
                    proyectos[contadorProyectos][2] = null; // EquipoID (sin asignar)
                    proyectos[contadorProyectos][3] = "Activo"; // Estado
                    return "OK|Proyecto creado con ID: " + contadorProyectos++;

                case "LISTAR_PROYECTOS":
                    if (contadorProyectos == 0)
                        return "ERROR|No hay proyectos registrados";
                    StringBuilder sbProyectos = new StringBuilder();
                    for (int i = 0; i < contadorProyectos; i++) {
                        sbProyectos.append(proyectos[i][0] + "," +
                                proyectos[i][1] + "," +
                                (proyectos[i][2] == null ? "null" : proyectos[i][2]) + "," +
                                proyectos[i][3]);
                        if (i < contadorProyectos - 1)
                            sbProyectos.append(";");
                    }
                    return "OK|" + sbProyectos.toString();

                case "ASIGNAR_EQUIPO":
                    if (partes.length != 3)
                        return "ERROR|Formato: ASIGNAR_EQUIPO|proyectoID|equipoID";
                    int proyectoID = Integer.parseInt(partes[1]);
                    int equipoID = Integer.parseInt(partes[2]);

                    if (proyectoID < 0 || proyectoID >= contadorProyectos)
                        return "ERROR|ID de proyecto no válido";
                    if (equipoID < 0 || equipoID >= contadorEquipos)
                        return "ERROR|ID de equipo no válido";

                    proyectos[proyectoID][2] = partes[2]; // Asignar equipoID
                    return "OK|Equipo asignado correctamente";

                // ---- TAREAS ----
                case "CREAR_TAREA":
                    if (partes.length != 3)
                        return "ERROR|Formato: CREAR_TAREA|proyectoID|título";
                    int proyectoIDTarea = Integer.parseInt(partes[1]);

                    if (proyectoIDTarea < 0 || proyectoIDTarea >= contadorProyectos)
                        return "ERROR|ID de proyecto no válido";

                    tareas[contadorTareas][0] = String.valueOf(contadorTareas);
                    tareas[contadorTareas][1] = partes[1]; // ProyectoID
                    tareas[contadorTareas][2] = partes[2]; // Título
                    tareas[contadorTareas][3] = "Pendiente"; // Estado
                    tareas[contadorTareas][4] = "Sin asignar"; // AsignadoA
                    tareas[contadorTareas][5] = "0"; // Porcentaje
                    return "OK|Tarea creada con ID: " + contadorTareas++;

                case "LISTAR_TAREAS":
                    if (contadorTareas == 0)
                        return "ERROR|No hay tareas registradas";
                    StringBuilder sbTareas = new StringBuilder();
                    for (int i = 0; i < contadorTareas; i++) {
                        String proyectoNombre = "Proyecto no encontrado";
                        try {
                            int idProyecto = Integer.parseInt(tareas[i][1]);
                            proyectoNombre = proyectos[idProyecto][1];
                        } catch (Exception e) {
                        }

                        sbTareas.append(tareas[i][0] + "," +
                                proyectoNombre + "," +
                                tareas[i][2] + "," +
                                tareas[i][3] + "," +
                                tareas[i][4] + "," +
                                tareas[i][5]);
                        if (i < contadorTareas - 1)
                            sbTareas.append(";");
                    }

                    return "OK|" + sbTareas.toString();

                case "CAMBIAR_ESTADO_TAREA":
                    if (partes.length != 3)
                        return "ERROR|Formato: CAMBIAR_ESTADO_TAREA|tareaID|estado";
                    int tareaID = Integer.parseInt(partes[1]);
                    int estado = Integer.parseInt(partes[2]);

                    if (tareaID < 0 || tareaID >= contadorTareas)
                        return "ERROR|ID de tarea no válido";

                    String nuevoEstado = switch (estado) {
                        case 1 -> "Pendiente";
                        case 2 -> "En progreso";
                        case 3 -> "Completado";
                        default -> tareas[tareaID][3]; // Mantener estado actual
                    };

                    tareas[tareaID][3] = nuevoEstado;
                    return "Estado actualizado a: " + nuevoEstado;

                // ---- REPORTES ----
                case "REPORTE_AVANCE":
                    if (partes.length != 2)
                        return "ERROR|Formato: REPORTE_AVANCE|proyectoID";
                    int proyectoIDReporte = Integer.parseInt(partes[1]);

                    if (proyectoIDReporte < 0 || proyectoIDReporte >= contadorProyectos)
                        return "ERROR|ID de proyecto no válido";

                    int tareasPendientes = 0;
                    int tareasProgreso = 0;
                    int tareasCompletadas = 0;
                    int totalTareas = 0;

                    for (int i = 0; i < contadorTareas; i++) {

                        if (tareas[i][1] != null && Integer.parseInt(tareas[i][1]) == proyectoIDReporte) {
                            totalTareas++;
                            switch (tareas[i][3]) {
                                case "Pendiente":
                                    tareasPendientes++;
                                    break;
                                case "En progreso":
                                    tareasProgreso++;
                                    break;
                                case "Completado":
                                    tareasCompletadas++;
                                    break;
                            }
                        }
                    }

                    if (totalTareas == 0)
                        return "ERROR|El proyecto no tiene tareas";

                    double porcentaje = (tareasCompletadas * 100.0) / totalTareas;
                    return "OK|Proyecto: " + proyectos[proyectoIDReporte][1] +
                            "|Tareas totales: " + totalTareas +
                            "|Pendientes: " + tareasPendientes +
                            "|En progreso: " + tareasProgreso +
                            "|Completadas: " + tareasCompletadas +
                            "|Porcentaje: " + String.format("%.2f", porcentaje) + "%";

                case "REPORTE_ESTADOS":
                    if (contadorTareas == 0)
                        return "ERROR|No hay tareas registradas";

                    int pendientes = 0;
                    int progreso = 0;
                    int completadas = 0;

                    for (int i = 0; i < contadorTareas; i++) {
                        switch (tareas[i][3]) {
                            case "Pendiente":
                                pendientes++;
                                break;
                            case "En progreso":
                                progreso++;
                                break;
                            case "Completado":
                                completadas++;
                                break;
                        }
                    }

                    return "|Pendientes: " + pendientes +
                            "|En progreso: " + progreso +
                            "|Completadas: " + completadas +
                            "|Total: " + contadorTareas;

                case "SALIR":
                    return "Servidor sigue activo para otros clientes";

                default:
                    return "ERROR --- Comando no reconocido";
            }
        } catch (Exception e) {
            return "ERROR|Error procesando comando: " + e.getMessage();
        }
    }
}