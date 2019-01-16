package ru.vsu.football.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.football.domain.FootballDomain;
import ru.vsu.football.mapper.AbstractMapper;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractService<E, D extends FootballDomain<ID>, ID extends Serializable> {

    protected final JpaRepository<E, ID> repository;
    protected final AbstractMapper<E, D> mapper;

    @Transactional
    public D save(D domain) {
        E entity = null;
        if (domain.getId() != null) {
            entity = repository.getOne(domain.getId());
        }
        entity = mapper.toEntity(domain);
        return mapper.toDomain(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<D> fetch() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(D domain) {
        repository.delete(domain.getId());
    }
}
