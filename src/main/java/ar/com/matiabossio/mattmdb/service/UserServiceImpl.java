package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService{

    private List<User> users;
    private List<Media> media;

    public UserServiceImpl() {
        this.users = new ArrayList<User>(Arrays
                .asList(
                        new User(1, "Matias", "matias@mail.com", "123456", new ArrayList<Media>(Arrays.asList(new Media(1, "tv", new ArrayList<>())))),
                        new User(2, "Jazmin", "jazmin@mail.com", "123456", new ArrayList<Media>(Arrays.asList())),
                        new User(3, "Victoria", "victoria@mail.com", "123456", new ArrayList<Media>(Arrays.asList())),
                        new User(4, "Mariangeles", "mariangeles@mail.com", "123456", new ArrayList<Media>(Arrays.asList())),
                        new User(5, "Mercedes", "mercedes@mail.com", "123456", new ArrayList<Media>(Arrays.asList()))
                )
        );
        this.media = new ArrayList<Media>(Arrays.asList(
                new Media(1, "TV", new ArrayList<User>())
        ));
    }

    // OK
    @Override
    public List<User> getUsersService() {
        return this.users;
    }

    // OK
    @Override
    public User createUserService(User userFromRequest) throws RuntimeException {

        if (this.userExists(userFromRequest.getEmail())){
            throw new RuntimeException(String.format("Email %s already in use.", userFromRequest.getEmail()));
        }

        this.users.add(userFromRequest);
        User createdUser = this.getUserByIdService(userFromRequest.getUserId()).get();


        return createdUser;
    }

    // OK
    @Override
    public Optional<User> getUserByIdService(Integer userId) {

        if (userId == null) throw new RuntimeException("must provide a userId");

        Optional<User> foundUser = this.users.stream()
                .filter(user -> user.getUserId() == userId)
                .findFirst();

    /*
     We use findFirst or findAny when we want to return one object.
     If we want to return a List we have to use ".collect(Collectors.toList())"
    */

        return foundUser;
    }

    @Override
    public User updateUserService(User userFromRequest) {
        return null;
    }

    @Override
    public Optional<User> deleteUserService(Integer userIdFromRequest) {
        return Optional.empty();
    }

    @Override
    public Optional<User> loginUserService(User userFromRequest) {
        return Optional.empty();
    }

    @Override
    public boolean userExists(String emailFromRequest) {
        return this.users.stream()
                .anyMatch(user -> user.getEmail().equals(emailFromRequest));
    }

    //Another option:
    /*
    private Optional<User> userExists2 (String email){

        Optional<User> optionalUser = this.users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();

        return optionalUser;
    }
    */

}
