package ar.com.matiabossio.mattmdb.controller;

import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IUserMapper;

import ar.com.matiabossio.mattmdb.service.UserServiceImpl;
import ar.com.matiabossio.mattmdb.util.Message;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

    /*
        En el controlador se hacen las validaciones de tipo de datos
        En el servicio las de lógica de negocio.

        Todas las excepciones hay que mandarlas al controlador asi enviamos las bad request.

        Las inyecciones de la interfaz del servicio mejor hacerla declarándolas con el constructor que con @Autowired
     */

//@CrossOrigin("http://localhost:4200") // allows requests from "http://localhost:4200"
@CrossOrigin("*")                  // allows requests from all origins
@RestController                    // makes this class a RestController
@RequestMapping("/users")       // makes "/users" the root URL for this controller
@Slf4j
@Api
public class UserController {
    private final IUserMapper userMapper;    // set it as final to force me to add it to the constructor
    private final UserServiceImpl userService;  // with "final" it forces us to initialize it in the constructor

    // IMPORTANT: always inject the interface (not the class), but the class hass to be decorated with @Bean/Component/Service/Repository/Controller/Etc
    public UserController(IUserMapper userMapper, UserServiceImpl userService) {
        this.userMapper = userMapper;

        this.userService = userService;
    }

    /*************************************
     *            /api/v2/users          *
     *************************************/
@GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers() {

    // ResponseEntity allows us to customize the response

    // get a list of users:
    List<User> usersList = this.userService.getUsersService();

    // get rid of password property in all users:
    List<UserDTO> userDTOList = this.userMapper.entityToDto(usersList);

    return ResponseEntity.ok(userDTOList);

    }

    /*************************************
     *  REGISTER    /api/v2/users/       *
     *************************************/

    @PostMapping
    public ResponseEntity<?> createUser (@RequestBody User userFromRequest) {

        // HashMap Message Option:
        // Map<String, Object> body2 = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();

        Message body;
        User createdUser;

        // example to send headers:
        headers.set("Test Key 1", "Test Value 1");

        // Validate if the userFromRequest is empty:
        if (Objects.equals(userFromRequest, new User())) {          // validates if 2 objects are equal
            //  throw new RuntimeException("must provide a user");

          /*
            // With HashMap
            body2.put("ok", Boolean.FALSE);
            body2.put("message", "must provide a user");
          */

            body = new Message("Sign up", "must provide a user", 400, false);

            return ResponseEntity.badRequest().body(body);

        }

        try {

            createdUser = this.userService.createUserService(userFromRequest);
            UserDTO createdUserDTO = this.userMapper.entityToDto(createdUser);


            body = new Message("Sign up", String.format("Welcome %s!! We are glad you trust us for saving your favorite movies & tv shows", userFromRequest.getUsername()), 201, true, createdUserDTO);

           /*
            // with HashMap:
            body2.put("ok", Boolean.TRUE);
            body2.put("message", String.format("Welcome %s!! We are glad you trust us for saving your favorite movies & tv shows", userFromRequest.getUsername()));
            body2.put("title", String.format("Sign up"));
            body2.put("data", createdUser);
            body2.put("statusCode", 201);
           */

            // Headers argument is optional:
            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

        } catch (RuntimeException ex) {
           /*
            // with HashMap:
            body2.put("sucess", Boolean.FALSE);
            body2.put("mensaje", ex.getMessage());
           */

            // log error:
            log.error(ex.getMessage());

            body = new Message("Sign up", ex.getMessage(), 409, false);


            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

    }


    /*************************************
     *    /api/v2/users/:userId          *
     *************************************/

@GetMapping("/{userId}")
    // if path params name equals the argument name we don't need to use name inside @PathVariable
    public ResponseEntity getUserById(@PathVariable(name = "userId") Integer userId) {

        Map<String, Object> body = new HashMap<>();

        // get the user by id:
        Optional<User> optionalFoundUser = this.userService.getUserByIdService(userId);

        // if (userFromRequest == null) throw new RuntimeException("must provide a user");

        if (optionalFoundUser.isEmpty()){

            // throw new RuntimeException("User ID " + userId + " not found");

            body.put("ok", Boolean.FALSE);
            body.put("message", String.format("User ID %s not found", userId));
            body.put("statusCode", 404);



            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);

        } else {
            User foundUser = optionalFoundUser.get();

            // get rid of password property:
            UserDTO foundUserDTO = this.userMapper.entityToDto(foundUser);

            return ResponseEntity.ok(foundUserDTO);

        }
    }


    /*************************************
     *  LOGIN   /api/v2/users/login      *
     *************************************/

    /*
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

    */

}
