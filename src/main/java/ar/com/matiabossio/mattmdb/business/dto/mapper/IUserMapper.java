package ar.com.matiabossio.mattmdb.business.dto.mapper;

import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import ar.com.matiabossio.mattmdb.controller.UserController;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;


@Mapper(componentModel = "spring")
public interface IUserMapper extends IEntityMapper<UserDTO, User> {

    // extends from EntityMapper in order to use lists of Users

    UserDTO entityToDto(User entity);

    User dtoToEntity(UserDTO userDto);

}
