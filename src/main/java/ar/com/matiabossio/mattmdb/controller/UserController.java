package ar.com.matiabossio.mattmdb.controller;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")   // allows requests from all origins
@RestController
@RequestMapping("/users")
public class UserController {
    private List<User> users;
    private List<Media> media;

    public UserController(List<User> usersList) {
        this.users = new ArrayList<User>(Arrays
                .asList(
                        new User(1, "Matias", "matias@mail.com", "123456", new ArrayList<Media>(Arrays.asList(new Media(1, "tv", new ArrayList<>())))),
                        new User(2, "Jazmin", "jazmin@mail.com", "123456", new ArrayList<Media>(Arrays.asList())),
                        new User(3, "Victoria", "victoria@mail.com", "123456", new ArrayList<Media>(Arrays.asList())),
                        new User(4, "mariangeles", "mariangeles@mail.com", "123456", new ArrayList<Media>(Arrays.asList()))
                )
        );
        this.media = new ArrayList<Media>(Arrays.asList(
                new Media(1, "TV", new ArrayList<User>())
        ));
    }

    /*************************************
     *            /api/v1/users          *
     *************************************/
@GetMapping()
    public List<User> getUsers() {
        return this.users;
    }

@GetMapping("/{userId}")
    // if path params name equals the argument name we don't need to use name inside @PathVariable
    public User getUserById(@PathVariable(name = "userId") Integer userId) {

    User foundUser = this.users.stream()
            .filter(user -> user.getUserId() == userId)
            .findFirst()
            .orElse(new User());
    /*
     We use findFirst or findAny when the result is one object.
     If the result is a list we have to use ".collect(Collectors.toList())"
    */

    return foundUser;

    }

}
