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
        String[] partes = comando.split("\\|"); // Extrae el primer elemento del arreglo que corresponde al comando
        String tipoComando = partes[0];               // y los separa con el simbolo |

        try {
            switch (tipoComando) {
                // ---- PERFILES ---- 
                case "CREAR_PERFIL":
                    if (partes.length != 3) return "ERROR | FORMATO INCORRECTO"; // Debe ser 3 porque matriz tiene 3 espacios (accion, nombre, rol)
                    perfiles[contadores[0]][0] = String.valueOf(contadores[0]); // Convertir valor a String
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
                    equipos[contadores[1]][0] = String.valueOf(contadores[1]); // Convertir el valor int a String del contador
                    equipos[contadores[1]][1] = partes[1];
                    return "Equipo creado con ID: " + contadores[1]++;

                case "LISTAR_EQUIPOS":
                    if (contadores[1] == 0) return "ERROR | No hay equipos registrados";
                    StringBuilder sbEquipos = new StringBuilder(); 

                    for (int i = 0; i < contadores[1]; i++) {
                        sbEquipos.append(String.join(",", equipos[i])); // Formatea la lista antes de enviarla al cliente
                        if (i < contadores[1] - 1) sbEquipos.append(";");     // y reutilizar el arreglo de nuevo
                    }
                    return "|" + sbEquipos.toString();

                // ---- PROYECTOS ----
                case "CREAR_PROYECTO":
                    if (partes.length != 2) return "ERROR | Formato: CREAR_PROYECTO|nombre"; // Valida que sea introducido correctamente (2 elementos)
                    proyectos[contadores[2]][0] = String.valueOf(contadores[2]); // Convierte el valor digitado a String
                    proyectos[contadores[2]][1] = partes[1]; // Nombre del proyecto
                    proyectos[contadores[2]][2] = null; // Equipo asignado que al inicio es null
                    proyectos[contadores[2]][3] = "Activo"; // Estado inicial
                    return "OK|Proyecto creado con ID: " + contadores[2]++;

                case "LISTAR_PROYECTOS":
                    if (contadores[2] == 0) return "ERROR|No hay proyectos registrados";
                    StringBuilder sbProyectos = new StringBuilder(); // Esto sirve para 

                    for (int i = 0; i < contadores[2]; i++) {  // Bucle para buscar dentro del arreglo los datos del proyecto
                        sbProyectos.append(proyectos[i][0] + "," + // Concatenar varios String
                                        proyectos[i][1] + "," + // Ubicacion del nombre del proyecto
                                        (proyectos[i][2] == null ? "null" : proyectos[i][2]) + "," + proyectos[i][3]); // Valida que haya un parametro en [i][2]
                        if (i < contadores[2] - 1) sbProyectos.append(";");
                    }
                    return "|" + sbProyectos.toString(); // Muestra los datos creados convertidos a String

                case "ASIGNAR_EQUIPO":
                    if (partes.length != 3) return "ERROR|Formato: ASIGNAR_EQUIPO|proyectoID|equipoID";
                    int proyectoID = Integer.parseInt(partes[1]); // Convierte de String a int
                    int equipoID = Integer.parseInt(partes[2]); // Convierte de String a int
                    
                    if (proyectoID < 0 || proyectoID >= contadores[2]) // Valida si se introdujeron los datos necesarios
                        return "ERROR | ID de proyecto no válido";
                    if (equipoID < 0 || equipoID >= contadores[1]) 
                        return "ERROR | ID de equipo no válido";
                    
                    proyectos[proyectoID][2] = partes[2];
                    return "OK|Equipo asignado correctamente";

                // ---- TAREAS ----
                case "CREAR_TAREA":

                
                    if (partes.length != 3) return "ERROR | Formato incorrecto"; // Valida que el formato sea correctp (3 elementos)
                    int proyectoIDTarea = Integer.parseInt(partes[1]);
                    
                    if (proyectoIDTarea < 0 || proyectoIDTarea >= contadores[2]) // Valida el ID del proyecto
                        return "ERROR | ID de proyecto no válido";
                    
                     // Crea nuevas tareas con los valores por defecto
                    tareas[contadores[3]][0] = String.valueOf(contadores[3]);
                    tareas[contadores[3]][1] = partes[1];
                    tareas[contadores[3]][2] = partes[2];
                    tareas[contadores[3]][3] = "Pendiente";
                    tareas[contadores[3]][4] = "Sin asignar";
                    tareas[contadores[3]][5] = "0";
                    return "| Tarea creada con ID: " + contadores[3]++;

                    //
                case "LISTAR_TAREAS":
                    if (contadores[3] == 0) return "ERROR | No hay tareas registradas";
                    StringBuilder sbTareas = new StringBuilder(); // Concatena cadenas de texto

                    for (int i = 0; i < contadores[3]; i++) {
                        String proyectoNombre = "ERROR | Proyecto no encontrado";

                        // Obtener ID y nombre del proyecto desde el cliente
                        try {
                            int idProyecto = Integer.parseInt(tareas[i][1]); // Convierte String obtenido de ID a int
                            proyectoNombre = proyectos[idProyecto][1]; // Obtiene el nombre del proyecto
                        }catch(Exception e) {}
                        
                        //  Almacena los datos de la tarea en su espacio correspondiente
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
                    if (partes.length != 3) return "ERROR | Formato incorrecto"; // Valida el formato
                    int tareaID = Integer.parseInt(partes[1]); // Convertir de String a int para almacenarlo de nuevo
                    int estado = Integer.parseInt(partes[2]);
                    
                    if (tareaID < 0 || tareaID >= contadores[3]) // Valida que el ID sea correcto
                        return "ERROR|ID de tarea no válido";
                    
                    String nuevoEstado = switch (estado) { // Cambia el estado de la tarea si se desea
                        case 1 -> "Pendiente";
                        case 2 -> "En progreso";
                        case 3 -> "Completado";
                        default -> tareas[tareaID][3];
                    };
                    
                    tareas[tareaID][3] = nuevoEstado;
                    return "|Estado actualizado a: " + nuevoEstado;

                // ---- REPORTES ----
                case "REPORTE_AVANCE":
                    if (partes.length != 2) return "ERROR | Formato incorrecto"; // Valida que el formato sea correcto
                    int proyectoIDReporte = Integer.parseInt(partes[1]);
                    
                    if (proyectoIDReporte < 0 || proyectoIDReporte >= contadores[2]) 
                        return "ERROR | ID de proyecto no válido";
                    
                    int tareasPendientes = 0;
                    int tareasProgreso = 0;
                    int tareasCompletadas = 0;
                    int totalTareas = 0;
                    
                    for (int i = 0; i < contadores[3]; i++){ // Actualiza el progreso de una tarea 

                        // Comprueba la existencia de una tarea o el avance
                        if (tareas[i][1] != null && Integer.parseInt(tareas[i][1]) == proyectoIDReporte){ 

                            totalTareas++;

                            switch (tareas[i][3]) { // Entra a la ubicacion de los estados de las tareas y los va contando
                                case "Pendiente": tareasPendientes++; break;
                                case "En progreso": tareasProgreso++; break;
                                case "Completado": tareasCompletadas++; break;
                            }
                        }
                    }
                    
                    if (totalTareas == 0) return "El proyecto no tiene tareas";
                    
                    double porcentaje = (tareasCompletadas * 100.0) / totalTareas; // Calculo del porcentaje de avance
                    return "|Proyecto: " + proyectos[proyectoIDReporte][1] + 
                           "|Tareas totales: " + totalTareas + 
                           "|Pendientes: " + tareasPendientes + 
                           "|En progreso: " + tareasProgreso + 
                           "|Completadas: " + tareasCompletadas + 
                           "|Porcentaje: " + String.format("%.2f", porcentaje) + "%"; //Sacamos solo 2 decimales del valor decimal que calcula

                case "REPORTE_ESTADOS":
                    if (contadores[3] == 0) return "No hay tareas registradas";
                    
                    // Inicia contadores para cada estado de tareas y mostrarlos luego
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