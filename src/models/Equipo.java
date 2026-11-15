package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Equipo {
    private String id;
    private Tecnico tecnico_equipo;
    private String teamName;
    private List<Jugador> plantilla = new ArrayList<>();

    public Equipo(String teamName) {
        this.teamName = teamName;
    }

    public Equipo(String id, String teamName) {
        this.id = id;
        this.teamName = teamName;
    }

    public Equipo(String teamName, Tecnico tecnico_equipo) {
        this.teamName = teamName;
        setTecnico(tecnico_equipo);
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        if (teamName == null || teamName.isBlank()) {
            return;
        }

        this.teamName = teamName;

        if (tecnico_equipo != null) {
            tecnico_equipo.club = this.teamName;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tecnico getTecnico() {
        return tecnico_equipo;
    }

    public void setTecnico(Tecnico tecnico_equipo) {
        this.tecnico_equipo = tecnico_equipo;

        if (tecnico_equipo != null) {
            tecnico_equipo.club = this.teamName;
        }
    }

    public List<Jugador> getPlantilla() {
        return Collections.unmodifiableList(plantilla);
    }

    public int getCantidadJugadores() {
        return plantilla.size();
    }

    public boolean agregarJugador(Jugador jugador) {
        if (jugador == null || jugador.name == null || jugador.name.isBlank()) {
            return false;
        }

        for (Jugador j : plantilla) {
            if (j == null) {
                continue;
            }

            if(j.dorsal == jugador.dorsal ||(j.name != null && j.name.equals(jugador.name))) {
                return false;
            }
        }
         plantilla.add(jugador);
        return true;
    }

    public boolean eliminarJugador(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return false;
        }

        return plantilla.removeIf(jugador -> jugador != null && jugador.name != null && jugador.name.equalsIgnoreCase(nombre.trim()));
    }

    public Jugador buscarJugador(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return null;
        }
        for (Jugador jugador : plantilla) {
            if (jugador != null && jugador.name != null && jugador.name.equalsIgnoreCase(nombre.trim())) {
                return jugador;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String nombreTecnico = tecnico_equipo == null ? "SIN_TECNICO" : tecnico_equipo.name;

        return "ID: " + id
                + " | Equipo: " + teamName
                + " | Técnico: " + nombreTecnico
                + " | Jugadores: " + plantilla.size();
    }

}
