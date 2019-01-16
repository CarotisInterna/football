package ru.vsu.football.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.football.entity.TeamEntity;

public interface TeamEntityRepository extends JpaRepository<TeamEntity, Integer> {
}
