package ar.com.matiabossio.mattmdb.business.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Response User", description = "User sent in the response to the client")
public class UserDTO {
    private Integer userId;
    private String username;
    private String email;
    private List<MediaDTO> favorites; // to avoid sending the fans in the response.

}
