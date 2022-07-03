package ru.senla.autoservice.repository.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends AbstractModel {

    @Id
    @SequenceGenerator(name = "orders_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_seq")
    private Long id;

    @Column(name = "time_of_created")
    private LocalDateTime timeOfCreated = LocalDateTime.now();
    @Column(name = "time_of_begin")
    private LocalDateTime timeOfBegin;
    @Column(name = "time_of_completion")
    private LocalDateTime timeOfCompletion;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "order_master",
            joinColumns = { @JoinColumn(name = "order_id") },
            inverseJoinColumns = { @JoinColumn(name = "master_id") }
    )
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