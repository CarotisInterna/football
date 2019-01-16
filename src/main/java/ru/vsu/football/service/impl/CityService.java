package ru.vsu.football.service.impl;

import org.springframework.stereotype.Service;
import ru.vsu.football.domain.City;
import ru.vsu.football.entity.CityEntity;
import ru.vsu.football.mapper.impl.CityMapper;
import ru.vsu.football.repository.CityEntityRepository;
import ru.vsu.football.service.AbstractService;

@Service
public class CityService extends AbstractService<CityEntity, City, Integer> {


    public CityService(CityEntityRepository repository, CityMapper mapper) {
        super(repository, mapper);
    }


}
