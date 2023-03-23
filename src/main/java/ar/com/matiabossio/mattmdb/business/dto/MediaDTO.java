package ar.com.matiabossio.mattmdb.business.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Response Media", description = "Media sent in the response to the client")
public class MediaDTO {
    private Integer mediaId;
    private String mediaType;

}
