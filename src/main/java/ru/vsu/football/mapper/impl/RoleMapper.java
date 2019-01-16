package ru.vsu.football.mapper.impl;

import org.springframework.stereotype.Service;
import ru.vsu.football.domain.Role;
import ru.vsu.football.entity.RoleEntity;
import ru.vsu.football.mapper.AbstractMapper;

@Service
public class RoleMapper implements AbstractMapper<RoleEntity, Role> {
    @Override
    public RoleEntity toEntity(Role domain) {
        if (domain == null) {
            return null;
        }
        RoleEntity entity = new RoleEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        return entity;
    }

    @Override
    public Role toDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        Role role = new Role();
        role.setId(entity.getId());
        role.setName(entity.getName());
        return role;
    }
}
