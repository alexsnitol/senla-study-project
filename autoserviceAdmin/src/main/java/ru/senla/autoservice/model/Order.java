package ru.senla.autoservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = {"id", "timeOfCreated", "masters"}, allowGetters = true)
@Entity
@Table(name = "orders")
public class Order extends AbstractModel {

    @Id
    @SequenceGenerator(name = "orders_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_seq")
    private Long id;

    @Column(name = "time_of_created")
    @JsonFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    private LocalDateTime timeOfCreated = LocalDateTime.now();
    @Column(name = "time_of_begin")
    @JsonFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    private LocalDateTime timeOfBegin;
    @Column(name = "time_of_completion")
    @JsonFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    private LocalDateTime timeOfCompletion;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "order_master",
            joinColumns = { @JoinColumn(name = "order_id") },
            inverseJoinColumns = { @JoinColumn(name = "master_id") }
    )
    @JsonManagedReference
    private List<Master> masters;
    private float price = 0;
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status = OrderStatusEnum.IN_PROCESS;

    public Order(LocalDateTime timeOfCompletion) {
        this.timeOfBegin = LocalDateTime.now();
        this.timeOfCompletion = timeOfCompletion;
    }

    public Order(int minutes) {
        this.timeOfBegin = LocalDateTime.now();
        this.timeOfCompletion = this.timeOfBegin.plusMinutes(minutes);
    }

    public Order(LocalDateTime timeOfBegin, int minutes) {
        this.timeOfBegin = timeOfBegin;
        this.timeOfCompletion = timeOfBegin.plusSeconds(0);
        this.timeOfCompletion = this.timeOfCompletion.plusMinutes(minutes);
    }

    public Order(LocalDateTime timeOfBegin, LocalDateTime timeOfCompletion) {
        this.timeOfBegin = timeOfBegin;
        this.timeOfCompletion = timeOfCompletion;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}