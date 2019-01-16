package ru.vsu.football.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.football.entity.CityEntity;

public interface CityEntityRepository extends JpaRepository<CityEntity, Integer> {
}
