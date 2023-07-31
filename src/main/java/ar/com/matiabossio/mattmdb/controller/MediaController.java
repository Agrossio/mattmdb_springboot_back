package ar.com.matiabossio.mattmdb.controller;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.MediaDTO;

import ar.com.matiabossio.mattmdb.business.dto.mapper.IMediaMapper;

import ar.com.matiabossio.mattmdb.exception.NotFoundException;
import ar.com.matiabossio.mattmdb.service.IMediaService;
import ar.com.matiabossio.mattmdb.service.IUserService;
import ar.com.matiabossio.mattmdb.util.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


   /*
        En el controlador se hacen las validaciones de tipo de datos
        En el servicio las de lógica de negocio.

        Todas las excepciones hay que mandarlas al controlador asi enviamos las bad request.

        Las inyecciones de la interfaz del servicio mejor hacerla declarándolas con el constructor que con @Autowired
     */

//@CrossOrigin("http://localhost:4200") // allows requests from "http://localhost:4200"
@CrossOrigin("*")                  // allows requests from all origins
@RestController                    // makes this class a RestController
@RequestMapping("/media")       // makes "/users" the root URL for this controller
@Slf4j
@Api(tags = "Media Controller", description = "Allowed actios for the Media Entity")
public class MediaController {
    private final IMediaService mediaService;
    private final IUserService userService;
    private final IMediaMapper mediaMapper;


    public MediaController(IMediaService mediaService, IUserService userService, IMediaMapper mediaMapper) {
        this.mediaService = mediaService;
        this.userService = userService;
        this.mediaMapper = mediaMapper;
    }

    /*************************************
     *            /api/v2/media          *
     *************************************/

    @GetMapping()
    @ApiOperation(value = "Get all Media", tags = {"media", "get"})
    public ResponseEntity<List<MediaDTO>> getMedia() {

        // ResponseEntity allows us to customize the response

        // get a list of media:
        List<Media> mediaList = this.mediaService.getMediaService();

        List<MediaDTO> mediaDTOList = this.mediaMapper.entityToDto(mediaList);

        // Get rid of fans info:
        return ResponseEntity.ok(mediaDTOList);
    }

    /*************************************
     *     /api/v2/media/paginated       *
     *************************************/

    @GetMapping("/paginated")
    @ApiOperation(value = "Get all Media Paginated", tags = {"media", "pagination", "get"})
    public ResponseEntity<Page<Media>> getMediaPaginated(Pageable pageable) {
        //TODO: Apply Message type to the payload

        // ResponseEntity allows us to customize the response

        // get a list of users:
        Page<Media> mediaPage = this.mediaService.getMediaPaginated(pageable);

        return ResponseEntity.ok(mediaPage);

    }


    /***********************************************
     *        /api/v2/media/favorites/:userId      *
     ***********************************************/


    @GetMapping("/favorites/{userId}")
    @ApiOperation(value = "Get all Favorites from a User", notes = "This endpoint brings all favorites from the userId given in the url.", tags = {"media", "user", "favorites", "get"})
    public ResponseEntity<List<MediaDTO>> getFavorites(@PathVariable String userId) throws NotFoundException {
        // TODO: Apply Message type to the payload

        // ResponseEntity allows us to customize the response

        // Find the user that corresponds to the userId:
        User foundUser = this.userService.getUserByIdService(Integer.valueOf(userId));

        // get a list of favorites:
        List<Media> mediaPage = this.mediaService.getFavorites(foundUser);

        List<MediaDTO> mediaPageDTO = this.mediaMapper.entityToDto(mediaPage);

        return ResponseEntity.ok(mediaPageDTO);

    }


    /*************************************
     *   CREATE    /api/v2/media/        *
     *************************************/

    @PostMapping
    @ApiOperation(value = "Create Media", notes = "This method creates a media.", tags = {"media", "post"})
    public ResponseEntity<?> createMedia(@RequestBody Media mediaFromRequest) {

        Message body;
        Media createdMedia;

        // Validate if mediaFromRequest is empty:
        if (Objects.equals(mediaFromRequest, new Media())) {          // validates if 2 objects are equal
            //  throw new RuntimeException("must provide a media");


            body = new Message("Create Media", "must provide a media", 400, false);

            return ResponseEntity.badRequest().body(body);

        }

        try {
            createdMedia = this.mediaService.createMediaService(mediaFromRequest);

            body = new Message("Create Media", "Media Created OK", 201, true, createdMedia);

            return new ResponseEntity<>(body, HttpStatus.CREATED);

        } catch (RuntimeException ex) {

            // log error:
            log.error(ex.getMessage());

            body = new Message("Create Media", ex.getMessage(), 409, false);


            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

    }
}
