package ar.com.matiabossio.mattmdb.business.dto;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer userId;
    private String username;
    private String email;
    private List<Media> favorites;

}
