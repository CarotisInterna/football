package ru.vsu.football.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.football.entity.AppUserEntity;

public interface AppUserEntityRepository extends JpaRepository<AppUserEntity, Integer> {

    AppUserEntity findByUsername(String username);
}
