package ar.com.matiabossio.mattmdb.business.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;
    private String username;
    private String email;
    private String password;
    private List<Media> favorites;
}
