package autoservice.view.cli.menu;

import java.util.List;

public class Menu {

    private String name;
    private String description;
    private List<MenuItem> menuItems;
    private Menu previousMenu;

    public Menu() {}

    public Menu(String name, Menu previousMenu) {
        this.name = name;
        this.previousMenu = previousMenu;
    }

    public Menu(String name, List<MenuItem> menuItems, Menu previousMenu) {
        this.name = name;
        this.menuItems = menuItems;
        this.previousMenu = previousMenu;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MenuItem> getMenuItems() {
        return this.menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public Menu getPreviousMenu() {
        return previousMenu;
    }

    public void setPreviousMenu(Menu previousMenu) {
        this.previousMenu = previousMenu;
    }
}
