package ru.senla.autoservice.repo;

import ru.senla.autoservice.model.User;

public interface IUserRepository extends IAbstractRepository<User> {

    User findByUsername(String username);

}
