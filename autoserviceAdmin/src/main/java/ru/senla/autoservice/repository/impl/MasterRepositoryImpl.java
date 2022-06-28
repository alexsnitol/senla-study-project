package ru.senla.autoservice.repository.impl;

import configuremodule.annotation.Singleton;
import ru.senla.autoservice.repository.IMasterRepository;
import ru.senla.autoservice.repository.model.Master;

@Singleton
public class MasterRepositoryImpl extends AbstractRepositoryImpl<Master> implements IMasterRepository {

}