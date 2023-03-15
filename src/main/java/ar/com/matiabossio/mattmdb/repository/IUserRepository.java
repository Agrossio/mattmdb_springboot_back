package ar.com.matiabossio.mattmdb.repository;

import ar.com.matiabossio.mattmdb.business.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

    /***************************************************************************************************
     As this is an interface we don't have to @Repository and also we don't have to create the class
     because, when injected, the framework makes the implementation by us (that one has the @Repository)
    ****************************************************************************************************/
public interface IUserRepository extends PagingAndSortingRepository<User, Integer> {


}
