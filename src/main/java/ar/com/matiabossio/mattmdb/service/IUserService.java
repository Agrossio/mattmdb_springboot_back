package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.business.domain.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getUsersService();
    Optional<User> getUserByIdService(Integer userId);
    User createUserService(User userFromRequest);
    Optional<User> updateUserService(User userFromRequest);
    Optional<User> deleteUserService(Integer userIdFromRequest);
    Optional<User> loginUserService(User userFromRequest);
    boolean userExists(String emailFromRequest);


}
