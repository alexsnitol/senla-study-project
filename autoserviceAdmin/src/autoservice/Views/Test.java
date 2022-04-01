package autoservice.Views;

import autoservice.Controllers.MasterController;
import autoservice.Controllers.OrderContoller;
import autoservice.Repositories.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static java.lang.System.out;

public class Test {
    public static void main(String[] args) {
        Autoservice myAutoservice = new Autoservice("My Autoservice");

        GarageRepository garageRepository = myAutoservice.getGarage();
        garageRepository.addPlace(5);
        garageRepository.takePlace(1);
        garageRepository.takePlace(4);
        out.println(garageRepository.getInfoOfPlaces());

        out.println();
        garageRepository.freePlace(1);
        garageRepository.freePlace(4);
        out.println(garageRepository.getInfoOfPlaces());

        out.println();

        MasterRepository masters = myAutoservice.getMasters();
        masters.addMaster(new Master("Slotin", "Alexander", "Sergeevich"));
        masters.addMaster(new Master("Novikov", "Alexey", "Pavlovich"));
        masters.addMaster(new Master("Smirnov", "Ilya", "Vassilyevich"));
        masters.addMaster(new Master("Abalakov", "Anatolii", "Vasilyevich2"));
        masters.addMaster(new Master("Abalakov", "Anatolii", "Vasilyevich1"));
        out.println(masters.getInfoOfMasters());

        MasterRepository mastersSorted = MasterController.getMastersSorted(masters, 0);

        out.println();

        OrderRepository orders = myAutoservice.getOrders();
        Order tmpOrder = new Order();
        OrderMasterRepository tmpOrderMasters;

        orders.addOrder(tmpOrder);
        tmpOrder.setTimeOfBegin(new GregorianCalendar(2022, Calendar.APRIL, 1, 12, 0, 0));
        tmpOrder.setTimeOfCompletion(30);
        tmpOrder.setPrice(150000f);
        tmpOrderMasters = tmpOrder.getMasters();
        tmpOrderMasters.addMasterById(masters, 0);
        tmpOrderMasters.addMasterById(masters, 1);
        tmpOrderMasters.addMasterById(masters, 2);

        Calendar customDate = new GregorianCalendar();
        customDate.add(Calendar.HOUR, 4);
        orders.addOrder(new Order(customDate));

        tmpOrder = new Order();
        orders.addOrder(tmpOrder);
        tmpOrder.setTimeOfBegin(new GregorianCalendar(2022, Calendar.APRIL, 1, 15, 0, 0));
        tmpOrder.setTimeOfCompletion(30);
        tmpOrder.setPrice(80000f);
        tmpOrderMasters = tmpOrder.getMasters();
        tmpOrderMasters.addMasterById(masters, 1);
        tmpOrderMasters.addMasterById(masters, 3);
        tmpOrderMasters.addMasterById(masters, 4);

        tmpOrder = new Order();
        orders.addOrder(tmpOrder);
        tmpOrder.setTimeOfBegin(new GregorianCalendar(2022, Calendar.APRIL, 2, 15, 0, 0));
        tmpOrder.setTimeOfCompletion(180);

        tmpOrder = new Order();
        orders.addOrder(tmpOrder);
        tmpOrder.setTimeOfBegin(new GregorianCalendar(2022, Calendar.APRIL, 3, 15, 0, 0));
        tmpOrder.setTimeOfCompletion(180);

        customDate = new GregorianCalendar();
        customDate.add(Calendar.DAY_OF_MONTH, 2);
        tmpOrder = new Order(customDate);
        orders.addOrder(tmpOrder);
        tmpOrder.setPrice(1000000f);
        tmpOrder.setStatus(OrderStatus.COMPLETED);
        out.println(orders.getInfoOfOrders());

        out.println();

        OrderRepository ordersSorted = OrderContoller.getOrdersSorted(orders, 3);

        OrderContoller.shiftOrderTimeOfCompletion(orders, 1, 60);
        out.println(orders.getInfoOfOrders());

        out.println();

        out.println(myAutoservice.getNumberOfFreePlacesByDate(new GregorianCalendar(2022, Calendar.APRIL, 1)));
        out.println(myAutoservice.getNearestDate().getTime());

        OrderRepository ordersOfMaster = OrderContoller.getOrdersFiltered(orders, 2, 1);
        OrderRepository ordersInProcess = OrderContoller.getOrdersFiltered(orders, 1, OrderStatus.IN_PROCESS);
    }
}
