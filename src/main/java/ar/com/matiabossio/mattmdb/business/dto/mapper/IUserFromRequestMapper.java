package ar.com.matiabossio.mattmdb.business.dto.mapper;

import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.UserFromRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserFromRequestMapper extends IEntityMapper<UserFromRequestDTO, User> {

    // extends from EntityMapper in order to use lists of Users

    UserFromRequestDTO entityToDto(User entity);

    User dtoToEntity(UserFromRequestDTO userFromRequestDto);
}
