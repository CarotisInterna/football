package ru.vsu.football.mapper;

public interface AbstractMapper<E, D> {
    E toEntity(D domain);

    D toDomain(E entity);
}
