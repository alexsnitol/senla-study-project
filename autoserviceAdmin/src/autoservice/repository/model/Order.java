package autoservice.repository.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order extends AbstractModel {

    private LocalDateTime timeOfCreated = LocalDateTime.now();
    private LocalDateTime timeOfBegin;
    private LocalDateTime timeOfCompletion;
    private List<Long> listOfMastersId = new ArrayList<>();
    private float price = 0;
    private OrderStatusEnum status = OrderStatusEnum.IN_PROCESS;

    public Order() {}

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

    public LocalDateTime getTimeOfCreated() {
        return timeOfCreated;
    }

    public LocalDateTime getTimeOfBegin() {
        return timeOfBegin;
    }

    public void setTimeOfBegin(LocalDateTime timeOfBegin) {
        this.timeOfBegin = timeOfBegin;
    }

    public LocalDateTime getTimeOfCompletion() {
        return this.timeOfCompletion;
    }

    public void setTimeOfCompletion(LocalDateTime timeOfCompletion) {
        this.timeOfCompletion = timeOfCompletion;
    }

    public List<Long> getListOfMastersId() {
        return listOfMastersId;
    }

    public void setListOfMastersId(List<Long> listOfMastersId) {
        this.listOfMastersId = listOfMastersId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public OrderStatusEnum getStatus() {
        return this.status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    /*@Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", timeOfCreated=" + timeOfCreated.getTime() +
                ", timeOfBegin=" + timeOfBegin.getTime() +
                ", timeOfCompletion=" + timeOfCompletion.getTime() +
                ", masters=" + (masters.getInfoOfMasters() != "" ? "\n{\n" + masters.getInfoOfMasters() + "\n}\n" : "not set") +
                ", price=" + price +
                ", status=" + status +
                '}';
    }*/
}