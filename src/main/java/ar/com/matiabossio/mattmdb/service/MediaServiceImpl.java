package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import ar.com.matiabossio.mattmdb.repository.IMediaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MediaServiceImpl implements IMediaService{

    private final IMediaRepository mediaRepository;

    public MediaServiceImpl(IMediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    // OK
    @Override
    public List<Media> getMediaService() {
        List<Media> mediaList = (List<Media>) this.mediaRepository.findAll();

        return mediaList;
    }

    // OK
    @Override
    public Page<Media> getMediaPaginated(Pageable pageable) {
        return this.mediaRepository.findAll(pageable);
    }

    @Override
    public List<Media> getFavorites(UserDTO userDTO) {
        return this.mediaRepository.findMediaByFansContains(userDTO);
    }

    // OK
    @Override
    public Media createMediaService(Media mediaFromRequest) {
        Media createdMedia = this.mediaRepository.save(mediaFromRequest);
        return createdMedia;
    }

    @Override
    public Optional<Media> getMediaByIdService(Integer mediaId) {
        return Optional.empty();
    }

    @Override
    public Media updateMediaService(int mediaId, Media mediaFromRequest) {
        return null;
    }

    @Override
    public Optional<Media> deleteMediaService(Integer mediaFromRequest) {
        return Optional.empty();
    }

    @Override
    public boolean mediaExists(int mediaId) {
        return false;
    }



}
