package ru.senla.autoservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.senla.autoservice.dto.TakenPlaceDto;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderStatusEnum;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.service.IMasterService;
import ru.senla.autoservice.service.IOrderService;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController extends AbstractController<Order, IOrderService> {

    private final IOrderService orderService;
    private final IGarageService garageService;
    private final IMasterService masterService;


    @Autowired
    public OrderController(IOrderService orderService, IGarageService garageService, IMasterService masterService) {
        this.orderService = orderService;
        this.garageService = garageService;
        this.masterService = masterService;
    }

    @PostConstruct
    public void init() {
        this.defaultService = orderService;
    }


    @GetMapping
    public List<Order> getAll(@RequestParam(required = false) MultiValueMap<String, String> requestParams) {
        return orderService.checkRequestParamsAndGetAll(requestParams);
    }

    @Override
    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @GetMapping("/{id}/masters")
    public List<Master> getMastersByOrderId(
            @PathVariable String id,
            @RequestParam(required = false) MultiValueMap<String, String> requestParams) {
        return masterService.getAllByOrderId(id, requestParams);
    }

    @Override
    @PutMapping("/add")
    public Order add(@RequestBody Order newOrder) {
        return super.add(newOrder);
    }

    @Override
    @PutMapping("/{id}/update")
    public Order update(@PathVariable Long id, @RequestBody Order changedModel) {
        return super.update(id, changedModel);
    }

    @Override
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody Order model) {
        return super.delete(model);
    }

    @Override
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        return super.deleteById(id);
    }

    @DeleteMapping("/{id}/delete-and-free-place")
    public ResponseEntity<String> deleteByIdAndFreePlace(@PathVariable Long id) {
        log.info("Deleting order with id {} and freeing up taken place", id);
        orderService.deleteByIdAndFreePlace(id);

        return ResponseEntity.ok("Order with id " + id + " deleted and place freed");
    }

    @Override
    @GetMapping("/size")
    public Integer size() {
        return super.size();
    }

    @PostMapping("/add-and-take-place")
    public TakenPlaceDto addAndTakePlace(@RequestBody Order newOrder) {
        log.info("Adding new order: {}", newOrder.toString());
        return orderService.addAndTakePlace(newOrder);
    }

    @PostMapping("/{id}/set-time-of-completion")
    public Order setTimeOfCompletion(@PathVariable Long id, @RequestParam int minutes) {
        log.info("Setting time of completion for order with id {} on {} min.", id, minutes);
        return orderService.setTimeOfCompletionInOrderByIdAndUpdate(id, minutes);
    }

    @PostMapping("/{id}/set-status")
    public Order setStatus(@PathVariable Long id, @RequestParam OrderStatusEnum status) {
        log.info("Setting new status for order with id {}", id);
        return orderService.setStatusInOrderByIdAndUpdate(id, status);
    }

    @PostMapping("/{id}/set-price")
    public Order setPrice(@PathVariable Long id, @RequestParam float price) {
        log.info("Setting new price for order with id {} on {}", id, price);
        return orderService.setPriceInOrderByIdAndUpdate(id, price);
    }

    @PostMapping("/{id}/assign-master")
    public Order assignMasterById(@PathVariable Long id, @RequestParam Long masterId) {
        log.info("Assign master with id {} on order with id {}", masterId, id);
        return orderService.assignMasterByIdInOrderByIdAndUpdate(id, masterId);
    }

    @PostMapping("/{id}/remove-master")
    public Order removeMasterById(@PathVariable Long id, @RequestParam Long masterId) {
        log.info("Remove master with id {} from order with id {}", masterId, id);
        return orderService.removeMasterByIdInOrderByIdAndUpdate(id, masterId);
    }

    @PostMapping("/{id}/shift-time-of-completion")
    public Order shiftTimeOfCompletion(@PathVariable Long id, @RequestParam int shiftMinutes) {
        log.info("Shifting time of completion of order with id {} on {} minutes", id, shiftMinutes);
        return orderService.shiftTimeOfCompletionInOrderById(id, shiftMinutes);
    }

}
