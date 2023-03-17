package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getUsersService();
    Optional<User> getUserByIdService(Integer userId);
    Optional<User> getUserByEmailService(String emailFromRequest);
    User createUserService(User userFromRequest);
    User updateUserService(int userId, User userFromRequest);
    Optional<User> deleteUserService(Integer userIdFromRequest);
    Optional<User> loginUserService(User userFromRequest);
    boolean userExists(String emailFromRequest);


}
