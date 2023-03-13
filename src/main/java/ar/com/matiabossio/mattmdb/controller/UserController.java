package ar.com.matiabossio.mattmdb.controller;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IUserMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//@CrossOrigin("http://localhost:4200") // allows requests from "http://localhost:4200"
@CrossOrigin("*")                  // allows requests from all origins
@RestController                    // makes this class a RestController
@RequestMapping("/users")       // makes "/users" the root URL for this controller
public class UserController {
    private IUserMapper userMapper;    // set it as final to force me to add it to the constructor
    private List<User> users;
    private List<Media> media;

    // IMPORTANT: always inject the interface (not the class), but the class hass to be decorated with @Bean/Component/Service/Repository/Controller/Etc
    public UserController(List<User> usersList, IUserMapper userMapper) {
        this.userMapper = userMapper;
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

    /*************************************
     *            /api/v2/users          *
     *************************************/
@GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers() {

    // ResponseEntity allows us to customize the response

    return ResponseEntity.ok(this.userMapper.entityToDto(this.users));

    }


    /*************************************
     *  REGISTER    /api/v2/users/       *
     *************************************/

    @PostMapping
    public ResponseEntity<?> createUser (@RequestBody User userFromRequest) {

        Map<String, Object> body = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();

        // to send headers:
        headers.set("Test Key 1", "Test Value 1");

        // Validate if the userFromRequest is empty:
        if (Objects.equals(userFromRequest, new User())) {          // validates if 2 objects are equal
            //  throw new RuntimeException("must provide a user");

            body.put("ok", Boolean.FALSE);
            body.put("message", "must provide a user");

            return ResponseEntity.badRequest().body(body);

        }

        if (this.userExists(userFromRequest.getEmail()).isPresent()){
            //  throw new RuntimeException("User " + userFromRequest.getEmail() + " already exists");

            body.put("ok", Boolean.FALSE);
            body.put("message", String.format("Email %s already in use", userFromRequest.getEmail()));

            return ResponseEntity.badRequest().body(body);
        }

        this.users.add(userFromRequest);
        UserDTO createdUser = this.getUserByEmail(userFromRequest);

        body.put("ok", Boolean.TRUE);
        body.put("message", String.format("Welcome %s!! We are glad you trust us for saving your favorite movies & tv shows", userFromRequest.getUsername()));
        body.put("title", String.format("Sign up"));
        body.put("data", createdUser);

        // Headers argument is optional:
        return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

    }

    private Optional<User> userExists (String email){

        Optional<User> optionalUser = this.users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();

        return optionalUser;
    }


    /*************************************
     *    /api/v2/users/:userId          *
     *************************************/

@GetMapping("/{userId}")
    // if path params name equals the argument name we don't need to use name inside @PathVariable
    public ResponseEntity getUserById(@PathVariable(name = "userId") Integer userId) {

    if (userId == null) throw new RuntimeException("must provide a userId");

    User foundUser = this.users.stream()
            .filter(user -> user.getUserId() == userId)
            .findFirst()
            .orElse(new User());
    /*
     We use findFirst or findAny when we want to return one object.
     If we want to return a List we have to use ".collect(Collectors.toList())"
    */

    return ResponseEntity.ok(this.userMapper.entityToDto(foundUser));

    }


    /*************************************
     *  LOGIN   /api/v2/users/login      *
     *************************************/

    @PostMapping("/login")
    public UserDTO getUserByEmail(@RequestBody User userFromRequest){

        if (userFromRequest == null) throw new RuntimeException("must provide a user");

        User foundUser = this.users.stream()
                .filter(user -> user.getEmail().equals(userFromRequest.getEmail()))
                .findFirst()
                .orElse(new User());

        // Use UserDTO to avoid returning the password:
        UserDTO foundUserDto = this.userMapper.entityToDto(foundUser);

        return foundUserDto;

    }



}
