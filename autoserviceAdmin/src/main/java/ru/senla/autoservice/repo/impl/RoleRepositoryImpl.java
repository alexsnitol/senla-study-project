package ru.senla.autoservice.repo.impl;

import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.Role;
import ru.senla.autoservice.repo.IRoleRepository;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class RoleRepositoryImpl extends AbstractRepositoryImpl<Role> implements IRoleRepository {

    @Override
    public Map<String, List<Order>> getSortMap(Root<Role> root) {
        return Collections.emptyMap();
    }

    @Override
    public List<Predicate> getPredicateList(Root<Role> root, MultiValueMap<String, String> requestParams) {
        return Collections.emptyList();
    }

}
