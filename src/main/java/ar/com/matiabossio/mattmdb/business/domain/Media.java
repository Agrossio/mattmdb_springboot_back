package ar.com.matiabossio.mattmdb.business.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Media {
    private Integer mediaId;
    private String mediaType;
    private List<User> fans;
}
