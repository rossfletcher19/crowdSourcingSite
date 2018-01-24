package models;

import javax.print.DocFlavor;

/**
 * Created by Guest on 1/18/18.
 */
public class Location {

    private String name;
    private int id;

    public Location(String name) {
        this.name = name;
    }

//    Getters

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }


//  Setters

    public void setName(String name) {
        this.name = name;
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
        return name.equals(location.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id;
        return result;
    }
}
