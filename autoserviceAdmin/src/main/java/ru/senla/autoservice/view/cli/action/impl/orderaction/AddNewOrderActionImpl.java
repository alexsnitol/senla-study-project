package ru.senla.autoservice.view.cli.action.impl.orderaction;


import ru.senla.autoservice.controller.OrderController;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class AddNewOrderActionImpl implements IAction {
    @Override
    public void execute() {
        OrderController orderController = OrderController.getInstance();
        Scanner scanner = new Scanner(System.in);

        Order newOrder = new Order();

        Integer year;
        Integer month;
        Integer day;
        Integer hours;
        Integer minutes;
        Integer seconds;

        out.println("enter time of begin with a space (year month day hour minute second)");
        out.print(MenuController.CONSOLE_POINTER);
        year = scanner.nextInt();
        month = scanner.nextInt();
        day = scanner.nextInt();
        hours = scanner.nextInt();
        minutes = scanner.nextInt();
        seconds = scanner.nextInt();
        LocalDateTime timeOfBegin = LocalDateTime.of(year, month, day, hours, minutes, seconds);
        newOrder.setTimeOfBegin(timeOfBegin);

        List<Long> garageIdAndTakenPlace = orderController.addOrderAndTakePlace(newOrder);

        out.println("enter time of completed order in minutes");
        out.print(MenuController.CONSOLE_POINTER);
        minutes = scanner.nextInt();
        orderController.setTimeOfCompletion(newOrder.getId(), minutes);

        out.println("enter price");
        out.print(MenuController.CONSOLE_POINTER);
        float price = scanner.nextFloat();
        orderController.setPrice(newOrder.getId(), price);

        out.println("order with id of " + newOrder.getId() +
                " in process and taken in garage with id of " + garageIdAndTakenPlace.get(0) +
                " place with index of " + garageIdAndTakenPlace.get(1));
    }
}
