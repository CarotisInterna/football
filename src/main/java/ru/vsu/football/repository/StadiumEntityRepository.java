package ru.vsu.football.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.football.entity.StadiumEntity;

public interface StadiumEntityRepository extends JpaRepository<StadiumEntity, Integer> {
}
