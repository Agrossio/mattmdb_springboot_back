package ar.com.matiabossio.mattmdb.controller;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.MediaDTO;

import ar.com.matiabossio.mattmdb.business.dto.mapper.IMediaMapper;

import ar.com.matiabossio.mattmdb.service.IMediaService;
import ar.com.matiabossio.mattmdb.service.IUserService;
import ar.com.matiabossio.mattmdb.util.Message;
import io.swagger.annotations.Api;
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

        Las inyecciones de la interfaz del servicio mejor hacerla declarándolas con el constructor que con @Autowired
     */

//@CrossOrigin("http://localhost:4200") // allows requests from "http://localhost:4200"
@CrossOrigin("*")                  // allows requests from all origins
@RestController                    // makes this class a RestController
@RequestMapping("/media")       // makes "/users" the root URL for this controller
@Slf4j
@Api
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
    public ResponseEntity<List<MediaDTO>> getFavorites(@PathVariable String userId) {
        // TODO: Apply Message type to the payload

        // ResponseEntity allows us to customize the response

        // Find the user that corresponds to the userId:
        Optional<User> oFoundUser = this.userService.getUserByIdService(Integer.valueOf(userId));

        if (oFoundUser.isEmpty()){
           return ResponseEntity.notFound().build();
        }

        // get a list of favorites:
        List<Media> mediaPage = this.mediaService.getFavorites(oFoundUser.get());

        List<MediaDTO> mediaPageDTO = this.mediaMapper.entityToDto(mediaPage);

        return ResponseEntity.ok(mediaPageDTO);

    }


    /*************************************
     *   CREATE    /api/v2/media/        *
     *************************************/

    @PostMapping
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
