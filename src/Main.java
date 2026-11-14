import jdk.swing.interop.SwingInterOpUtils;
import models.Jugador;
import models.Posicion;
import models.Tecnico;
import operations.Operation;

import java.time.LocalDate;
import java.util.Scanner;

import static operations.Operation.*;

public class Main {
    private static final Scanner leer = new Scanner(System.in);
    public static void main(String[] args) {

        //models.Jugador newPlayer = new models.Jugador("Pele", LocalDate.of(1940, 10,23));
        //newPlayer.is_alive = false;
        //newPlayer.dorsal=10;
        //TODO crear menu para usar y operar las acciones de los jugadores

        Scanner leer = new Scanner(System.in);


        //Menu
        //Ingresar jugador -> pulsa 1
        //Leer todos jugadores -> pulsa 2
        //Buscar jugador -> pulsa 3


        int opcion;
        do {
            System.out.println("\n *********** CRUD FIFAS *********** ");
            System.out.println("1. Jugadores");
            System.out.println("2. Tecnicos");
            System.out.println("3. Equipos");
            System.out.println("0. Salir destamonda");
            System.out.println("*********** CRUD FIFAS *********** ");

            opcion = leer.nextInt();
            leer.nextLine();

            switch (opcion) {
                case 1:
                    MenuJugadores();
                    break;
                case 2:
                    MenuTecnicos();
                    break;
                case 3:
                    MenuEquipos();
                    break;
                case 0:
                    System.out.println("Nos vemos");
                    break;
                default:
                    System.out.println("Abrite burro");


            }
        }
        while (opcion!=0);

    }

    public static void MenuJugadores(){
        int opcion;

        do {
            System.out.println("\n *********** CRUD FIFAS *********** ");
            System.out.println("1. Crear Jugador");
            System.out.println("2. Buscar Jugador");
            System.out.println("3. Mostrar Jugador");
            System.out.println("4. Actualizar Jugador");
            System.out.println("5. Eliminar Jugador");
            System.out.println("0. Nos vidrios");
            System.out.println("*********** CRUD FIFAS *********** ");

            opcion = leer.nextInt();
            leer.nextLine();

            switch (opcion) {
                case 1:
                    newPlayer();
                    break;

                case 2:
                    findPlayer();
                    break;

                case 3:
                    showAllPlayers();
                    break;

                case 4:
                    UpdatePlayer();
                    break;

                case 5:
                    DeletePlayer();
                    break;

                case 0:
                    System.out.println("Nos vidrios");
                    break;

                default:
                    System.out.println("Abrite burro");

            }

        }
        while (opcion!=0);
    }

    public static void MenuTecnicos(){
        int opcion;

        do {
            System.out.println("\n *********** CRUD FIFAS *********** ");
            System.out.println("1. Crear técnico");
            System.out.println("2. Buscar técnico");
            System.out.println("3. Mostrar técnicos");
            System.out.println("4. Actualizar técnico");
            System.out.println("5. Eliminar técnico");
            System.out.println("0. Volver al menú principal");
            System.out.println("*********** CRUD FIFAS *********** ");

            opcion = leer.nextInt();
            leer.nextLine();

            switch (opcion) {

                case 1:
                    newTecnico();
                    break;

                case 2:
                    findTecnico();

                case 3:
                    showAllTecnico();

                case 4:
                    UpdateTecnico();
                    break;

                case 5:
                    DeleteTecnico();
                    break;

                case 0:
                    break;
                default:
                    System.out.println("Abrite burro");

            }
        }

        while (opcion!=0);
    }

    public static void MenuEquipos(){
        int opcion;
        do {
            System.out.println("\n *********** CRUD FIFAS *********** ");
            System.out.println("1. Crear equipo");
            System.out.println("2. Mostrar equipo");
            System.out.println("3. Ver plantilla de un equipo");
            System.out.println("4. Agregar jugador a la plantilla");
            System.out.println("5. Eliminar jugador de la plantilla");
            System.out.println("0. Nos vidrios");
            System.out.println("*********** CRUD FIFAS *********** ");

            opcion = leer.nextInt();

            switch (opcion) {
                case 1:
                    createTeam();
                    break;

                case 2:
                    showTeams();
                    break;

                case 3:
                    showTeamsRoster();
                    break;

                case 4:
                    addPlayerToTeam();
                    break;

                case 5:
                    removePlayerFromTeam();
                    break;

                case 0:
                    break;
                default:
                    System.out.println("Abrite burro");

            }
        }
        while (opcion != 0);
    }

    private static void UpdatePlayer() {
        Scanner leer = new Scanner(System.in);
        System.out.println("Ingrese el nombre del jugador que quiere actualizar");
        String name = leer.nextLine();
        System.out.println("Ingrese el nuevo dorsal");
        int dorsal = leer.nextInt();
        System.out.println("¿El jugador esta vivo? (s/n)");
        String vivo = leer.next();

        Posicion[] posiciones = Posicion.values();
        int opcionPosicion;

        do {
            System.out.println("Seleccione la nueva posicion");
            for(int i = 0; i < posiciones.length; i++){
                System.out.println((i +1) + ". "  + posiciones[i]);
            }
            System.out.println("0. Dejar la misma posicion");
            opcionPosicion = leer.nextInt();
            if(opcionPosicion < 0 || opcionPosicion > posiciones.length){
                System.out.println("Opcion no valida");
            }
        }
        while (opcionPosicion < 0 || opcionPosicion > posiciones.length);

        Jugador jugador = new Jugador();
        jugador.name=name;
        jugador.dorsal=dorsal;
        jugador.is_alive = vivo.equalsIgnoreCase("s");
        if(opcionPosicion > 0){
            jugador.posicion = posiciones[opcionPosicion-1];
        }
        Operation.UpdatePlayer(jugador);
    }

    private static void DeletePlayer() {
        System.out.println("Ingrese el nombre del jugador que quiere eliminar");

        String name = leer.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }

        System.out.println("¿Seguro que desea eliminar a "
                + name + "? (s/n)");

        String confirmacion = leer.nextLine().trim();

        if (confirmacion.equalsIgnoreCase("s")) {
            Jugador jugador = new Jugador();
            jugador.name = name;
            operations.Operation.DeletePlayer(jugador);
        } else {
            System.out.println("CANCELADO");
        }
    }

    public static void newPlayer() {
        Scanner leer = new Scanner(System.in);
        System.out.println("Ingrese el nombre del jugador");
        String name_player = leer.nextLine();
        System.out.println("Ingrese año de nacimiento");
        int year = leer.nextInt();
        System.out.println("Ingrese mes de naciemiento");
        int month = leer.nextInt();
        System.out.println("Ingrese día de nacimiento");
        int day = leer.nextInt();
        System.out.println("Ingrese el dorsal");
        int dorsal = leer.nextInt();
        Posicion[] posiciones = Posicion.values();
        int opcionPosicion;

        do {
            System.out.println("Ingrese el posicion del Jugador");

            for(int i = 0; i < posiciones.length; i++){
                System.out.println((i + 1) + ". " + posiciones[i]);
            }
            opcionPosicion = leer.nextInt();

            if(opcionPosicion< 1 || opcionPosicion > posiciones.length){
                System.out.println("Opcion no valida");
            }
        }
        while (opcionPosicion<1 || opcionPosicion>posiciones.length);
        Posicion posicion = posiciones[opcionPosicion - 1];

        savePlayer(new Jugador(name_player, LocalDate.of(year, month, day),dorsal,posicion));
    }

    public static void findPlayer() {
        Scanner leer = new Scanner(System.in);
        System.out.println("Ingrese el jugador a buscar");
        readOnePlayer(leer.next());

    }

    public static void showAllPlayers() {
        readPlayers();

    }

    public static void createTeam() {
        Scanner leer = new Scanner(System.in);
        System.out.println("Ingrese el nombre del equipo");
        Operation.createTeam(leer.nextLine());
    }

    public static void showTeamsRoster() {
        Scanner leer = new Scanner(System.in);
        System.out.println("Ingrese el nombre del equipo");
        Operation.showTeamsRoster(leer.nextLine());
    }

    public static void addPlayerToTeam() {
        Scanner leer = new Scanner(System.in);
        System.out.println("Ingrese el nombre del equipo");
        String team_name = leer.nextLine();
        System.out.println("Ingrese el nombre del jugador que desea agregar");
        String player_name = leer.nextLine();
        Operation.addPlayerToTeam(team_name, player_name);
    }

    public static void removePlayerFromTeam() {
        Scanner leer = new Scanner(System.in);
        System.out.println("Ingrese el nombre del equipo");
        String team_name = leer.nextLine();
        System.out.println("Ingrese el nombre del jugador que desea eliminar");
        String player_name = leer.nextLine();
        Operation.removePlayerFromTeam(team_name, player_name);
    }

    public static void newTecnico(){
        System.out.println("Ingrese el nombre del tecnico");
        String name = leer.nextLine();
        System.out.println("Ingrese año de nacimiento");
        int year = leer.nextInt();
        System.out.println("Ingrese mes de naciemiento");
        int month = leer.nextInt();
        System.out.println("Ingrese dia de nacimiento");
        int day = leer.nextInt();
        leer.nextLine();

    }

    public static void findTecnico() {
        System.out.println("Ingrese el nombre del tecnico a buscar");
        readOneTecnico(leer.nextLine().trim());
    }

    public static void showAllTecnico() {
        readTecnico();
    }

    private static void UpdateTecnico() {
        System.out.println("Ingrese el nombre del tecnico");
        String name = leer.nextLine();
        System.out.println("¿El tecnico esta vivo? (s/n)");
        String vivo = leer.nextLine();

        Tecnico tecnico = new Tecnico();
        tecnico.name = name.trim();
        tecnico.is_alive = vivo.equalsIgnoreCase("s");

        Operation.UpdateTecnico(tecnico);
    }

    private static void DeleteTecnico() {
        System.out.println("Ingrese el nombre del tecnico a eliminar");
        String name = leer.nextLine();
         if (name.isEmpty()){
             System.out.println("El nombre del tecnico no puede ser vacio");
             return;
         }

        System.out.println("¿Seguro que desea eliminar a " + name + "? (s/n)");
         if (leer.nextLine().equalsIgnoreCase("s")) {
             Tecnico tecnico = new Tecnico();
             tecnico.name =name;
             Operation.deleteTecnico(tecnico);
         }
         else {
             System.out.println("CANCELADO");
         }
    }

}
