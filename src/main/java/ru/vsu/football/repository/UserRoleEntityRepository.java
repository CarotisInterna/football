package ru.vsu.football.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.football.domain.UserRoleId;
import ru.vsu.football.entity.UserRoleEntity;

public interface UserRoleEntityRepository extends JpaRepository<UserRoleEntity, UserRoleId> {
}
