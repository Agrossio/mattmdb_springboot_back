package ar.com.matiabossio.mattmdb.business.dto.mapper;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.MediaDTO;
import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMediaMapper extends IEntityMapper<MediaDTO, Media> {

    // extends from EntityMapper in order to use lists of Media

    MediaDTO entityToDto(Media entity);
    Media dtoToEntity(MediaDTO mediaDto);
}
