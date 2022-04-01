package autoservice.Repositories;

import autoservice.Repositories.OrderStatus;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Order {

    private int id;
    private Calendar timeOfCreated = new GregorianCalendar();
    private Calendar timeOfBegin;
    private Calendar timeOfCompletion;
    private OrderMasterRepository masters = new OrderMasterRepository(this);
    private float price = 0;
    private OrderStatus status = OrderStatus.IN_PROCESS;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setTimeOfCompletion(int minutes) {
        if (this.timeOfBegin == null)
            return;

        this.timeOfCompletion = (Calendar) this.timeOfBegin.clone();
        this.timeOfCompletion.add(Calendar.MINUTE, minutes);
    }

    public OrderMasterRepository getMasters() {
        return masters;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public void setStatus(OrderStatus status) {
        if (this.status != OrderStatus.IN_PROCESS && this.status != OrderStatus.POSTPONED) {
            if (status == OrderStatus.IN_PROCESS && status == OrderStatus.POSTPONED) {
                for (Master master : this.masters.getMasters()) {
                    master.setNumberOfActiveOrders(master.getNumberOfActiveOrders() + 1);
                }
            }
        } else {
            if (status != OrderStatus.IN_PROCESS && status != OrderStatus.POSTPONED) {
                for (Master master : this.masters.getMasters()) {
                    master.setNumberOfActiveOrders(master.getNumberOfActiveOrders() - 1);
                }
            }
        }

        this.status = status;
    }

    public void shiftTimeOfOrder(int minutes) {
        if (this.status == OrderStatus.IN_PROCESS) {
            this.timeOfCompletion.add(Calendar.MINUTE, minutes);
        } else if (this.status == OrderStatus.POSTPONED) {
            this.timeOfBegin.add(Calendar.MINUTE, minutes);
            this.timeOfCompletion.add(Calendar.MINUTE, minutes);
        }
    }

    @Override
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
    }
}