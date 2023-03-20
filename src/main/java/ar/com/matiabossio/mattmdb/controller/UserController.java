package ar.com.matiabossio.mattmdb.controller;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.MediaDTO;
import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IMediaMapper;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IUserMapper;

import ar.com.matiabossio.mattmdb.service.UserServiceImpl;
import ar.com.matiabossio.mattmdb.util.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

    /*
        En el controlador se hacen las validaciones de tipo de datos
        En el servicio las de lógica de negocio.

        Todas las excepciones hay que mandarlas al controlador asi enviamos las bad request.

        Las inyecciones de la interfaz del servicio mejor hacerla declarándolas con el constructor que con @Autowired
     */

//@CrossOrigin("http://localhost:4200") // allows requests from "http://localhost:4200"
@CrossOrigin("*")                  // allows requests from all origins
@RestController                    // makes this class a RestController
@RequestMapping("/users")       // makes "/users" the root URL for this controller
@Slf4j
@Api(value = "Allowed actios for the User Entity", tags = "User Controller")
public class UserController {
    private final IUserMapper userMapper;    // set it as final to force me to add it to the constructor
    private final UserServiceImpl userService;
    private final IMediaMapper mediaMapper;

    // IMPORTANT: always inject the interface (not the class), but the class hass to be decorated with @Bean/Component/Service/Repository/Controller/Etc
    public UserController(IUserMapper userMapper, UserServiceImpl userService, IMediaMapper mediaMapper) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.mediaMapper = mediaMapper;
    }

    /*************************************
     *            /api/v2/users          *
     *************************************/
@GetMapping()
@ApiOperation(value = "Get all Users")
    public ResponseEntity<List<UserDTO>> getUsers() {

    // TODO: The response sends passwords. See how to stop the getter before it gets the users of the media.

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
    @ApiOperation(value = "Register User")
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


            body = new Message("Sign up", String.format("Welcome %s!!", userFromRequest.getUsername()), 201, true, createdUserDTO);

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
    @ApiOperation(value = "Find User by ID")
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
     *  UPDATE  /api/v2/users/:userId    *
     *************************************/

    @PutMapping("/{userId}")
    @ApiOperation(value = "Update User")
    // if path params name equals the argument name we don't need to use name inside @PathVariable
    public ResponseEntity updateUser(@PathVariable(name = "userId") Integer userId, @RequestBody User userFromRequest) {

        User updatedUser;
        Message body;

        try {
            updatedUser = this.userService.updateUserService(userId, userFromRequest);

            UserDTO updatedUserDTO = this.userMapper.entityToDto(updatedUser);

            body = new Message("Update User", String.format("User %s updated", userFromRequest.getUsername()), 200, true, updatedUserDTO);

            return ResponseEntity.ok(body);

        } catch (HttpClientErrorException ex) {

            // log error:
            log.error(ex.toString());
            body = new Message("Update User", ex.getMessage(), ex.getStatusCode().value(), false);

            return ResponseEntity.status(ex.getStatusCode()).body(body);
        }

    }


    /*************************************
     * DELETE  /api/v2/users/:userId    *
     *************************************/

    @DeleteMapping("/{userId}")
    @ApiOperation(value = "Delete User")
    // if path params name equals the argument name we don't need to use name inside @PathVariable
    public ResponseEntity deleteUser(@PathVariable(name = "userId") Integer userId, @RequestBody User userFromRequest) {

        Message body;

        System.out.println("DELETE BODY ------------------" + userFromRequest);

        try {

            User deletedUser = this.userService.deleteUserService(userId, userFromRequest);

            // Get rid of user password:
            UserDTO deletedUserDTO = this.userMapper.entityToDto(deletedUser);

            body = new Message("Delete User", String.format("User %s deleted OK", deletedUser.getUsername()), 200, true, deletedUserDTO);

            return ResponseEntity.ok(body);

        } catch (HttpClientErrorException ex) {

            // log error:
            log.error(ex.toString());
            body = new Message("Delete User", ex.getMessage(), ex.getStatusCode().value(), false);

            return ResponseEntity.status(ex.getStatusCode()).body(body);
        }
    }


    /*************************************
     *  LOGIN   /api/v2/users/login      *
     *************************************/

    @PostMapping("/login")
    @ApiOperation(value = "Login User")
    public ResponseEntity getUserByEmail(@RequestBody User userFromRequest){

        // userFromRequest only has email & password

        Message body;

        try {

            User foundUser = this.userService.loginUserService(userFromRequest);

            // Get rid of user password:
            UserDTO foundUserDTO = this.userMapper.entityToDto(foundUser);

            body = new Message("Login", String.format("Welcome back %s!!", foundUser.getUsername()), 200, true, foundUserDTO);

            return ResponseEntity.ok(body);

        } catch (HttpClientErrorException ex) {

            // log error:
            log.error(ex.toString());
            body = new Message("Login", ex.getMessage(), ex.getStatusCode().value(), false);

            return ResponseEntity.status(ex.getStatusCode()).body(body);
        }

    }

    /**************************************************
     * ADD FAVORITE  /api/v2/users/favorites/:userId  *
     **************************************************/

    @PostMapping("/favorites/{userId}")
    @ApiOperation(value = "Add to favorites")
    // if path params name equals the argument name we don't need to use name inside @PathVariable
    public ResponseEntity addFavorite(@PathVariable(name = "userId") Integer userId, @RequestBody Media favoriteFromRequest) {

        Message body;

        try {
            User updatedUser = this.userService.addTofavorites(userId, favoriteFromRequest);

            // Get rid of fan info:
            UserDTO updatedUserDTO = this.userMapper.entityToDto(updatedUser);

            // TODO: change media id with media title
            body = new Message("Add Favorite", String.format("Media %s added to your favorites", favoriteFromRequest.getMediaId()), 200, true, updatedUserDTO);

            return ResponseEntity.ok(body);

        } catch (HttpClientErrorException ex) {

            // log error:
            log.error(ex.toString());
            body = new Message("Add Favorite", ex.getMessage(), ex.getStatusCode().value(), false);

            return ResponseEntity.status(ex.getStatusCode()).body(body);
        }

    }


    /*****************************************************
     * DELETE FAVORITE  /api/v2/users/favorites/:userId  *
     *****************************************************/

    @PostMapping("/remove-favorites/{userId}")
    @ApiOperation(value = "Remove from favorites")
    // if path params name equals the argument name we don't need to use name inside @PathVariable
    public ResponseEntity removeFavorite(@PathVariable(name = "userId") Integer userId, @RequestBody Media favoriteFromRequest) {

        Message body;

        try {
            User updatedUser = this.userService.removeFromFavorites(userId, favoriteFromRequest);

            // Get rid of fan info:
            UserDTO updatedUserDTO = this.userMapper.entityToDto(updatedUser);

            // TODO: change media id with media title
            body = new Message("Remove Favorite", String.format("Media %s removed from your favorites", favoriteFromRequest.getMediaId()), 200, true, updatedUserDTO);

            return ResponseEntity.ok(body);

        } catch (HttpClientErrorException ex) {

            // log error:
            log.error(ex.toString());
            body = new Message("Add Favorite", ex.getMessage(), ex.getStatusCode().value(), false);

            return ResponseEntity.status(ex.getStatusCode()).body(body);
        }

    }

}
