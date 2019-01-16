package ru.vsu.football.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.football.domain.RoleId;
import ru.vsu.football.entity.RoleEntity;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, RoleId> {
}
