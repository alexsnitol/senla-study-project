package ru.senla.autoservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.service.IMasterService;
import ru.senla.autoservice.service.IOrderService;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/masters")
public class MasterController extends AbstractController<Master, IMasterService> {

    private final IMasterService masterService;
    private final IOrderService orderService;


    @PostConstruct
    public void init() {
        this.defaultService = masterService;
        this.clazz = Master.class;
    }


    @Override
    @GetMapping
    public List<Master> getAll(@RequestParam(required = false) MultiValueMap<String, String> requestParams) {
        return masterService.checkRequestParamsAndGetAll(requestParams);
    }

    @Override
    @GetMapping("/{id}")
    public Master getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @GetMapping("/{id}/orders")
    public List<Order> getOrdersMyMasterId(
            @PathVariable String id,
            @RequestParam(required = false) MultiValueMap<String, String> requestParams) {
        return orderService.getAllByMasterId(id, requestParams);
    }

    @Override
    @PutMapping("/{id}")
    public Master update(@PathVariable Long id, @RequestBody Master changedModel) {
        return super.update(id, changedModel);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        log.info("Deleting master with id {}", id);

        return super.deleteById(id);
    }

    @Override
    @GetMapping("/size")
    public Integer size() {
        return super.size();
    }

    @Override
    @PostMapping
    public Master add(@RequestBody Master newMaster) {
        log.info("Adding new master: {}", newMaster);

        return super.add(newMaster);
    }

    @PostMapping("/full-name")
    public String getFullName(@RequestBody Master master) {
        return masterService.getFullName(master);
    }

    @PostMapping("/full-name-with-id")
    public String getFullNameWithId(@RequestBody Master master) {
        return masterService.getFullNameWithId(master);
    }

}
