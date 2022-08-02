package ru.senla.autoservice.repo.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.User;
import ru.senla.autoservice.repo.IUserRepository;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class UserRepositoryImpl extends AbstractRepositoryImpl<User> implements IUserRepository {

    @PostConstruct
    public void init() {
        setClazz(User.class);
    }


    @Override
    public Map<String, List<Order>> getSortMap(Root<User> root) {
        return Collections.emptyMap();
    }

    @Override
    public List<Predicate> getPredicateList(Root<User> root, MultiValueMap<String, String> requestParams) {
        List<Predicate> predicateList = new ArrayList<>();

        if (requestParams.containsKey("username")) {
            String username = requestParams.get("username").get(0);

            predicateList.add(getPredicateOfUsername(root, username));
        }

        return predicateList;
    }

    private Predicate getPredicateOfUsername(Root<User> root, String username) {
        return criteriaBuilder.equal(root.get("username"), username);
    }

    public User findByUsername(String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        return findOne(params);
    }
}
