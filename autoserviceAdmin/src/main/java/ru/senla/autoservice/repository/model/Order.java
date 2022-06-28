package ru.senla.autoservice.repository.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Order extends AbstractModel {

    private LocalDateTime timeOfCreated = LocalDateTime.now();
    private LocalDateTime timeOfBegin;
    private LocalDateTime timeOfCompletion;
    private List<Long> listOfMastersId = new ArrayList<>();
    private float price = 0;
    private OrderStatusEnum status = OrderStatusEnum.IN_PROCESS;


    public Order(LocalDateTime timeOfCompletion) {
        this.timeOfBegin = LocalDateTime.now();
        this.timeOfCompletion = timeOfCompletion;
    }

    public Order(int minutes) {
        this.timeOfBegin = LocalDateTime.now();
        this.timeOfCompletion = timeOfBegin.plusMinutes(minutes);
    }

    public Order(LocalDateTime timeOfBegin, int minutes) {
        this.timeOfBegin = timeOfBegin;
        this.timeOfCompletion = timeOfBegin.plusSeconds(0);
        this.timeOfCompletion.plusMinutes(minutes);
    }

    public Order(LocalDateTime timeOfBegin, LocalDateTime timeOfCompletion) {
        this.timeOfBegin = timeOfBegin;
        this.timeOfCompletion = timeOfCompletion;
    }

}