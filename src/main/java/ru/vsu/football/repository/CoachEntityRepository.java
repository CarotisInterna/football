package ru.vsu.football.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.football.entity.CoachEntity;

public interface CoachEntityRepository extends JpaRepository<CoachEntity, Integer> {
}
