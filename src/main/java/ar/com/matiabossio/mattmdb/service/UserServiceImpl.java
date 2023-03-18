package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.business.domain.User;

import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IUserMapper;
import ar.com.matiabossio.mattmdb.repository.IUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService{
    // Inject it as "final" to force the ide to add it in the constructor:
    private final IUserRepository userRepository;
    private  final IUserMapper userMapper;


    public UserServiceImpl(IUserRepository userRepository, IUserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // OK
    @Override
    public List<User> getUsersService() {

        // findAll from the CRUD repository returns an iterable, so I need to cast it:
        return (List<User>) this.userRepository.findAll();
    }

    // OK
    @Override
    public User createUserService(User userFromRequest) throws RuntimeException {

        if (this.userExists(userFromRequest.getEmail())){
            throw new RuntimeException(String.format("Email %s already in use.", userFromRequest.getEmail()));
        }

        // save works as saveOrCreate:
        User createdUser = this.userRepository.save(userFromRequest);

        return createdUser;
    }

    // OK
    @Override
    public Optional<User> getUserByIdService(Integer userId) {

        if (userId == null) throw new RuntimeException("must provide a userId");

        Optional<User> oFoundUser = this.userRepository.findById(userId);

        return oFoundUser;


    /*
     We use findFirst or findAny when we want to return one object.
     If we want to return a List we have to use ".collect(Collectors.toList())"
    */


    }

    // OK
    @Override
    public Optional<User> getUserByEmailService(String emailFromRequest) {

        Optional<User> foundUser = this.userRepository.findByEmail(emailFromRequest);

        return foundUser;
    }

    // OK
    @Override
    public boolean userExists(String emailFromRequest) {

        boolean exists = this.userRepository.findByEmail(emailFromRequest).isPresent();

        return exists;

    }

    // OK
    @Override
    public User updateUserService(int userId, User userFromRequest) throws HttpClientErrorException{

        User userToUpdate;

        // Search user in the DB:
        Optional<User> oFoundUser = this.userRepository.findById(userId);

        if (oFoundUser.isEmpty()){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format("User ID %s doesn't exist.", userId));
        }

        // Save the foundUser into userToUpdate (to get it's userId & validate password).
        userToUpdate = oFoundUser.get();

        // If passwords don't match:
        if (!userToUpdate.getPassword().equals(userFromRequest.getPassword())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Please, check your password.");
        }

        // Overwrite the other fields with the info from the request:
        userToUpdate.setUsername(userFromRequest.getUsername());
        userToUpdate.setEmail(userFromRequest.getEmail());

        // Update user in th DB (save works as updateOrCreate).
        User updatedUser = this.userRepository.save(userToUpdate);

        return updatedUser;
    }

    // OK
    @Override
    public User deleteUserService(Integer userIdFromRequest, User userFromRequest) throws HttpClientErrorException {

       Optional<User> oFoundUser = this.userRepository.findById(userIdFromRequest);

       if (oFoundUser.isEmpty()){
           throw new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format("User ID %s doesn't exist.", userIdFromRequest));
       } else {
           User foundUser = oFoundUser.get();
           if (foundUser.getPassword().equals(userFromRequest.getPassword())){
               this.userRepository.delete(foundUser);
               return foundUser;
           } else {
               throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Please, check your password.");
           }
       }

    }

    // OK
    @Override
    public User loginUserService(User userFromRequest) throws HttpClientErrorException {
        // userFromRequest only has email & password

        Optional<User> oFoundUser = this.getUserByEmailService(userFromRequest.getEmail());

        if (oFoundUser.isEmpty()){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Please check your credentials.");
        }

        User foundUser = oFoundUser.get();

        if (!foundUser.getPassword().equals(userFromRequest.getPassword())){
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Please check your credentials.");
        }

        return foundUser;
    }

}
