package autoservice.repository.model;

public class Master extends AbstractModel {

    private String lastName;
    private String firstName;
    private String patronymic;
    private int numberOfActiveOrders = 0;

    public Master(String lastName, String firstName, String patronymic) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return this.patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public int getNumberOfActiveOrders() {
        return numberOfActiveOrders;
    }

    public void setNumberOfActiveOrders(int numberOfActiveOrders) {
        this.numberOfActiveOrders = numberOfActiveOrders;
    }

}