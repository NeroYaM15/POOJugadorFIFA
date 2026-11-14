package operations;

import models.Jugador;
import models.Posicion;
import models.Equipo;
import models.Tecnico;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.time.LocalDate;

public class Operation {
    public static String generarId() {
        Set<String> usados = new HashSet<>();
        try (BufferedReader buffer = new BufferedReader(new FileReader("players.csv"))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                usados.add(line.split(",")[0]);
            }
        }
        catch (IOException e) {
        }
        Random ramdom = new Random();
        String nuevo;
        do{
            nuevo = String.valueOf(100000 + ramdom.nextInt(900000));
        }
        while (usados.contains(nuevo));
            return nuevo;
    }
    public static void savePlayer(Jugador jugadorToSave)
    {
        jugadorToSave.setId(generarId());
    try{
        FileWriter escribir = new FileWriter("players.csv", true);

        //escribir.write(jugadorToSave.playerToCSV());
        escribir.write(jugadorToSave.playerToCSV());
        escribir.write("\n");
        escribir.close();
        System.out.println("Player Saved");

    }
    catch (IOException e)
    {
        System.out.println(e.getMessage());
    }
}
    public static void readPlayers()
    {
        try(BufferedReader buffer = new BufferedReader(new FileReader("players.csv"))) {
            String playerReader;

            while ((playerReader= buffer.readLine())!=null)
            {
                if (playerReader.isBlank()){
                    continue;
                }
                String[] d = playerReader.split(",", -1);

                if (d.length < 5){
                    continue;
                }
                String posicion = (d.length > 5 && !d[5].isBlank()) ? d[5] : "SIN_POSICION";
                System.out.println("ID: " + d[0] + " | Nombre: " + d[1] + " | Dorsal: " + d[2] + " | Edad: " + d[3] + " | Vivo: " + d[4] + " | Posicion:" + (d.length > 5 ? d[5] : "SIN_POSICION"));
            }


            buffer.close();
            System.out.println("All players have been read");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void readOnePlayer(String name)
    {
        try {
            FileReader lector = new FileReader("players.csv");
            BufferedReader buffer = new BufferedReader(lector);
            String playerReader;

            while ((playerReader = buffer.readLine())!=null)
            {

               String [] data_readed = playerReader.split(",");
               Jugador temp_player = new Jugador();
               temp_player.setId(data_readed[0]);
               temp_player.name= data_readed[1];
               temp_player.dorsal = Integer.parseInt(data_readed[2]);
               temp_player.is_alive = Boolean.parseBoolean(data_readed[4]);

               if (temp_player.name.equals(name))
               {

                   System.out.println(playerReader);
                   //playerReader = null;
               }

            }


            buffer.close();
            System.out.println("All players have been read");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void UpdatePlayer(Jugador jugadorToUpdate){
        StringBuilder contenido = new StringBuilder();
        boolean encontrado = false;

        try{
            FileReader lector = new FileReader("players.csv");
            BufferedReader buffer = new BufferedReader(lector);
            String playerReader;

            while ((playerReader = buffer.readLine()) !=null){
                if(playerReader.isBlank()) continue;
                String[] data_readed = playerReader.split(",");
                if(data_readed.length<5)continue;

                if(!encontrado && data_readed[1].equalsIgnoreCase(jugadorToUpdate.name)){
                    if (data_readed.length < 6){
                        data_readed = Arrays.copyOf(data_readed,6);
                    }
                    data_readed[2] = String.valueOf(jugadorToUpdate.dorsal);
                    data_readed[4] = String.valueOf(jugadorToUpdate.is_alive);

                    if(jugadorToUpdate.posicion != null){
                        data_readed[5] = jugadorToUpdate.posicion.name();
                    } else if (data_readed[5] == null) {
                        data_readed[5] = "SIN_POSICION";

                    }
                    playerReader = String.join(",", data_readed);
                    encontrado = true;

                }
                contenido.append(playerReader).append("\n");
            }
            buffer.close();

            if(encontrado){
                FileWriter writer = new FileWriter("players.csv", false);
                writer.write(contenido.toString());
                writer.close();
                System.out.println("Player Updated");
            }
            else {
                System.out.println("Player Not Updated");
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void DeletePlayer(Jugador jugadorToDelete) {
        StringBuilder contenido = new StringBuilder();
        boolean encontrado = false;

        try {
            FileReader lector = new FileReader("players.csv");
            BufferedReader buffer = new BufferedReader(lector);
            String playerReader;

            while ((playerReader = buffer.readLine()) != null) {
                if (playerReader.isBlank()) continue;
                String[] data_readed = playerReader.split(",");
                if (data_readed.length < 5) continue;

                if (!encontrado && data_readed[1].equalsIgnoreCase(jugadorToDelete.name)) {
                    encontrado = true;
                    continue;                      // se salta la línea: queda borrada
                }

                contenido.append(playerReader).append("\n");
            }

            buffer.close();

            if (encontrado) {
                FileWriter escribir = new FileWriter("players.csv", false);
                escribir.write(contenido.toString());
                escribir.close();
                System.out.println("Player Deleted");
            } else {
                System.out.println("No se encontró un jugador con ese nombre");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static  Jugador findPlayerByName(String name) {
        try(BufferedReader buffer = new BufferedReader(new FileReader("players.csv"))) {
            String playerReader;
            while ((playerReader = buffer.readLine()) != null){
                if (playerReader.isBlank()) continue;
                String[] data_readed = playerReader.split(",");
                if (data_readed.length < 5) continue;

                if (data_readed[1].equalsIgnoreCase(name)) {
                    Jugador temp_player = new Jugador();
                    temp_player.setId(data_readed[0]);
                    temp_player.name= data_readed[1];
                    temp_player.dorsal = Integer.parseInt(data_readed[2]);
                    temp_player.is_alive = Boolean.parseBoolean(data_readed[4]);
                    if (data_readed.length > 5){
                        try {
                            temp_player.posicion = Posicion.valueOf(data_readed[5]);
                        }
                        catch (IllegalArgumentException e) {

                        }
                    }
                    return temp_player;
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static List<Equipo> loadTeams() {
        List<Equipo> equipos = new ArrayList<>();
        try (BufferedReader buffer = new BufferedReader(new FileReader("teams.csv"))) {
            String teamReader;

            while ((teamReader = buffer.readLine()) != null){
                String[] d = teamReader.split(",", -1);
                Equipo equipo = new Equipo(d[0]);

                if (d.length > 1 && !d[1].isBlank()){
                    for (String name : d[1].split(";")) {
                        Jugador jugador = findPlayerByName(name);
                        if (jugador != null) {
                            equipo.agregarJugador(jugador);
                        }
                    }
                }
                equipos.add(equipo);
            }
        }
        catch (IOException e) {

        }
        return equipos;
    }

    private static void saveTeams(List<Equipo> equipos) {
        try (FileWriter writer = new FileWriter("teams.csv", false)) {
            for (Equipo equipo : equipos) {
                List<String> names = new ArrayList<>();
                for (Jugador jugador : equipo.getPlantilla()){
                    names.add(jugador.name);
                }
                writer.write(equipo.getTeamName() + "," + String.join(";", names) + "\n");
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Equipo findTeam(List<Equipo> equipos, String teamName) {
        for (Equipo equipo : equipos){
            if (equipo.getTeamName().equalsIgnoreCase(teamName)){
                return equipo;
            }
        }
        return null;
    }

    public static void createTeam(String teamName) {
        if (teamName.isEmpty() || teamName.contains(",") || teamName.contains(";")){
            System.out.println("Error: Nombre no valido");
            return;
        }

        List<Equipo> equipos = loadTeams();
        if (findTeam(equipos,teamName) != null) {
            System.out.println("Error: Jugador existente");
            return;
        }

        equipos.add(new Equipo(teamName));
        saveTeams(equipos);
        System.out.println("Team Created");
    }

    public static void showTeams() {
        List<Equipo> equipos = loadTeams();
        if (equipos.isEmpty()){
            System.out.println("No hay equipos registrados");
            return;
        }

        for (Equipo equipo : equipos){
            System.out.println("Equipo: " + equipo.getTeamName() + " | Jugadores: " + equipo.getCantidadJugadores());
        }
    }

    public static void showTeamsRoster(String teamName) {
        Equipo equipo = findTeam(loadTeams(), teamName);
        if (equipo == null){
            System.out.println("No se encontro el equipo");
            return;
        }

        System.out.println("Plantilla de " + equipo.getTeamName() + ":");
        if (equipo.getCantidadJugadores() == 0){
            System.out.println("(Sin jugadores)");
            return;
        }
        for (Jugador jugador : equipo.getPlantilla()){
            String posicion = (jugador.posicion == null) ? "SIN_POSICION" : jugador.posicion.name();
            System.out.println("Dorsal " + jugador.dorsal + " | " + jugador.name + " | " + posicion);
        }
    }

    public static void addPlayerToTeam(String teamName, String playerName) {
        List<Equipo> equipos = loadTeams();
        Equipo equipo = findTeam(equipos,teamName);
        if (equipo == null){
            System.out.println("No se encontro el equipo");
            return;
        }

        Jugador jugador = findPlayerByName(playerName);
        if (jugador == null){
            System.out.println("No existe un jugador con ese nombre");
            return;
        }

        if (equipo.buscarJugador(jugador.name) != null){
            System.out.println("Ese jugador ya esta en la plantilla");
            return;
        }

        if (!equipo.agregarJugador(jugador)){
            System.out.println("El dorsal " + jugador.dorsal + " ya lo usa otro jugador del equipo");
            return;
        }

        saveTeams(equipos);
        System.out.println("Player added to team");

    }

    public static void removePlayerFromTeam(String teamName, String playerName) {
        List<Equipo> equipos = loadTeams();
        Equipo equipo = findTeam(equipos,teamName);
        if (equipo == null){
            System.out.println("No se encontro el equipo");
            return;
        }

        if (equipo.eliminarJugador(playerName)){
            saveTeams(equipos);
            System.out.println("Player removed from team");
        }
        else {
            System.out.println("El jugador no esta en la plantilla");
        }
    }

    private static String generarIdTecnico(){
        Set<String> usados = new HashSet<>();
        try (BufferedReader buffer = new BufferedReader(new FileReader("coaches.csv"))){
            String line;
            while ((line = buffer.readLine()) != null){
                usados.add(line.split(",")[0]);
            }
        }
        catch (IOException e){

        }
        Random random = new Random();
        String nuevo;
        do {
            nuevo = String.valueOf(100000 + random.nextInt(900000));
        }
        while (usados.contains(nuevo));
        return nuevo;
    }

    private static boolean existsTecnico(String name){
        try (BufferedReader buffer = new BufferedReader(new FileReader("coaches.csv"))){
            String tecnicoReader;
            while ((tecnicoReader = buffer.readLine()) != null){
                if (tecnicoReader.isBlank()) continue;
                String[] data_readed = tecnicoReader.split(",");
                if (data_readed.length >= 5 && data_readed[1].equalsIgnoreCase(name)){
                    return true;
                }
            }
        }
        catch (IOException e){

        }
        return false;
    }

    public static void saveTecnico(Tecnico tecnicoToSave){
        if (tecnicoToSave.name == null || tecnicoToSave.name.isBlank() || tecnicoToSave.name.contains(",")) {
            System.out.println("Error: Nombre no valido");
            return;
        }

        if (existsTecnico(tecnicoToSave.name)){
            System.out.println("Error: Jugador existente");
            return;
        }

        tecnicoToSave.setId(generarIdTecnico());
        try {
            FileWriter writer = new FileWriter("coaches.csv", true);
            writer.write(tecnicoToSave.tecnicoToCSV());
            writer.write("\n");
            writer.close();
            System.out.println("Tecnico saved");
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static void printTecnico(String[] d) {
        int edad = 0;
        try {
            edad = new Tecnico(d[1], LocalDate.parse(d[2]), d[3]).get();
        }
        catch (DateTimeException e) {

        }
        System.out.println("ID: " + d[0] + " | Nombre: " + d[1] + " | Nacimiento: " + d[2]
                + " | Edad: " + edad + " | Club: " + d[3] + " | Vivo: " + d[4]);
    }

    public static void readTecnico(){
        try (BufferedReader buffer = new BufferedReader(new FileReader("coaches.csv"))){
            String tecnicoReader;
            boolean there_are_Tecnicos = false;

            while ((tecnicoReader = buffer.readLine()) != null){
                if (tecnicoReader.isBlank()) continue;
                String[] d = tecnicoReader.split(",", -1);

                if (d.length < 5) continue;
                printTecnico(d);
                there_are_Tecnicos = true;

            }
            if  (there_are_Tecnicos){
                System.out.println("All tecnicos have bee read");
            }
            else  {
                System.out.println("Tecnicos have not been read");
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Error: No se encontro el tecnico");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void readOneTecnico(String name){
        try (BufferedReader buffer = new BufferedReader(new FileReader("coaches.csv"))){
            String tecnicoReader;
            boolean encontrado = false;

            while ((tecnicoReader = buffer.readLine()) != null){
                if (tecnicoReader.isBlank()) continue;
                String[] d = tecnicoReader.split(",", -1);
                if (d.length < 5) continue;

                if (d[0].equalsIgnoreCase(name)){
                    printTecnico(d);
                    encontrado = true;
                }
            }
            if (!encontrado){
                System.out.println("Tecnico no encontrado");
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Error: No se encontro el tecnico");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void UpdateTecnico(Tecnico tecnicoToUpdate){
        StringBuilder contenido = new StringBuilder();
        boolean encontrado = false;

        try {
            FileReader lector = new FileReader("coaches.csv");
            BufferedReader buffer = new BufferedReader(lector);
            String tecnicoReader;

            while ((tecnicoReader = buffer.readLine()) != null){
                if (tecnicoReader.isBlank()) continue;
                String[] data_readed = tecnicoReader.split(",");
                if (data_readed.length < 5) continue;

                if (!encontrado && data_readed[1].equalsIgnoreCase(tecnicoToUpdate.name)){
                    if (tecnicoToUpdate.club != null){
                        data_readed[3] = tecnicoToUpdate.club;
                    }
                    data_readed[4] = String.valueOf(tecnicoToUpdate.is_alive);
                    tecnicoReader = String.join(",", data_readed);
                    encontrado = true;
                }
                contenido.append(tecnicoReader).append("\n");
            }
            buffer.close();

            if (encontrado){
                FileWriter writer = new FileWriter("coaches.csv", false);
                writer.write(contenido.toString());
                writer.close();
                System.out.println("Tecnico Updated");
            }
            else  {
                System.out.println("Tecnico Not Updated");
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    public static void deleteTecnico(Tecnico tecnicoToDelete){
        StringBuilder contenido = new StringBuilder();
        boolean encontrado = false;

        try {
            FileReader lector = new FileReader("coaches.csv");
            BufferedReader buffer = new BufferedReader(lector);
            String tecnicoReader;

            while ((tecnicoReader = buffer.readLine()) != null){
                if (tecnicoReader.isBlank()) continue;
                String[] data_readed = tecnicoReader.split(",");
                if (data_readed.length < 5) continue;

                if (!encontrado && data_readed[1].equalsIgnoreCase(tecnicoToDelete.name)){
                    encontrado = true;
                    continue;
                }
                contenido.append(tecnicoReader).append("\n");
            }
            buffer.close();
            if (encontrado){
                FileWriter writer = new FileWriter("coaches.csv", false);
                writer.write(contenido.toString());
                writer.close();
                System.out.println("Tecnico Deleted");
            }
            else {
                System.out.println("Tecnico Not Deleted");
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}


















