package ar.com.matiabossio.mattmdb.business.dto.mapper;

import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.RegisterUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IRegisterUserMapper extends IEntityMapper<RegisterUserDTO, User> {

    // extends from EntityMapper in order to use lists of Users

    RegisterUserDTO entityToDto(User entity);

    User dtoToEntity(RegisterUserDTO registerUserDto);
}
