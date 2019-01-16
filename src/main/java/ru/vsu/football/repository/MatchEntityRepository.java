package ru.vsu.football.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.football.entity.MatchEntity;

public interface MatchEntityRepository extends JpaRepository<MatchEntity, Integer> {
}
