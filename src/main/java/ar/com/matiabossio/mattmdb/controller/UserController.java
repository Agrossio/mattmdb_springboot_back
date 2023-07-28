package ar.com.matiabossio.mattmdb.controller;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.*;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IMediaMapper;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IUserFromRequestMapper;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IToggleFavoriteMapper;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IUserMapper;

import ar.com.matiabossio.mattmdb.service.UserServiceImpl;
import ar.com.matiabossio.mattmdb.util.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.*;

    /*
        En el controlador se hacen las validaciones de tipo de datos
        En el servicio las de lógica de negocio.

        Todas las excepciones hay que mandarlas al controlador asi enviamos los mensajes correspondientes.

        Las inyecciones de la interfaz del servicio mejor hacerla declarándolas con el constructor que con @Autowired
     */

//@CrossOrigin("http://localhost:4200") // allows requests from "http://localhost:4200"
@CrossOrigin("*")                  // allows requests from all origins
@RestController                    // makes this class a RestController
@RequestMapping("/users")       // makes "/users" the root URL for this controller
@RequiredArgsConstructor
@Slf4j
@Api(tags = "User Controller", description = "Allowed actions for the User Entity")
public class UserController {
    private final IUserMapper userMapper;    // set it as final to force me to add it to the constructor
    private final IUserFromRequestMapper userFromRequestMapper;
    private final UserServiceImpl userService;
    private final IMediaMapper mediaMapper;
    private final IToggleFavoriteMapper toggleFavoriteMapper;

    // IMPORTANT: always inject the interface (not the class), but the class has to be decorated with @Bean/Component/Service/Repository/Controller/Etc
/*    public UserController(IUserMapper userMapper, IUserFromRequestMapper userFromRequestMapper, UserServiceImpl userService, IMediaMapper mediaMapper, IToggleFavoriteMapper toggleFavoriteMapper) {
        this.userMapper = userMapper;
        this.userFromRequestMapper = userFromRequestMapper;
        this.userService = userService;
        this.mediaMapper = mediaMapper;
        this.toggleFavoriteMapper = toggleFavoriteMapper;
    }*/

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
    public ResponseEntity<?> createUser (@Valid @RequestBody UserFromRequestDTO userFromRequestDTO, BindingResult result) throws MethodArgumentNotValidException, RuntimeException {

        // The way I worked in this method is to know how to do it without using an Exception Handler Class (@ControllerAdvice, @ExceptionHandler) & showing how to send headers

        Message body;
        User createdUser;

        // example to send headers:
        HttpHeaders headers = new HttpHeaders();
        headers.set("Test Key 1", "Test Value 1");


        // Validate userFromRequestDTO:
        // When using an Exception Handler we don't have to use "BindingResult"

        if (result.hasErrors()){

            Map<String, String> validations = new HashMap<>();

            result.getFieldErrors().forEach(validation -> {
                validations.put(validation.getField(), validation.getDefaultMessage());

            });

            body = new Message("Sign up", "Input Error", 400, false, validations);

            return ResponseEntity.badRequest().body(body);

        }

        try {

            User userFromRequest =  userFromRequestMapper.dtoToEntity(userFromRequestDTO);

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

            Message body;

            // get the user by id:
            User foundUser = this.userService.getUserByIdService(userId);

            // get rid of password property:
            UserDTO foundUserDTO = this.userMapper.entityToDto(foundUser);


            body = new Message("Find by id", String.format("User %s found!", userId), 200, true, foundUserDTO);

            return ResponseEntity.ok(body);

        }


    /*************************************
     *  UPDATE  /api/v2/users/:userId    *
     *************************************/

    @PutMapping("/{userId}")
    @ApiOperation(value = "Update User", notes = "This endpoint updates an existing user by providing the userId in the url and the user to update in the body of the request (it must include the password), if user doesn't exist it returns a 404 not found status. It also validates if the provided password is the one stored in the user account.", tags = {"user", "put"})
    // if path params name equals the argument name we don't need to use name inside @PathVariable
    public ResponseEntity updateUser(@PathVariable(name = "userId") Integer userId, @Valid @RequestBody UserFromRequestDTO userFromRequestDTO) throws MethodArgumentNotValidException, RuntimeException, HttpClientErrorException {

        User userFromRequest = userFromRequestMapper.dtoToEntity(userFromRequestDTO);

        User updatedUser = this.userService.updateUserService(userId, userFromRequest);

        UserDTO updatedUserDTO = this.userMapper.entityToDto(updatedUser);

        Message body = new Message("Update User", String.format("User %s updated", userFromRequest.getUsername()), 200, true, updatedUserDTO);

        return ResponseEntity.ok(body);

    }

    /*************************************
     * DELETE  /api/v2/users/:userId    *
     *************************************/

    @DeleteMapping("/{userId}")
    @ApiOperation(value = "Delete User", notes = "This endpoint deletes an existing user by providing the userId in the url and the user to delete in the body of the request (it must include the password), if user doesn't exist it returns a 404 not found status. It also validates if the provided password is the one stored in the user account.", tags = {"user", "delete"})
    // if path params name equals the argument name we don't need to use name inside @PathVariable
    public ResponseEntity deleteUser(@PathVariable(name = "userId") Integer userId, @Valid @RequestBody PasswordFromRequestDTO passwordFromRequestDTO) {

        this.userService.deleteUserService(userId, passwordFromRequestDTO);

        Message body = new Message("Delete User", String.format("User %s deleted OK", userId), 200, true, String.format("Deleted user with ID: %s", userId));

        return ResponseEntity.ok(body);

    }

    /*************************************
     *  LOGIN   /api/v2/users/login      *
     *************************************/

    @PostMapping("/login")
    @ApiOperation(value = "Login User", notes = "This endpoint returns an existing user by providing the users email and password in the body of the request (it must include the password), if user doesn't exist it returns a 404 not found status. It also validates if the provided password is the one stored in the user account.", tags = {"user", "post"})
    public ResponseEntity getUserByEmail(@Valid @RequestBody LoginFromRequestDTO loginUserFromRequestDTO){

        // loginUserFromRequestDTO only has email & password

        User foundUser = this.userService.loginUserService(loginUserFromRequestDTO);

        // Get rid of user password:
        UserDTO foundUserDTO = this.userMapper.entityToDto(foundUser);

        Message body = new Message("Login", String.format("Welcome back %s!!", foundUser.getUsername()), 200, true, foundUserDTO);

        return ResponseEntity.ok(body);

    }

    /**************************************************
     * TOGGLE FAVORITE  /api/v2/users/favorites/:userId  *
     **************************************************/

    @PostMapping("/favorites/{userId}")
    @ApiOperation(value = "Add/Remove favorites", notes = "This endpoint toggles a favorite on a users account. If the favorite is not on the user account it will add it to the list, if it's already on the list, then it will remove it", tags = {"user", "toggle", "post", "delete", "favorites"})
    // if path params name equals the argument name we don't need to use name inside @PathVariable
    public ResponseEntity addFavorite(@PathVariable(name = "userId") Integer userId, @RequestBody ToggleFavoriteDTO favoriteFromRequestDTO) {

        Media favoriteFromRequest = this.toggleFavoriteMapper.dtoToEntity(favoriteFromRequestDTO);

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

                Message body = new Message("Add Favorite", String.format("%s added to your favorites", title), 201, true, updatedUserDTO);

                return ResponseEntity.status(HttpStatus.CREATED).body(body);

            } else {

            // Removed favorite message:

                Message body = new Message("Remove Favorite", String.format("%s removed from favorites", title), 200, true, updatedUserDTO);

                return ResponseEntity.ok(body);
            }
    }

}
