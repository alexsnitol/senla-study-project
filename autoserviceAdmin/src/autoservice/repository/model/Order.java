package autoservice.repository.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Order extends AbstractModel {

    private Calendar timeOfCreated = new GregorianCalendar();
    private Calendar timeOfBegin;
    private Calendar timeOfCompletion;
    private List<Master> masters = new ArrayList<>();
    private float price = 0;
    private OrderStatusEnum status = OrderStatusEnum.IN_PROCESS;

    public Order() {}

    public Order(Calendar timeOfCompletion) {
        this.timeOfBegin = new GregorianCalendar();
        this.timeOfCompletion = timeOfCompletion;
    }

    public Order(Calendar timeOfBegin, int minutes) {
        this.timeOfBegin = timeOfBegin;
        this.timeOfCompletion = (Calendar) this.timeOfBegin.clone();
        this.timeOfCompletion.add(Calendar.MINUTE, minutes);
    }

    public Order(Calendar timeOfBegin, Calendar timeOfCompletion) {
        this.timeOfBegin = timeOfBegin;
        this.timeOfCompletion = timeOfCompletion;
    }

    public Calendar getTimeOfCreated() {
        return timeOfCreated;
    }

    public Calendar getTimeOfBegin() {
        return timeOfBegin;
    }

    public void setTimeOfBegin(Calendar timeOfBegin) {
        this.timeOfBegin = timeOfBegin;
    }

    public Calendar getTimeOfCompletion() {
        return this.timeOfCompletion;
    }

    public void setTimeOfCompletion(Calendar timeOfCompletion) {
        this.timeOfCompletion = timeOfCompletion;
    }

    public List<Master> getMasters() {
        return masters;
    }

    public void setMasters(List<Master> masters) {
        this.masters = masters;
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