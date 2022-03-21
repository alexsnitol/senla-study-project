package autoservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.System.*;

public class OrderArray {

    private ArrayList<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        Calendar timeOfLastOrder = new GregorianCalendar();

        if (!orders.isEmpty())
            timeOfLastOrder = (Calendar) orders.get(orders.size() - 1).getTimeOfCompletion().clone();

        this.orders.add(order);
        order.setTimeOfBegin(timeOfLastOrder);
    }

    public void deleteOrder(int index) {
        if (index >= 0 && index < orders.size())
            this.orders.remove(index);
    }

    public Order getOrder(int index) {
        if (index >= 0 && index < orders.size())
            return this.orders.get(index);
        return null;
    }

    public int sizeOrders() {
        return this.orders.size();
    }

    public void shiftTimeOfOrders(int beginIndex, int minutes) {
        if (beginIndex >= 0 && beginIndex < orders.size()) {
            if (orders.get(beginIndex).getStatus() == OrderStatus.IN_PROCESS)
                orders.get(beginIndex).getTimeOfCompletion().add(Calendar.MINUTE, minutes);
            for (int i = beginIndex + 1; i < orders.size(); i++) {
                orders.get(i).shiftTimeOfOrder(minutes);
            }
        }
    }

    public String getInfoOfOrders() {
        String result = "";
        for (int i = 0; i < orders.size(); i++) {
            result += "" + (i + 1) + ". " + orders.get(i).toString();
            if (i != orders.size() - 1)
                result += "\n";
        }
        return result;
    }
}