package ar.com.matiabossio.mattmdb.business.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

// JPA
@Entity                     // to tell JPA that this class is an entity
@Table(name = "media")      // Gives the name of the table. If we don't use it, JPA will assign the Table the name of the class.

// lombok:
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Media {
    @Id                             // indicates this is the PK of the table
    @Column(name = "id_media")
    private Integer mediaId;
    @Column(name = "media_type", length = 16, nullable = false) // OPTIONAL: to customize column & set constraints, if not used it gets the name of the table from the attibute name.
    private String mediaType;
    // @Transient                       // Ignore this field
    // Relation type. "mappedBy" references the attribute from the other entity that refers to this Entity (goes only in the not owner of the relationship). FetchType.LAZY saves us from getting into an infinite loop (in toMany ending relationships) when making a SELECT * (it doesn't bring all Users when requesting a Media):
    @ManyToMany(
            mappedBy = "favorites",
            fetch = FetchType.LAZY)
    private List<User> fans;
}
