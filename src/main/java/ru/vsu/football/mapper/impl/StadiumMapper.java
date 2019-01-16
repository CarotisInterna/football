package ru.vsu.football.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.football.domain.Stadium;
import ru.vsu.football.entity.StadiumEntity;
import ru.vsu.football.mapper.AbstractMapper;

@Service
public class StadiumMapper implements AbstractMapper<StadiumEntity, Stadium> {

    private CityMapper cityMapper;

    @Override
    public StadiumEntity toEntity(Stadium domain) {
        if (domain == null) {
            return null;
        }

        StadiumEntity entity = new StadiumEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setCapacity(domain.getCapacity());
        entity.setCity(cityMapper.toEntity(domain.getCity()));
        return entity;
    }

    @Override
    public Stadium toDomain(StadiumEntity entity) {
        if (entity == null) {
            return null;
        }

        Stadium domain = new Stadium();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setCapacity(entity.getCapacity());
        domain.setCity(cityMapper.toDomain(entity.getCity()));
        return domain;
    }

    @Autowired
    public StadiumMapper(CityMapper cityMapper) {
        this.cityMapper = cityMapper;
    }

}
