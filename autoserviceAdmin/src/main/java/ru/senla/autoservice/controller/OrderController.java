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
@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController extends AbstractController<Order, IOrderService> {

    private final IOrderService orderService;
    private final IGarageService garageService;
    private final IMasterService masterService;


    @PostConstruct
    public void init() {
        this.defaultService = orderService;
        this.clazz = Order.class;
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
    @PutMapping("/{id}")
    public Order update(@PathVariable Long id, @RequestBody Order changedModel) {
        return super.update(id, changedModel);
    }

    @DeleteMapping("/{id}")
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

    @PostMapping
    public TakenPlaceDto addAndTakePlace(@RequestBody Order newOrder) {
        log.info("Adding new order: {}", newOrder.toString());
        return orderService.addAndTakePlace(newOrder);
    }

    @PostMapping("/{id}/set-time-of-completion")
    public ResponseEntity<String> setTimeOfCompletion(@PathVariable Long id, @RequestParam int minutes) {
        log.info("Setting time of completion for order with id {} on {} min.", id, minutes);
        orderService.setTimeOfCompletionInOrderByIdAndUpdate(id, minutes);
        return ResponseEntity.ok("Time of completion for order with id " + id + " set to " + minutes + " minutes");
    }

    @PostMapping("/{id}/set-status")
    public ResponseEntity<String> setStatus(@PathVariable Long id, @RequestParam OrderStatusEnum status) {
        log.info("Setting new status for order with id {}", id);
        orderService.setStatusInOrderByIdAndUpdate(id, status);
        return ResponseEntity.ok("Status for order with id " + id + " set to " + status);
    }

    @PostMapping("/{id}/set-price")
    public ResponseEntity<String> setPrice(@PathVariable Long id, @RequestParam float price) {
        log.info("Setting new price for order with id {} on {}", id, price);
        orderService.setPriceInOrderByIdAndUpdate(id, price);
        return ResponseEntity.ok("Price for order with id " + id + " set to " + price);
    }

    @PostMapping("/{id}/assign-master")
    public ResponseEntity<String> assignMasterById(@PathVariable Long id, @RequestParam Long masterId) {
        log.info("Assign master with id {} on order with id {}", masterId, id);
        orderService.assignMasterByIdInOrderByIdAndUpdate(id, masterId);
        return ResponseEntity.ok("Master with id " + masterId + " assigned on order with id " + id);
    }

    @DeleteMapping("/{id}/masters/{masterId}")
    public ResponseEntity<String> removeMasterById(@PathVariable Long id, @PathVariable Long masterId) {
        log.info("Remove master with id {} from order with id {}", masterId, id);
        orderService.removeMasterByIdInOrderByIdAndUpdate(id, masterId);
        return ResponseEntity.ok("Master with id " + masterId + " removed from order with id " + id);
    }

    @PostMapping("/{id}/shift-time-of-completion")
    public ResponseEntity<String> shiftTimeOfCompletion(@PathVariable Long id, @RequestParam int shiftMinutes) {
        log.info("Shifting time of completion of order with id {} on {} minutes", id, shiftMinutes);
        orderService.shiftTimeOfCompletionInOrderByIdWithUpdate(id, shiftMinutes);
        return ResponseEntity.ok(
                "Time of completion for order with id " + id + " shifted on " + shiftMinutes + " minutes"
        );
    }

}
