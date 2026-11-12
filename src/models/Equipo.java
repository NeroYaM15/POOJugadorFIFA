package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Equipo {
    private Tecnico tecnico_equipo;
    private List<Jugador> plantilla = new ArrayList<>();
    private String teamName;

    public Equipo(String teamName) {
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
        return "models.Equipo{" +
                "teamName=" + teamName + '\'' +
                ",tecnico_equipo=" + (tecnico_equipo == null ? "sin asignar" : tecnico_equipo.name) + ",jugadores=" + plantilla.size() +'}';
    }

}
