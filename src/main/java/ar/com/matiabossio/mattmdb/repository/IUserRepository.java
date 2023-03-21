package ar.com.matiabossio.mattmdb.repository;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/***************************************************************************************************
     As this is an interface we don't have to @Repository and also we don't have to create the class
     because, when injected, the framework makes the implementation by us (that one has the @Repository)
    ****************************************************************************************************/
public interface IUserRepository extends JpaRepository <User, Integer> {

    // SELECT * FROM users WHERE email = ?          // SQL query
    @Query("FROM User user WHERE user.email = ?1")  // HQL query: ?1 corresponds to the 1st parameter received in the method
    Optional<User> findByEmail(String email);

    Optional<User> findUserByEmailAndPassword(String email, String password);

    @Query("SELECT COUNT(m) > 0 FROM User u JOIN u.favorites m WHERE u.userId = :userId AND m.mediaId = :mediaId")
    boolean isFavorite(@Param("userId") Integer userId, @Param("mediaId") Integer mediaId);


}
