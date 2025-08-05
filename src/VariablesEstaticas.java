public class VariablesEstaticas {
    // En esta clase se guardaran todos las variables que nesesitan ser globales
    //Creando vectores para guardar informacion

    // vector para guardar ids de los usuarios

    // 0. ID - 1. Nombre - 2. Contrasena 
    static String[][] usuarios = new String[100][3];

    // 0. ID - 1. Nombre del Equipo 
    static String[][] equipos = new String[100][2];

    // 0. ID - 1.Nombre del proyecto - 2. IDs de miembros)
    static String[] proyectos = new String[100];

    //0. ID - 1. Titulo de la tarea - 2.ID proyecto - 3.ID usuario de la tarea - 4. Estado (Hacer/Proceso/Hecho), 5. modulo?
    static String[][] tareas = new String[500][6];

    // vector para guardar nombres de los proyectos
    static String[][] nombres_proyectos = new String[100][5];


    //static String[][] reportes = new String[500][]

    //variable para hacer IDs dinamicos
    static int ID_contador = -1;


}
