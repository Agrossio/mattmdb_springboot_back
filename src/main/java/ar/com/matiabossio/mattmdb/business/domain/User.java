package ar.com.matiabossio.mattmdb.business.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/*
More specific Lombok examples:
@Setter
@Getter
@ToString(exclude = {"password"})
@EqualsAndHashCode(exclude = {
        "id",
        "username"
})
 */

// JPA
@Entity                     // to tell JPA that this class is an entity
@Table(name = "users")      // Gives the name of the table. If we don't use it, JPA will assign the Table the name of the class.

// lombok:
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id                             // indicates this is the PK of the table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // to leave the responsability of generating the id to the DB engine
    @Column(name = "id_user")
    private Integer userId;
    @Column(name = "username", length = 45, nullable = false, unique = true) // OPTIONAL: to customize column & set constraints, if not used it gets the name of the table from the attibute name.
    private String username;
    @Column(name = "email", length = 45, nullable = false, unique = true)
    private String email;
    @Column(name = "password", length = 255, nullable = false)
    private String password;
    // @Transient                       // Ignore this field
    // Relation type. FetchType.LAZY saves us from getting into an infinite loop (in toMany ending relationships) when making a SELECT * (it doesn't bring all media when requesting a User, it doesn't make the INNER JOIN), it's at JPA level::
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {                 // Ver Clase de Spring 09/03 1/2 minuto 1:20:00 aprox
                    CascadeType.ALL
                    //CascadeType.PERSIST
                    //CascadeType.MERGE
            }
    )
    @JoinTable(                         // Allows us to customize the middle table
            name = "favorites",         // Name of the middle table
            // Select the columns of the middle table:
            // Column from this entity:
            joinColumns = @JoinColumn(name = "id_fan", referencedColumnName = "id_user"),    // FK of the owner of the relationship. I use this 2 properties to change the name of the field in this table. If used as below it will give the name of the column like in the PK of users (id_user)
            // Column from the other entity:
            inverseJoinColumns = @JoinColumn(name = "id_media"))    // FK of the other entity
    @JsonIgnoreProperties("fans")   // Tell Jackson to ignore "fans" prop to avoid infinite loop when creating the json from the getter (it's at Jackson level when sending the response).
    private List<Media> favorites;
}
