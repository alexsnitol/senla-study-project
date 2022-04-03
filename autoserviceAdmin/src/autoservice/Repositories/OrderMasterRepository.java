package autoservice.Repositories;

public class OrderMasterRepository extends MasterRepository {
    private Order order;

    OrderMasterRepository(Order order) {
        this.order = order;
    }

    public void addMasterById(MasterRepository masters, int id) {
        Master master = masters.getMasterById(id);

        if (master != null) {
            if (this.order.getStatus() == OrderStatus.IN_PROCESS || this.order.getStatus() == OrderStatus.POSTPONED)
                master.setNumberOfActiveOrders(master.getNumberOfActiveOrders() + 1);

            this.masters.add(master);
        }
    }

    public void deleteMasterById(MasterRepository masters, int id) {
        Master master = masters.getMasterById(id);

        if (master != null) {
            if (this.order.getStatus() == OrderStatus.IN_PROCESS || this.order.getStatus() == OrderStatus.POSTPONED)
                master.setNumberOfActiveOrders(master.getNumberOfActiveOrders() - 1);

            this.masters.remove(master);
        }
    }
}
