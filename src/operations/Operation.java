package operations;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;

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
    public static void readPlayers() {
        List<Equipo> equipos = loadTeams();

        boolean encontrado = false;

        try (BufferedReader buffer = new BufferedReader(
                new FileReader("players.csv"))) {

            String playerReader;

            while ((playerReader = buffer.readLine()) != null) {

                if (playerReader.isBlank()) {
                    continue;
                }

                String[] d = playerReader.split(",", -1);

                if (d.length < 5) {
                    continue;
                }

                String posicion = d.length > 5 ? d[5] : "SIN_POSICION";
                String nombreEquipo = "SIN_EQUIPO";

                // Buscar el equipo al que pertenece el jugador
                for (Equipo equipo : equipos) {

                    for (Jugador jugador : equipo.getPlantilla()) {

                        if (jugador.name.equalsIgnoreCase(d[1].trim())) {
                            nombreEquipo = equipo.getTeamName();
                            break;
                        }
                    }

                    if (!nombreEquipo.equals("SIN_EQUIPO")) {
                        break;
                    }
                }

                System.out.println("ID: " + d[0] + " | Nombre: " + d[1] + " | Dorsal: " + d[2] + " | Edad: " + d[3] + " | Vivo: " + d[4] + " | Posición: " + posicion + " | Equipo: " + nombreEquipo);

                encontrado = true;
            }

            if (encontrado) {
                System.out.println("All players have been read");
            } else {
                System.out.println("No players found");
            }

        } catch (Exception e) {
            System.out.println("Error al leer jugadores: " + e.getMessage());
        }
    }
    public static void readOnePlayer(String name)
    {
        try (BufferedReader buffer = new BufferedReader(new FileReader("players.csv"))){
            String playerReader;
            boolean encontrado = false;
            List<Equipo> equipos = loadTeams();

            while ((playerReader = buffer.readLine())!=null) {
                if (playerReader.isBlank()){
                    continue;
                }
               String [] d = playerReader.split(",", -1);

                if (d.length < 5){
                    continue;
                }

                if (d[1].trim().equalsIgnoreCase(name.trim())){
                    mostrarJugador(d, findPlayerTeam(equipos, d[1]));
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado){
                System.out.println("Jugador no encontrado");
            }
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

    private static Tecnico findTecnicoByName(String name) {
        try (BufferedReader buffer = new BufferedReader(new FileReader("coaches.csv"))){
            String line;

            while ((line = buffer.readLine()) != null){
                if (line.isBlank()){
                    continue;
                }
                String[] d = line.split(",", -1);

                if (d.length < 5){
                    continue;
                }

                if (d[1].trim().equalsIgnoreCase(name.trim())){
                    Tecnico tecnico = new Tecnico(d[1], LocalDate.parse(d[2]), d[3]);

                    tecnico.setId(d[0]);
                    tecnico.is_alive = Boolean.parseBoolean(d[4]);
                    return tecnico;
                }
            }
        }
        catch (IOException | DateTimeException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static List<Equipo> loadTeams() {
        List<Equipo> equipos = new ArrayList<>();
        try (BufferedReader buffer = new BufferedReader(new FileReader("teams.csv"))) {
            String teamReader;

            while ((teamReader = buffer.readLine()) != null){

                if (teamReader.isBlank()){
                    continue;
                }
                String[] d = teamReader.split(",", -1);

                if (d.length < 2){
                    continue;
                }
                String id = d[0].trim();
                String name = d[1].trim();

                Equipo equipo = new Equipo(id, name);

                if (d.length > 2 && !d[2].isBlank()){

                    for (String player_name: d[2].split(";")){
                        Jugador jugador = findPlayerByName(player_name.trim());

                        if (jugador != null){
                            equipo.agregarJugador(jugador);
                        }
                    }
                }
                if (d.length > 3 && !d[3].isBlank()){
                    Tecnico tecnico = findTecnicoByName(d[3].trim());

                    if (tecnico != null){
                        equipo.setTecnico(tecnico);
                    }
                }
                equipos.add(equipo);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return equipos;
    }

    private static void saveTeams(List<Equipo> equipos) {
        try (FileWriter writer = new FileWriter("teams.csv", false)) {
            for (Equipo equipo : equipos) {
                List<String> players_name = new ArrayList<>();
                for (Jugador jugador : equipo.getPlantilla()){
                    players_name.add(jugador.name);
                }
                String tecnico_name = equipo.getTecnico() == null ? "" : equipo.getTecnico().name;

                writer.write(equipo.getId() + "," + equipo.getTeamName() + "," + String.join(";", players_name) + "," + tecnico_name + "\n");
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Equipo findTeam(List<Equipo> equipos, String teamName) {
        for (Equipo equipo : equipos){
            if (equipo.getTeamName().equalsIgnoreCase(teamName.trim()) || equipo.getId().equalsIgnoreCase(teamName.trim())){
                return equipo;
            }
        }
        return null;
    }

    private static boolean UpdateTecnicoTeam(String tecnicoName, String newClub) {
        List<String> lines = new ArrayList<>();
        boolean encontrado = false;

        try (BufferedReader buffer = new BufferedReader(new FileReader("coaches.csv"))){
            String linea;

            while ((linea = buffer.readLine()) != null) {

                if (linea.isBlank()) {
                    continue;
                }

                String[] d = linea.split(",", -1);

                if (d.length < 4) {
                    lines.add(linea);
                    continue;
                }

                if (d[1].trim().equalsIgnoreCase(tecnicoName.trim())) {

                    if (d.length < 5) {
                        d = Arrays.copyOf(d, 5);
                        d[4] = "true";
                    }

                    d[3] = newClub;
                    linea = String.join(",", d);
                    encontrado = true;
                }

                lines.add(linea);
            }

        } catch (IOException e) {
            System.out.println("Error al leer coaches.csv: " + e.getMessage());
            return false;
        }

        if (!encontrado) {
            System.out.println("No se encontró el técnico en coaches.csv");
            return false;
        }

        try (FileWriter writer = new FileWriter("coaches.csv", false)) {

            for (String linea : lines) {
                writer.write(linea + "\n");
            }

            return true;

        } catch (IOException e) {
            System.out.println("Error al actualizar club: " + e.getMessage());
            return false;
        }
    }

    public static String generarIdEquipo() {
        Random random = new Random();
        Set<String> ids = new HashSet<>();

        try (BufferedReader buffer = new BufferedReader(new FileReader("teams.csv"))){
            String line;

            while ((line = buffer.readLine()) != null){
                if (line.isBlank()){
                    continue;
                }
                String[] d = line.split(",", -1);

                if (d.length > 0 && !d[0].isBlank() && !d[0].equalsIgnoreCase("null")){
                    ids.add(d[0]);
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        String Id;

        do {
            Id = String.valueOf(100000 + random.nextInt(900000));
        }
        while (ids.contains(Id));
        return Id;
    }

    public static void createTeam(String teamName) {
        List<Equipo> equipos = loadTeams();

        if (findTeam(equipos,teamName) != null) {
            System.out.println("El team existe en el sistema");
            return;
        }
        String id = generarIdEquipo();
        Equipo equipo = new Equipo(id, teamName);
        equipos.add(equipo);
        saveTeams(equipos);

        System.out.println("El team se ha agregado correctamente");
        System.out.println("ID del equipo: " + equipo.getId());
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

        Equipo equipo = findTeam(equipos, teamName);

        if (equipo == null) {
            System.out.println("No se encontró el equipo");
            return;
        }

        Jugador jugador = findPlayerByName(playerName);

        if (jugador == null) {
            System.out.println("No existe un jugador con ese nombre");
            return;
        }

        if (equipo.buscarJugador(jugador.name) != null) {
            System.out.println("Ese jugador ya está en la plantilla");
            return;
        }

        if (!equipo.agregarJugador(jugador)) {
            System.out.println("El dorsal " + jugador.dorsal
                    + " ya lo usa otro jugador del equipo");
            return;
        }

        saveTeams(equipos);

        System.out.println("Jugador agregado correctamente al equipo");
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

    public static void AssignTecnicoToTeam(String teamName, String tecnicoName) {
        List<Equipo> equipos = loadTeams();

        Equipo equipo = findTeam(equipos, teamName);

        if (equipo == null) {
            System.out.println("No se encontró el equipo");
            return;
        }

        if (equipo.getTecnico() != null) {
            System.out.println("El equipo ya tiene un técnico asignado");
            return;
        }

        Tecnico tecnico = findTecnicoByName(tecnicoName);

        if (tecnico == null) {
            System.out.println("No existe un técnico con ese nombre");
            return;
        }

        for (Equipo otro : equipos) {

            if (otro.getTecnico() != null
                    && otro.getTecnico().name.equalsIgnoreCase(tecnicoName.trim())) {

                System.out.println("El técnico ya pertenece a otro equipo");
                return;
            }
        }

        // Actualizar club en coaches.csv
        if (!UpdateTecnicoTeam(tecnicoName, equipo.getTeamName())) {
            return;
        }

        // Asignar técnico al equipo
        equipo.setTecnico(tecnico);

        // Guardar equipo y técnico en teams.csv
        saveTeams(equipos);

        System.out.println("Técnico asignado correctamente al equipo");
    }

    public static void removeTecnicoFromTeam(String teamName, String tecnicoName) {
        List<Equipo> equipos = loadTeams();

        Equipo equipo = findTeam(equipos, teamName);

        if (equipo == null) {
            System.out.println("No se encontró el equipo");
            return;
        }

        if (equipo.getTecnico() == null) {
            System.out.println("El equipo no tiene un técnico asignado");
            return;
        }

        if (!equipo.getTecnico().name.equalsIgnoreCase(tecnicoName.trim())) {
            System.out.println("El técnico no está asignado a este equipo");
            return;
        }

        if (!UpdateTecnicoTeam(tecnicoName, "SIN_CLUB")) {
            return;
        }

        equipo.setTecnico(null);

        saveTeams(equipos);

        System.out.println("Técnico retirado correctamente del equipo");
    }

    private static String generarIdTecnico(){
        Set<String> usados = new HashSet<>();
        try (BufferedReader buffer = new BufferedReader(new FileReader("coaches.csv"))){
            String line;
            while ((line = buffer.readLine()) != null){
                usados.add(line.split(",", -1)[0]);
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
                if (tecnicoReader.isBlank()){
                    continue;
                }
                String[] d = tecnicoReader.split(",", -1);
                if (d.length >= 4 && d[1].equalsIgnoreCase(name)){
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

        if (tecnicoToSave.getNacimiento() == null){
            System.out.println("Error: Fecha de nacimiento no valido");
            return;
        }

        if (existsTecnico(tecnicoToSave.name)){
            System.out.println("Error: El tecnico ya existe");
            return;
        }
        tecnicoToSave.setId(generarIdTecnico());

        try (FileWriter writer = new FileWriter("coaches.csv", true)){
            writer.write(tecnicoToSave.tecnicoToCSV());
            writer.write("\n");
            System.out.println("Tecnico saved");
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private  static void printTecnico(String[] d){
        try {
            Tecnico tecnico = new Tecnico(d[1],LocalDate.parse(d[2]),d[3]);
            tecnico.setId(d[0]);

            if (d.length > 4){
                tecnico.is_alive = Boolean.parseBoolean(d[4]);
            }
            System.out.println("ID: " + tecnico.getId() + " | Nombre: " + tecnico.name + " | Edad: " + tecnico.getEdad() + " | Club: " + tecnico.club + " | Vivo: " + tecnico.is_alive);
        }
        catch (DateTimeException e){
            System.out.println("Error: Fecha de nacimiento no valido");
        }
    }

    public static void readTecnico(){
        try (BufferedReader buffer = new BufferedReader(new FileReader("coaches.csv"))){
            String tecnicoReader;
            boolean encontado = false;

            while ((tecnicoReader = buffer.readLine()) != null){
                if (tecnicoReader.isBlank()){
                    continue;
                }
                String[] d = tecnicoReader.split(",", -1);

                if (d.length < 5){
                    continue;
                }
                printTecnico(d);
                encontado = true;

            }
            if  (encontado){
                System.out.println("All tecnicos have been read");
            }
            else  {
                System.out.println("Tecnicos have not been read");
            }
        }
        catch (Exception e){
            System.out.println("Error al leer técnicos: " + e.getMessage());
        }
    }

    public static void readOneTecnico(String name){
        try (BufferedReader buffer = new BufferedReader(new FileReader("coaches.csv"))){
            String tecnicoReader;
            boolean encontrado = false;

            while ((tecnicoReader = buffer.readLine()) != null){
                if (tecnicoReader.isBlank()){
                    continue;
                }
                String[] d = tecnicoReader.split(",", -1);
                if (d.length < 5){
                    continue;
                }

                if (d[1].equalsIgnoreCase(name)){
                    printTecnico(d);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado){
                System.out.println("Tecnico no encontrado");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void UpdateTecnico(Tecnico tecnicoToUpdate){
        StringBuilder contenido = new StringBuilder();
        boolean encontrado = false;

        try (BufferedReader buffer = new BufferedReader(new FileReader("coaches.csv"))){
            String tecnicoReader;

            while ((tecnicoReader = buffer.readLine()) != null){
                if (tecnicoReader.isBlank()){
                    continue;
                }
                String[] d = tecnicoReader.split(",", -1);
                if (d.length < 4){
                    continue;
                }

                if (!encontrado && d[1].trim().equalsIgnoreCase(tecnicoToUpdate.name.trim())){
                    if (d.length < 5){
                        d = Arrays.copyOf(d, 5);
                        d[4] = "true";
                    }

                    if (tecnicoToUpdate.club != null && !tecnicoToUpdate.club.isBlank()){
                        d[3] = tecnicoToUpdate.club;
                    }
                    d[4] = String.valueOf(tecnicoToUpdate.is_alive);
                    tecnicoReader = String.join(",", d);
                    encontrado = true;
                }
                contenido.append(tecnicoReader).append("\n");
            }

            if (encontrado){
                try (FileWriter writer = new FileWriter("coaches.csv", false)){
                    writer.write(contenido.toString());
                }
                System.out.println("Tecnico updated");
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

        try (BufferedReader buffer = new BufferedReader(new FileReader("coaches.csv"))){
            String tecnicoReader;

            while ((tecnicoReader = buffer.readLine()) != null){
                if (tecnicoReader.isBlank()){
                    continue;
                }
                String[] d = tecnicoReader.split(",", -1);
                if (d.length < 4){
                    continue;
                }

                if (!encontrado && d[1].trim().equalsIgnoreCase(tecnicoToDelete.name.trim())){
                    encontrado = true;
                    continue;
                }
                contenido.append(tecnicoReader).append("\n");
            }

            if (encontrado) {
                try (FileWriter writer = new FileWriter("coaches.csv", false)) {
                    writer.write(contenido.toString());
                }
                System.out.println("Tecnico deleted");
            }
            else {
                System.out.println("Tecnico Not Deleted");
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static void mostrarJugador(String[] d, String equipo){
        String posicion = d.length > 5 && !d[5].isBlank() ? d[5] : "SIN_POSICION";

        System.out.println("ID: " + d[0] + " | Nombre: " + d[1] + " | Dorsal: " + d[2] + " | Edad: " + d[3] + " | Vivo: " + d[4] + " | Posición: " + posicion + " | Equipo: " + equipo);
    }

    private static String findPlayerTeam( List<Equipo> equipos, String playerName){
        for (Equipo equipo : equipos){
            if (equipo.buscarJugador(playerName) != null){
                return equipo.getTeamName();
            }
        }
        return "SIN_EQUIPO";
    }
}


















