package ru.senla.autoservice.repository.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "masters")
public class Master extends AbstractModel {

    @Id
    @SequenceGenerator(name = "masters_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "masters_id_seq")
    private Long id;

    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    private String patronymic;
    @Formula(
            "  (SELECT count(om.order_id)"
            + " FROM order_master om"
            + " WHERE om.master_id = id"
            + " GROUP BY om.master_id)"
    )
    private Integer numberOfActiveOrders = 0;
    @ManyToMany(mappedBy = "masters", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Order> orders;

    public Master(String lastName, String firstName, String patronymic) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @PostLoad
    private void setDefaultValueForNumberOfActiveOrders() {
        if (this.numberOfActiveOrders == null) {
            this.numberOfActiveOrders = 0;
        }
    }

}