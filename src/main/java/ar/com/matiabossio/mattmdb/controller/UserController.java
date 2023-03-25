package ar.com.matiabossio.mattmdb.controller;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.ToggleFavoriteDTO;
import ar.com.matiabossio.mattmdb.business.dto.UserDTO;
import ar.com.matiabossio.mattmdb.business.dto.RegisterUserDTO;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IMediaMapper;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IRegisterUserMapper;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IToggleFavoriteMapper;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IUserMapper;

import ar.com.matiabossio.mattmdb.service.UserServiceImpl;
import ar.com.matiabossio.mattmdb.util.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
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
@Api(tags = "User Controller", description = "Allowed actios for the User Entity")
public class UserController {
    private final IUserMapper userMapper;    // set it as final to force me to add it to the constructor
    private final IRegisterUserMapper registerUserMapper;
    private final UserServiceImpl userService;
    private final IMediaMapper mediaMapper;
    private final IToggleFavoriteMapper toggleFavoriteMapper;

    // IMPORTANT: always inject the interface (not the class), but the class hass to be decorated with @Bean/Component/Service/Repository/Controller/Etc
    public UserController(IUserMapper userMapper, IRegisterUserMapper registerUserMapper, UserServiceImpl userService, IMediaMapper mediaMapper, IToggleFavoriteMapper toggleFavoriteMapper) {
        this.userMapper = userMapper;
        this.registerUserMapper = registerUserMapper;
        this.userService = userService;
        this.mediaMapper = mediaMapper;
        this.toggleFavoriteMapper = toggleFavoriteMapper;
    }

    /*************************************
     *            /api/v2/users          *
     *************************************/
@GetMapping()
@ApiOperation(value = "Get all Users", notes = "This endpoint lists all the users stored in the database", tags = {"user", "get"})
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
    @ApiOperation(value = "Register User", notes = "This endpoint creates a new user account.", tags = {"user", "post"})
    public ResponseEntity<?> createUser (@Valid @RequestBody RegisterUserDTO registerUserDTO, BindingResult result) {

        Message body;
        User createdUser;

        // example to send headers:
        HttpHeaders headers = new HttpHeaders();
        headers.set("Test Key 1", "Test Value 1");

        // Validate registerUserDTO:

        if (result.hasErrors()){

            Map<String, String> validations = new HashMap<>();

            result.getFieldErrors().forEach(validation -> {
                validations.put(validation.getField(), validation.getDefaultMessage());

            });

            body = new Message("Sign up", "Input Error", 400, false, validations);

            return ResponseEntity.badRequest().body(body);

        }

        try {

            User userFromRequest =  registerUserMapper.dtoToEntity(registerUserDTO);

            createdUser = this.userService.createUserService(userFromRequest);
            UserDTO createdUserDTO = this.userMapper.entityToDto(createdUser);


            body = new Message("Sign up", String.format("Welcome %s!!", userFromRequest.getUsername()), 201, true, createdUserDTO);

            // Headers argument is optional:
            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

        } catch (RuntimeException ex) {

            // log error:
            log.warn(ex.getMessage());

            body = new Message("Sign up", ex.getMessage(), 409, false);

            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

    }


    /*************************************
     *    /api/v2/users/:userId          *
     *************************************/

    @GetMapping("/{userId}")
    @ApiOperation(value = "Find User by ID", notes = "This endpoint searches a user by providing a userId in the url.", tags = {"user", "get"})
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
    @ApiOperation(value = "Update User", notes = "This endpoint updates an existing user by providing the userId in the url and the user to update in the body of the request (it must include the password), if user doesn't exist it returns a 404 not found status. It also validates if the provided password is the one stored in the user account.", tags = {"user", "put"})
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
            log.error(ex.getMessage());
            body = new Message("Update User", ex.getMessage(), ex.getStatusCode().value(), false);

            return ResponseEntity.status(ex.getStatusCode()).body(body);
        }

    }


    /*************************************
     * DELETE  /api/v2/users/:userId    *
     *************************************/

    @DeleteMapping("/{userId}")
    @ApiOperation(value = "Delete User", notes = "This endpoint deletes an existing user by providing the userId in the url and the user to delete in the body of the request (it must include the password), if user doesn't exist it returns a 404 not found status. It also validates if the provided password is the one stored in the user account.", tags = {"user", "delete"})
    // if path params name equals the argument name we don't need to use name inside @PathVariable
    public ResponseEntity deleteUser(@PathVariable(name = "userId") Integer userId, @RequestBody User userFromRequest) {

        Message body;

        System.out.println("DELETE BODY ------------------" + userFromRequest);

        try {

            this.userService.deleteUserService(userId, userFromRequest);

            // Get rid of user password:
            //UserDTO deletedUserDTO = this.userMapper.entityToDto(deletedUser);

            userFromRequest.setPassword("");

            body = new Message("Delete User", String.format("User %s deleted OK", userFromRequest.getUsername()), 200, true, userFromRequest);

            return ResponseEntity.ok(body);

        } catch (HttpClientErrorException ex) {

            // log error:
            log.error(ex.getMessage());
            body = new Message("Delete User", ex.getMessage(), ex.getStatusCode().value(), false);

            return ResponseEntity.status(ex.getStatusCode()).body(body);
        }
    }


    /*************************************
     *  LOGIN   /api/v2/users/login      *
     *************************************/

    @PostMapping("/login")
    @ApiOperation(value = "Login User", notes = "This endpoint returns an existing user by providing the users email and password in the body of the request (it must include the password), if user doesn't exist it returns a 404 not found status. It also validates if the provided password is the one stored in the user account.", tags = {"user", "post"})
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
            log.error(ex.getMessage());
            body = new Message("Login", ex.getMessage(), ex.getStatusCode().value(), false);

            return ResponseEntity.status(ex.getStatusCode()).body(body);
        }

    }

    /**************************************************
     * TOGGLE FAVORITE  /api/v2/users/favorites/:userId  *
     **************************************************/

    @PostMapping("/favorites/{userId}")
    @ApiOperation(value = "Add/Remove favorites", notes = "This endpoint toggles a favorite on a users account. If the favorite is not on the user account it will add it to the list, if it's already on the list, then it will remove it", tags = {"user", "toggle", "post", "delete", "favorites"})
    // if path params name equals the argument name we don't need to use name inside @PathVariable
    public ResponseEntity addFavorite(@PathVariable(name = "userId") Integer userId, @RequestBody ToggleFavoriteDTO favoriteFromRequestDTO) {

        System.out.println("FAVORITE DTO-------" + favoriteFromRequestDTO);

        Media favoriteFromRequest = this.toggleFavoriteMapper.dtoToEntity(favoriteFromRequestDTO);

        System.out.println("FAVORITE-------" + favoriteFromRequest);

        Message body;

        try {

            // Count favorite quantity:
            int preFavoriteCount = this.userService.countFavorites(userId);

            User updatedUser = this.userService.addTofavorites(userId, favoriteFromRequest);

            // Count new favorite quantity:
            int afterFavoriteCount = (int) updatedUser.getFavorites().stream().count();

            // Get rid of fan info:
            UserDTO updatedUserDTO = this.userMapper.entityToDto(updatedUser);

            String title = favoriteFromRequest.getTitle() == null ? favoriteFromRequest.getName() : favoriteFromRequest.getTitle();

            // Added favorite message:
            if (preFavoriteCount < afterFavoriteCount) {

                body = new Message("Add Favorite", String.format("%s added to your favorites", title), 201, true, updatedUserDTO);

                return ResponseEntity.status(HttpStatus.CREATED).body(body);

            } else {

            // Removed favorite message:

                body = new Message("Remove Favorite", String.format("%s removed from favorites", title), 200, true, updatedUserDTO);

                return ResponseEntity.ok(body);
            }


        } catch (HttpClientErrorException ex) {

            // log error:
            log.error(ex.getMessage());
            body = new Message("Add Favorite", ex.getMessage(), ex.getStatusCode().value(), false);

            return ResponseEntity.status(ex.getStatusCode()).body(body);
        }
    }

}
