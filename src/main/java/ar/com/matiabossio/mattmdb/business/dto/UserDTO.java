package ar.com.matiabossio.mattmdb.business.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer userId;
    private String username;
    private String email;
    private List<MediaDTO> favorites; // to avoid sending the fans in the response.

}
