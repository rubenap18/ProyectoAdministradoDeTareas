import java.util.Scanner;

public class MetodosMenuPrincipal {
    //Metodo para acceder a un perfil
    public static void AccederPerfil(){
        Scanner entradaAcceso = new Scanner(System.in);
        entradaAcceso.close();
    }

    //Metodo para crear un perfi;
    public static void CrearPerfil(String nombre){

        VariablesEstaticas.contador_IDs += 1;
        Scanner entradaCreacion = new Scanner(System.in);

        entradaCreacion.close();

    }

}
