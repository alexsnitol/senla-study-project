package autoservice;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Order {

    private Calendar timeOfCreated = new GregorianCalendar();
    private Calendar timeOfBegin;
    private Calendar timeOfCompletion;
    private OrderStatus status;

    public Order() {
        this.status = OrderStatus.IN_PROCESS;
    }


    /**
     * @param timeOfCompletion
     */
    public Order(Calendar timeOfCompletion) {
        this.timeOfCompletion = timeOfCompletion;
        this.status = OrderStatus.IN_PROCESS;
    }

    /**
     * @param timeOfBegin
     * @param minutes
     */
    public Order(Calendar timeOfBegin, int minutes) {
        this.timeOfBegin = timeOfBegin;
        this.timeOfCompletion = (Calendar) this.timeOfBegin.clone();
        this.timeOfCompletion.add(Calendar.MINUTE, minutes);
        this.status = OrderStatus.IN_PROCESS;
    }

    /**
     * @param timeOfBegin
     * @param timeOfCompletion
     */
    public Order(Calendar timeOfBegin, Calendar timeOfCompletion) {
        this.timeOfBegin = timeOfBegin;
        this.timeOfCompletion = timeOfCompletion;
        this.status = OrderStatus.IN_PROCESS;
    }

    /**
     * @param timeOfCompletion
     * @param status
     */
    public Order(Calendar timeOfCompletion, OrderStatus status) {
        this.timeOfCompletion = timeOfCompletion;
        this.status = status;
    }

    /**
     * @param timeOfBegin
     * @param timeOfCompletion
     * @param status
     */
    Order(Calendar timeOfBegin, Calendar timeOfCompletion, OrderStatus status) {
        this.timeOfBegin = timeOfBegin;
        this.timeOfCompletion = timeOfCompletion;
        this.status = status;
    }

    public Calendar getTimeOfCreated() {
        return timeOfCreated;
    }

    public Calendar getTimeOfBegin() {
        return timeOfBegin;
    }

    /**
     * @param timeOfBegin
     */
    public void setTimeOfBegin(Calendar timeOfBegin) {
        this.timeOfBegin = timeOfBegin;
    }

    public Calendar getTimeOfCompletion() {
        return this.timeOfCompletion;
    }

    /**
     * @param timeOfCompletion
     */
    public void setTimeOfCompletion(Calendar timeOfCompletion) {
        this.timeOfCompletion = timeOfCompletion;
    }

    /**
     * @param minutes
     */
    public void setTimeOfCompletion(int minutes) {
        this.timeOfCompletion = (Calendar) this.timeOfBegin.clone();
        this.timeOfCompletion.add(Calendar.MINUTE, minutes);
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    /**
     * @param status
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * @param minutes
     */
    public void shiftTimeOfOrder(int minutes) {
        if (this.status == OrderStatus.IN_PROCESS) {
            this.timeOfBegin.add(Calendar.MINUTE, minutes);
            this.timeOfCompletion.add(Calendar.MINUTE, minutes);
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "timeOfCreated=" + timeOfCreated.getTime() +
                ", timeOfBegin=" + timeOfBegin.getTime() +
                ", timeOfCompletion=" + timeOfCompletion.getTime() +
                ", status=" + status +
                '}';
    }
}