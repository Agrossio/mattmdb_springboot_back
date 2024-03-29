package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.LoginFromRequestDTO;
import ar.com.matiabossio.mattmdb.business.dto.PasswordFromRequestDTO;
import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import ar.com.matiabossio.mattmdb.business.dto.UserFromRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getUsersService();
    User getUserByIdService(Integer userId);
    Optional<User> getUserByEmailService(String emailFromRequest);
    User createUserService(User userFromRequest);
    User updateUserService(int userId, User userFromRequest);
    void deleteUserService(Integer userIdFromRequest, PasswordFromRequestDTO passwordFromRequest);
    User loginUserService(LoginFromRequestDTO loginUserFromRequestDTO);
    User addTofavorites(int userId, Media favorite);
    int countFavorites(int userId);
    // void removeFromFavorites(int userId, Media favorite);
    boolean userExists(String emailFromRequest);
    boolean userExistsUsername(String emailFromRequest);


}
