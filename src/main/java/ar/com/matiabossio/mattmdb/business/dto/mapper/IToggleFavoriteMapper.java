package ar.com.matiabossio.mattmdb.business.dto.mapper;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.dto.MediaDTO;
import ar.com.matiabossio.mattmdb.business.dto.ToggleFavoriteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface IToggleFavoriteMapper extends IEntityMapper<ToggleFavoriteDTO, Media> {

    // extends from EntityMapper in order to use lists of Media


    @Mappings({
            @Mapping(source = "mediaId", target = "id"),
            @Mapping(source = "mediaType", target = "media_type")
    })
    ToggleFavoriteDTO entityToDto(Media entity);

    @Mappings({
            @Mapping(source = "id", target = "mediaId"),
            @Mapping(source = "media_type", target = "mediaType")
    })
    Media dtoToEntity(ToggleFavoriteDTO mediaDto);
}
