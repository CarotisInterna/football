package ru.vsu.football.mapper.impl;

import org.springframework.stereotype.Service;
import ru.vsu.football.domain.City;
import ru.vsu.football.entity.CityEntity;
import ru.vsu.football.mapper.AbstractMapper;

@Service
public class CityMapper implements AbstractMapper<CityEntity, City> {

    @Override
    public CityEntity toEntity(City domain) {
        if (domain == null) {
            return null;
        }

        CityEntity cityEntity = new CityEntity();
        cityEntity.setId(domain.getId());
        cityEntity.setName(domain.getName());
        return cityEntity;
    }

    @Override
    public City toDomain(CityEntity entity) {
        if (entity == null) {
            return null;
        }

        City city = new City();
        city.setId(entity.getId());
        city.setName(entity.getName());
        return city;
    }
}
