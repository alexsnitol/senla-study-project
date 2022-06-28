package ru.senla.autoservice.view.cli;

import ru.senla.autoservice.view.cli.action.IAction;
import ru.senla.autoservice.view.cli.menu.Menu;
import ru.senla.autoservice.view.cli.menu.MenuItem;

import java.util.List;

import static java.lang.System.err;
import static java.lang.System.out;

public class Navigator {
    private Menu currentMenu;

    public Navigator(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    private String getFullPath() {
        String fullPath = "";
        Menu tmpMenu = currentMenu;

        while (tmpMenu != null) {
            if (fullPath.equals("")) {
                fullPath = tmpMenu.getName();
            } else {
                fullPath = tmpMenu.getName() + " -> " + fullPath;
            }
            tmpMenu = tmpMenu.getPreviousMenu();
        }

        return fullPath;
    }

    public void printMenu() {
        String fullPath = getFullPath();
        if (!fullPath.equals(currentMenu.getName())) {
            out.println(fullPath);
        }
        out.println(currentMenu.getName());
        if (currentMenu.getDescription() != null) {
            out.println(currentMenu.getDescription());
        }
        out.println("");

        int i = 1;
        for (MenuItem menuItem : currentMenu.getMenuItems()) {
            out.println(i++ + ". " + menuItem.getTitle());
        }
        out.println("");

        if (currentMenu.getPreviousMenu() != null) {
            out.println("0" + ". return");
        }

        out.println("-1" + ". exit");
    }

    public void navigate(Integer index) throws Exception {
        if (index == 0) {
            if (currentMenu.getPreviousMenu() != null) {
                currentMenu = currentMenu.getPreviousMenu();
            }
            return;
        } else if (index == -1) {
            return;
        }

        index = index - 1;

        List<MenuItem> currentMenuItems = currentMenu.getMenuItems();

        if (index >= currentMenuItems.size() || index < -1) {
            err.println("wrong enter, try again");
            return;
        }

        MenuItem selectedMenuItem = currentMenuItems.get(index);
        IAction selectedAction = selectedMenuItem.getAction();
        Menu selectedNextMenu = selectedMenuItem.getNextMenu();

        if (selectedAction != null) {
            selectedAction.execute();
        }

        if (selectedNextMenu != null) {
            currentMenu = selectedNextMenu;
        }
    }
}
