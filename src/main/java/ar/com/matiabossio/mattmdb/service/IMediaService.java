package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IMediaService {

    List<Media> getMediaService();
    Optional<Media> getMediaByIdService(Integer mediaId);
    Media createMediaService(Media mediaFromRequest);
    Media updateMediaService(int mediaId, Media mediaFromRequest);
    Optional<Media> deleteMediaService(Integer mediaFromRequest);
    boolean mediaExists(int mediaId);
    Page<Media> getMediaPaginated(Pageable pageable);
    List<Media> getFavorites(User user);

}
