package ru.vsu.football.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.vsu.football.domain.Role;
import ru.vsu.football.domain.RoleId;
import ru.vsu.football.entity.RoleEntity;
import ru.vsu.football.mapper.AbstractMapper;
import ru.vsu.football.service.AbstractService;

@Service
public class RoleService extends AbstractService<RoleEntity, Role, RoleId> {

    public RoleService(JpaRepository<RoleEntity, RoleId> repository, AbstractMapper<RoleEntity, Role> mapper) {
        super(repository, mapper);
    }
}
