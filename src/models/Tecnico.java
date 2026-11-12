package models;

import java.time.LocalDate;
import java.time.Period;

public class Tecnico extends Persona{
    public String club;
    public LocalDate nacimiento;

    public Tecnico(){
    }
    public Tecnico(String nombre, LocalDate nacimiento, String club){
        super.name = nombre;
        setNacimiento(nacimiento);
    }

    public LocalDate getNacimiento() {
        return nacimiento;
    }
    public void setNacimiento(LocalDate nacimiento) {
        if(nacimiento != null && nacimiento.isAfter(LocalDate.now())){
            return;
        }
        this.nacimiento = nacimiento;
    }

    public int get(){
        if (nacimiento == null) {
            return 0;
        }

        return Period.between(
                nacimiento,
                LocalDate.now()
        ).getYears();
    }

    @Override
    public String toString() {
        return "models.Tecnico{" +
                "club='" + club + '\'' +
                "} " + super.toString();
    }
}

