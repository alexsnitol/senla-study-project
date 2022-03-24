package autoservice;

public class Autoservice {

    private String name;
    private MasterArray masters = new MasterArray();
    private Garage garage = new Garage();
    private OrderArray orders = new OrderArray();

    public Autoservice() {
    }

    /**
     * @param name
     */
    public Autoservice(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public MasterArray getMasters() {
        return this.masters;
    }

    public Garage getGarage() {
        return this.garage;
    }

    public OrderArray getOrders() {
        return this.orders;
    }

}