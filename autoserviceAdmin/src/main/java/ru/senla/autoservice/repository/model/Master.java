package ru.senla.autoservice.repository.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
}