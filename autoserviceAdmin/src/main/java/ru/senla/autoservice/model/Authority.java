package ru.senla.autoservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "authorities")
public class Authority extends AbstractModel {

    @Id
    @SequenceGenerator(name = "authorities_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authorities_id_seq")
    private Long id;

    private String name;

}
