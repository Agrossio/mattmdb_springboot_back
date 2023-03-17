package ar.com.matiabossio.mattmdb.business.dto.mapper;

import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IEntityMapper<D, E> {
    E dtoToEntity(D dto);

    D entityToDto(E entity);

    List<E> dtoToEntity(List<D> dtoList);

    List<D> entityToDto(List<E> entityList);

}
