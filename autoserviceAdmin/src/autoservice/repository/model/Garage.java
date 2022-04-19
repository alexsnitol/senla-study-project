package autoservice.repository.model;

import java.util.ArrayList;
import java.util.List;

public class Garage extends AbstractModel {
    // >=0 - place is taken
    // null - place is free
    private List<Long> places;
    private int size = 0;
    private Integer numberOfTakenPlaces = 0;

    public Garage() {}

    public Garage(int size) {
        this.size = size;
        this.places = new ArrayList<>(size);
        for (Long i = 0L; i < size; i++) {
            this.places.add(null);
        }
    }

    public List<Long> getPlaces() {
        return this.places;
    }

    public int getSize() {
        return size;
    }

    public void setPlaces(List<Long> places) {
        this.places = places;
        this.size = places.size();
    }

    public Integer findByOrderId(Long orderId) {
        for (Integer i = 0; i < size; i++) {
            if (places.get(i) == orderId) {
                return i;
            }
        }

        return null;
    }

    public Integer getNumberOfTakenPlaces() {
        return this.numberOfTakenPlaces;
    }

    public void setNumberOfTakenPlaces(Integer numberOfTakenPlaces) {
        this.numberOfTakenPlaces = numberOfTakenPlaces;
    }
}
