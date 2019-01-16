package ru.vsu.football.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.football.entity.PlayerEntity;

public interface PlayerEntityRepository extends JpaRepository<PlayerEntity, Integer> {

}
