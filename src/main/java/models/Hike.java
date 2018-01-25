package models;

public class Hike {
    private String nameOfHike;
    private String notesOnHike;
    private int ratingHike;
    private int id;
    private int locationId;

    public Hike(String nameOfHike, String notesOnHike, int ratingHike, int locationId) {
        this.nameOfHike = nameOfHike;
        this.notesOnHike = notesOnHike;
        this.ratingHike = ratingHike;
        this.locationId = locationId;
    }

    // Getters

    public String getNameOfHike() {
        return nameOfHike;
    }

    public String getNotesOnHike() {
        return notesOnHike;
    }

    public int getRatingHike() {
        return ratingHike;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getId() {
        return id;
    }


    // Setters

    public void setNameOfHike(String nameOfHike) {
        this.nameOfHike = nameOfHike;
    }

    public void setNotesOnHike(String notesOnHike) {
        this.notesOnHike = notesOnHike;
    }

    public void setRatingHike(int ratingHike) {
        this.ratingHike = ratingHike;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hike hike = (Hike) o;

        if (ratingHike != hike.ratingHike) return false;
        if (id != hike.id) return false;
        if (locationId != hike.locationId) return false;
        if (!nameOfHike.equals(hike.nameOfHike)) return false;
        return notesOnHike != null ? notesOnHike.equals(hike.notesOnHike) : hike.notesOnHike == null;
    }

    @Override
    public int hashCode() {
        int result = nameOfHike.hashCode();
        result = 31 * result + (notesOnHike != null ? notesOnHike.hashCode() : 0);
        result = 31 * result + ratingHike;
        result = 31 * result + id;
        result = 31 * result + locationId;
        return result;
    }
}
