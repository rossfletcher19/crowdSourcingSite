package models;

import javax.print.DocFlavor;

/**
 * Created by Guest on 1/18/18.
 */
public class Location {

    private String nameCity;
    private String nameState;
    private String nameCountry;
    private int id;

    public Location(String nameCity, String nameState, String nameCountry, int id) {
        this.nameCity = nameCity;
        this.nameState = nameState;
        this.nameCountry = nameCountry;
        this.id = id;
    }

//    Getters

    public String getNameCity() {
        return nameCity;
    }

    public String getNameState() {
        return nameState;
    }

    public String getNameCountry() {
        return nameCountry;
    }

    public int getId() {
        return id;
    }


//  Setters

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public void setNameState(String nameState) {
        this.nameState = nameState;
    }

    public void setNameCountry(String nameCountry) {
        this.nameCountry = nameCountry;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (id != location.id) return false;
        if (nameCity != null ? !nameCity.equals(location.nameCity) : location.nameCity != null) return false;
        if (nameState != null ? !nameState.equals(location.nameState) : location.nameState != null) return false;
        return nameCountry != null ? nameCountry.equals(location.nameCountry) : location.nameCountry == null;
    }

    @Override
    public int hashCode() {
        int result = nameCity != null ? nameCity.hashCode() : 0;
        result = 31 * result + (nameState != null ? nameState.hashCode() : 0);
        result = 31 * result + (nameCountry != null ? nameCountry.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }
}
