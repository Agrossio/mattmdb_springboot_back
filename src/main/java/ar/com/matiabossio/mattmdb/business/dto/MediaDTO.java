package ar.com.matiabossio.mattmdb.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO {
    private Integer mediaId;
    private String mediaType;

}
