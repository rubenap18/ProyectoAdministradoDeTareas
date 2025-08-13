import java.net.*;
import java.io.*;

public class Servidor {
    public static void main(String[] args) {
        
        // Almacenamiento de datos en servidor con matrices
        String[][] perfiles = new String[100][3];  // ID, Nombre, Rol
        String[][] equipos = new String[50][2];    // ID, Nombre
        String[][] proyectos = new String[50][4];  // ID, Nombre, EquipoID, Estado
        String[][] tareas = new String[500][6];   // ID, ProyectoID, Título, Estado, AsignadoA, Porcentaje
        
        int[] contadores = new int[4];  // Índices: 0=perfiles, 1=equipos, 2=proyectos, 3=tareas

        try (ServerSocket serverSocket = new ServerSocket(1800)) {
            System.out.println("Servidor esperando conexion");

            while (true) {
                
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Cliente conectado: "+clientSocket.getInetAddress());

                    String comando;

                    while ((comando = in.readLine()) != null) {
                        System.out.println("Comando recibido: " + comando);
                        String respuesta = procesarComando(
                            comando, 
                            perfiles, 
                            equipos, 
                            proyectos, 
                            tareas, 
                            contadores
                        );
                        out.println(respuesta);
                    }
                }catch(IOException e){
                    System.err.println("Error con cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        }
    }

    public static String procesarComando(
        String comando, 
        String[][] perfiles,  // Recibe como parametro los arreglos
        String[][] equipos, 
        String[][] proyectos, 
        String[][] tareas, 
        int[] contadores
    )  

    {
        String[] partes = comando.split("\\|"); // Dividir el arreglo entre simbolos
        String tipoComando = partes[0];

        try {
            switch (tipoComando) {
                // ---- PERFILES ---- 
                case "CREAR_PERFIL":
                    if (partes.length != 3) return "ERROR | FORMATO INCORRECTO"; // Debe ser 3 porque matriz tiene 3 espacios (accion, nombre, rol)
                    perfiles[contadores[0]][0] = String.valueOf(contadores[0]);
                    perfiles[contadores[0]][1] = partes[1];
                    perfiles[contadores[0]][2] = partes[2];
                    return "Perfil creado con ID: " + contadores[0]++;

                case "LISTAR_PERFILES":
                    if (contadores[0] == 0) return "ERROR | No hay perfiles registrados";
                    StringBuilder sbPerfiles = new StringBuilder(); // Se crea para poder formatear la lista despues
                    for (int i = 0; i < contadores[0]; i++) {
                        sbPerfiles.append(String.join(",", perfiles[i]));
                        if (i < contadores[0] - 1) sbPerfiles.append(";"); // Formatea la lista antes de enviarla al cliente
                    }
                    return "|" + sbPerfiles.toString();

                // ---- EQUIPOS ----
                case "CREAR_EQUIPO":
                    if (partes.length != 2) return "ERROR | FORMATO INCORRECTO";
                    equipos[contadores[1]][0] = String.valueOf(contadores[1]);
                    equipos[contadores[1]][1] = partes[1];
                    return "Equipo creado con ID: " + contadores[1]++;

                case "LISTAR_EQUIPOS":
                    if (contadores[1] == 0) return "ERROR | No hay equipos registrados";
                    StringBuilder sbEquipos = new StringBuilder(); 
                    for (int i = 0; i < contadores[1]; i++) {
                        sbEquipos.append(String.join(",", equipos[i])); 
                        if (i < contadores[1] - 1) sbEquipos.append(";");
                    }
                    return "|" + sbEquipos.toString();

                // ---- PROYECTOS ----
                case "CREAR_PROYECTO":
                    if (partes.length != 2) return "ERROR|Formato: CREAR_PROYECTO|nombre";
                    proyectos[contadores[2]][0] = String.valueOf(contadores[2]);
                    proyectos[contadores[2]][1] = partes[1];
                    proyectos[contadores[2]][2] = null;
                    proyectos[contadores[2]][3] = "Activo";
                    return "OK|Proyecto creado con ID: " + contadores[2]++;

                case "LISTAR_PROYECTOS":
                    if (contadores[2] == 0) return "ERROR|No hay proyectos registrados";
                    StringBuilder sbProyectos = new StringBuilder();
                    for (int i = 0; i < contadores[2]; i++) {
                        sbProyectos.append(proyectos[i][0] + "," + 
                                        proyectos[i][1] + "," + 
                                        (proyectos[i][2] == null ? "null" : proyectos[i][2]) + "," + proyectos[i][3]); // Valida que haya un parametro en [i][2]
                        if (i < contadores[2] - 1) sbProyectos.append(";");
                    }
                    return "|" + sbProyectos.toString();

                case "ASIGNAR_EQUIPO":
                    if (partes.length != 3) return "ERROR|Formato: ASIGNAR_EQUIPO|proyectoID|equipoID";
                    int proyectoID = Integer.parseInt(partes[1]);
                    int equipoID = Integer.parseInt(partes[2]);
                    
                    if (proyectoID < 0 || proyectoID >= contadores[2]) 
                        return "ERROR | ID de proyecto no válido";
                    if (equipoID < 0 || equipoID >= contadores[1]) 
                        return "ERROR | ID de equipo no válido";
                    
                    proyectos[proyectoID][2] = partes[2];
                    return "OK|Equipo asignado correctamente";

                // ---- TAREAS ----
                case "CREAR_TAREA":
                    if (partes.length != 3) return "ERROR | Formato incorrecto";
                    int proyectoIDTarea = Integer.parseInt(partes[1]);
                    
                    if (proyectoIDTarea < 0 || proyectoIDTarea >= contadores[2]) 
                        return "ERROR | ID de proyecto no válido";
                    
                    tareas[contadores[3]][0] = String.valueOf(contadores[3]);
                    tareas[contadores[3]][1] = partes[1];
                    tareas[contadores[3]][2] = partes[2];
                    tareas[contadores[3]][3] = "Pendiente";
                    tareas[contadores[3]][4] = "Sin asignar";
                    tareas[contadores[3]][5] = "0";
                    return "|Tarea creada con ID: " + contadores[3]++;

                case "LISTAR_TAREAS":
                    if (contadores[3] == 0) return "ERROR | No hay tareas registradas";
                    StringBuilder sbTareas = new StringBuilder();
                    for (int i = 0; i < contadores[3]; i++) {
                        String proyectoNombre = "ERROR | Proyecto no encontrado";
                        try {
                            int idProyecto = Integer.parseInt(tareas[i][1]);
                            proyectoNombre = proyectos[idProyecto][1];
                        } catch (Exception e) {}
                        
                        sbTareas.append(tareas[i][0] + "," + 
                                      proyectoNombre + "," + 
                                      tareas[i][2] + "," + 
                                      tareas[i][3] + "," + 
                                      tareas[i][4] + "," + 
                                      tareas[i][5]);
                        if (i < contadores[3] - 1) sbTareas.append(";");
                    }
                    return "OK|" + sbTareas.toString(); //

                case "CAMBIAR_ESTADO_TAREA":
                    if (partes.length != 3) return "ERROR | Formato incorrecto";
                    int tareaID = Integer.parseInt(partes[1]);
                    int estado = Integer.parseInt(partes[2]);
                    
                    if (tareaID < 0 || tareaID >= contadores[3]) 
                        return "ERROR|ID de tarea no válido";
                    
                    String nuevoEstado = switch (estado) {
                        case 1 -> "Pendiente";
                        case 2 -> "En progreso";
                        case 3 -> "Completado";
                        default -> tareas[tareaID][3];
                    };
                    
                    tareas[tareaID][3] = nuevoEstado;
                    return "|Estado actualizado a: " + nuevoEstado;

                // ---- REPORTES ----
                case "REPORTE_AVANCE":
                    if (partes.length != 2) return "ERROR | Formato incorrecto";
                    int proyectoIDReporte = Integer.parseInt(partes[1]);
                    
                    if (proyectoIDReporte < 0 || proyectoIDReporte >= contadores[2]) 
                        return "ERROR | ID de proyecto no válido";
                    
                    int tareasPendientes = 0;
                    int tareasProgreso = 0;
                    int tareasCompletadas = 0;
                    int totalTareas = 0;
                    
                    for (int i = 0; i < contadores[3]; i++) {
                        if (tareas[i][1] != null && Integer.parseInt(tareas[i][1]) == proyectoIDReporte) {
                            totalTareas++;
                            switch (tareas[i][3]) {
                                case "Pendiente": tareasPendientes++; break;
                                case "En progreso": tareasProgreso++; break;
                                case "Completado": tareasCompletadas++; break;
                            }
                        }
                    }
                    
                    if (totalTareas == 0) return "El proyecto no tiene tareas";
                    
                    double porcentaje = (tareasCompletadas * 100.0) / totalTareas;
                    return "|Proyecto: " + proyectos[proyectoIDReporte][1] + 
                           "|Tareas totales: " + totalTareas + 
                           "|Pendientes: " + tareasPendientes + 
                           "|En progreso: " + tareasProgreso + 
                           "|Completadas: " + tareasCompletadas + 
                           "|Porcentaje: " + String.format("%.2f", porcentaje) + "%"; //Sacamos solo 2 decimales del valor decimal que calcula

                case "REPORTE_ESTADOS":
                    if (contadores[3] == 0) return "No hay tareas registradas";
                    
                    int pendientes = 0;
                    int progreso = 0;
                    int completadas = 0;
                    
                    for (int i = 0; i < contadores[3]; i++) { // Lee arreglo y acumula las tareas dependiendo de su estado
                        switch (tareas[i][3]) {
                            case "Pendiente": pendientes++; break;
                            case "En progreso": progreso++; break;
                            case "Completado": completadas++; break;
                        }
                    }
                    
                    return "|Pendientes: " + pendientes + 
                           "|En progreso: " + progreso + 
                           "|Completadas: " + completadas + 
                           "|Total: " + contadores[3];

                case "SALIR":
                    return "Servidor sigue activo";

                default:
                    return "ERROR | Comando desconocido";
            }
        } catch (Exception e) {
            return "ERROR | Error procesando comando: " + e.getMessage();
        }
    }
}