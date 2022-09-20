package ru.senla.autoservice.service.helper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.senla.autoservice.model.AbstractModel;

import javax.persistence.EntityNotFoundException;

@Slf4j
@UtilityClass
public class EntityHelper {

    public <M extends AbstractModel> void checkEntityOnNullAfterFindedById(M model, Class<M> modelClass, Long id) {
        if (model == null) {
            String message = String.format("%s with id %s not found", modelClass.getSimpleName(), id);
            log.error(message);
            throw new EntityNotFoundException(message);
        }
    }

}
