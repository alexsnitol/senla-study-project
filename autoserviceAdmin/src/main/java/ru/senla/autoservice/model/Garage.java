package ru.senla.autoservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = {"id", "places", "numberOfTakenPlaces"}, allowGetters = true)
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setPlaces(List<Order> places) {
        this.places = places;
        this.size = places.size();
    }

}
