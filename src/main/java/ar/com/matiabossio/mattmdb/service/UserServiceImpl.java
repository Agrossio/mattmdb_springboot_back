package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;

import ar.com.matiabossio.mattmdb.repository.IUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService{
    // Inject it as "final" to force the ide to add it in the constructor:
    private final IUserRepository userRepository;


    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
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

        Optional<User> foundUser = this.userRepository.findById(userId);

    /*
     We use findFirst or findAny when we want to return one object.
     If we want to return a List we have to use ".collect(Collectors.toList())"
    */

        return foundUser;
    }

    // OK
    @Override
    public Optional<User> getUserByEmail(String emailFromRequest) {

        Optional<User> foundUser = this.userRepository.findByEmail(emailFromRequest);

        return foundUser;
    }

    // OK
    @Override
    public boolean userExists(String emailFromRequest) {

        boolean exists = this.userRepository.findByEmail(emailFromRequest).isPresent();

        return exists;

    }

    @Override
    public User updateUserService(int userId, User userFromRequest) {

        User userToUpdate;

        Optional<User> oFoundUser = this.userRepository.findById(userId);

        if (oFoundUser.isPresent()){

            // save the foundUser into updatedUser (only to get it's userId).
            userToUpdate = oFoundUser.get();
            // overwrite the other fields with the info from the request:
            userToUpdate.setUsername(userFromRequest.getUsername());
            userToUpdate.setEmail(userFromRequest.getEmail());

        } else {
            throw new RuntimeException(String.format("User with ID %s doesn't exists.", userId));
        }

        // Update user in th DB (save works as updateOrCreate).
        User updatedUser = this.userRepository.save(userToUpdate);

        return updatedUser;
    }

    @Override
    public Optional<User> deleteUserService(Integer userIdFromRequest) {
        return Optional.empty();
    }

    @Override
    public Optional<User> loginUserService(User userFromRequest) {
        return Optional.empty();
    }





}
