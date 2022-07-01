package ru.senla.autoservice.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "garages")
public class Garage extends AbstractModel {

    @Id
    @SequenceGenerator(name = "garages_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "garages_id_seq")
    private Long id;

    @Transient
    private List<Order> places = new ArrayList<>();
    private int size = 0;
    @Transient
    private Integer numberOfTakenPlaces = 0;


    public Garage(int size) {
        this.size = size;
        this.places = new ArrayList<>(size);
        for (long i = 0; i < size; i++) {
            this.places.add(null);
        }
    }

    public void setPlaces(List<Order> places) {
        this.places = places;
        this.size = places.size();
    }

    public Integer getIndexOfPlaceByOrderId(Long orderId) {
        for (int i = 0; i < size; i++) {
            if (places.get(i).getId().equals(orderId)) {
                return i;
            }
        }

        return null;
    }
}
