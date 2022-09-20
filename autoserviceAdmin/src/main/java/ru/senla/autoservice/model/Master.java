package ru.senla.autoservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = {"id", "numberOfActiveOrders", "orders"}, allowGetters = true)
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
                    + " INNER JOIN orders o on o.id = om.order_id"
                    + " WHERE"
                    + " om.master_id = id AND ("
                    + " o.status = 'IN_PROCESS' OR"
                    + " o.status = 'POSTPONED'"
                    + " )"
                    + " GROUP BY om.master_id)"
    )
    private Integer numberOfActiveOrders = 0;
    @ManyToMany(mappedBy = "masters", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonBackReference
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