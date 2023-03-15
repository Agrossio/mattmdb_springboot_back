package ar.com.matiabossio.mattmdb.repository;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import org.springframework.data.repository.PagingAndSortingRepository;


/***************************************************************************************************
 As this is an interface we don't have to @Repository and also we don't have to create the class
 because, when injected, the framework makes the implementation by us (that one has the @Repository)
 ****************************************************************************************************/
public interface IMediaRepository extends PagingAndSortingRepository<Media, Integer> {



}
