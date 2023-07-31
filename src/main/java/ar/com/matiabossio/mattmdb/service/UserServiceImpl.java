package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;

import ar.com.matiabossio.mattmdb.business.dto.LoginFromRequestDTO;
import ar.com.matiabossio.mattmdb.business.dto.PasswordFromRequestDTO;
import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import ar.com.matiabossio.mattmdb.business.dto.UserFromRequestDTO;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IUserMapper;
import ar.com.matiabossio.mattmdb.exception.NotFoundException;
import ar.com.matiabossio.mattmdb.repository.IMediaRepository;
import ar.com.matiabossio.mattmdb.repository.IUserRepository;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


import javax.transaction.Transactional;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService{
    // Inject it as "final" to force the ide to add it in the constructor:
    private final IUserRepository userRepository;
    private  final IUserMapper userMapper;
    private final IMediaRepository mediaRepository;


    public UserServiceImpl(IUserRepository userRepository, IUserMapper userMapper, IMediaRepository mediaRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.mediaRepository = mediaRepository;
    }

    // OK
    @Override
    public List<User> getUsersService() {

        // findAll from the CRUD repository returns an iterable, so I need to cast it:
        return this.userRepository.findAll();
    }

    // OK
    @Override
    @Transactional
    public User createUserService(User userFromRequest) throws DataIntegrityViolationException {

        if (userExists(userFromRequest.getEmail())){
            throw new DataIntegrityViolationException(String.format("Email %s already in use.", userFromRequest.getEmail()));
        }

        if (userExistsUsername(userFromRequest.getUsername())){
            throw new DataIntegrityViolationException(String.format("Username %s already in use.", userFromRequest.getUsername()));
        }

        try {

            // save works as saveOrCreate:
            User createdUser = this.userRepository.save(userFromRequest);

            return createdUser;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    // OK
    @Override
    public User getUserByIdService(Integer userId) throws NotFoundException {

        if (userId == null) throw new DataIntegrityViolationException("must provide a userId");

        User foundUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User %s not found!", userId)));

        try {
            return foundUser;
        } catch (RuntimeException e) {
            //if (e instanceof JDBCConnectionException) {
                throw new JDBCConnectionException(e.getMessage(), new SQLException());
            //} else {
        }/* catch (RuntimeException e2) {
            throw new RuntimeException(e2);
        }*/


    /*
     We use findFirst or findAny when we want to return one object.
     If we want to return a List we have to use ".collect(Collectors.toList())"
    */

    }

    @Override
    public Optional<User> getUserByEmailService(String emailFromRequest) {

        Optional<User> foundUser = this.userRepository.findByEmail(emailFromRequest);

        return foundUser;
    }


    @Override
    public boolean userExists(String emailFromRequest) {

        boolean exists = this.userRepository.findByEmail(emailFromRequest).isPresent();

        return exists;

    }

    public boolean userExistsUsername(String usernameFromRequest) {

        boolean exists = this.userRepository.findUserByUsername(usernameFromRequest).isPresent();

        return exists;

    }

    // OK
    @Override
    @Transactional      // takes a snapshot of the DB before writing and if an error occurs it makes a rollback
    public User updateUserService(int userId, User userFromRequest) throws HttpClientErrorException {

        // Search user in the DB (to get it's userId & validate password):
        User userToUpdate = this.userRepository.findById(userId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format("User ID %s doesn't exist.", userId)));


        // If passwords don't match:
        if (!userToUpdate.getPassword().equals(userFromRequest.getPassword())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Please, check your password.");
        }

        // Overwrite the other fields with the info from the request:
        userToUpdate.setUsername(userFromRequest.getUsername());
        userToUpdate.setFavorites(userFromRequest.getFavorites());

        // For now, I don't want to be able to update email & password:
        // userToUpdate.setEmail(userFromRequest.getEmail());
        // userToUpdate.setPassword(userFromRequest.getPassword());

        // Update user in th DB (save works as updateOrCreate).
        User updatedUser = this.userRepository.save(userToUpdate);

        try {
            return updatedUser;
        } catch (JDBCConnectionException e) {
           // if (e instanceof JDBCConnectionException) {
                throw new JDBCConnectionException("Database Unavailable", new SQLException());
           // } else {

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // OK
    @Override
    @Transactional      // takes a snapshot of the DB before writing and if an error occurs it makes a rollback
    public void deleteUserService(Integer userIdFromRequest, PasswordFromRequestDTO passwordFromRequest) throws HttpClientErrorException {

        // Another way to check if user exists is to use an "Optional":

       Optional<User> oFoundUser = this.userRepository.findById(userIdFromRequest);

       if (oFoundUser.isEmpty()){
           throw new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format("User ID %s doesn't exist.", userIdFromRequest));
       } else {
           User foundUser = oFoundUser.get();
           if (foundUser.getPassword().equals(passwordFromRequest.getPassword())){
               this.userRepository.delete(foundUser);
           } else {
               throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Please, check your password.");
           }
       }

    }

    // OK
    @Override
    public User loginUserService(LoginFromRequestDTO loginUserFromRequestDTO) throws HttpClientErrorException {
        // LoginUserFromRequest only has email & password

        User foundUser = this.getUserByEmailService(loginUserFromRequestDTO.getEmail()).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Please check your credentials."));

        if (foundUser.getPassword().equals(loginUserFromRequestDTO.getPassword())){
            return foundUser;
        }

        throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Please check your credentials.");

    }

    // OK
    @Override
    public User addTofavorites(int userId, Media favorite) {

            User foundUser = this.userRepository.findById(userId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format("User ID %s not found.", userId)));


            Optional<Media> oFavoriteToAdd = this.mediaRepository.findById(favorite.getMediaId());
            User updatedUser;

            // If the media is not in the media Table, add it to the DB:
            if (oFavoriteToAdd.isEmpty()) {
                this.mediaRepository.save(favorite);
            }

            // Check if this media is already a favorite of the user:

            boolean isFavorite = userRepository.isFavorite(userId, favorite.getMediaId());

            if (!isFavorite) {

                // add favorite to the User instance
                foundUser.getFavorites().add(favorite);

                // update user in DB:
                updatedUser = userRepository.save(foundUser);

            } else {

                // remove favorite from the User instance
                boolean bandera = foundUser.getFavorites().remove(oFavoriteToAdd.get());

                // update user in DB:
                updatedUser = userRepository.save(foundUser);

            }

            return updatedUser;
    }

    @Override
    public int countFavorites(int userId) {

        User foundUser = this.userRepository.findById(userId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format("User ID %s not found.", userId)));

        return foundUser.getFavorites().size();

    }

}
