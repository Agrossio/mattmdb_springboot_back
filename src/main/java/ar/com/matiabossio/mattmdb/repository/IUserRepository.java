package ar.com.matiabossio.mattmdb.repository;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/***************************************************************************************************
     As this is an interface we don't have to @Repository and also we don't have to create the class
     because, when injected, the framework makes the implementation by us (that one has the @Repository)
    ****************************************************************************************************/
public interface IUserRepository extends PagingAndSortingRepository<User, Integer> {

    // SELECT * FROM users WHERE email = ?          // SQL query
    @Query("FROM User user WHERE user.email = ?1")  // HQL query: ?1 corresponds to the 1st parameter received in the method
    Optional<User> findByEmail(String email);


}
