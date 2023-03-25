package ar.com.matiabossio.mattmdb.business.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Column(length = 45, nullable = true)
    private String name;
    @Column(length = 45, nullable = true)
    private String title;
    @Column(length = 2000, nullable = true)
    private String overview;
    @Column(length = 2000, nullable = true)
    private String poster_path;
    @Column(nullable = true)
    private boolean adult;
    @Column(length = 2000, nullable = true)
    private String backdrop_path;
    @Column(length = 16, nullable = true)
    private String first_air_date;
    @Column(length = 16, nullable = true)
    private String release_date;
    @Column(nullable = true)
    private double popularity;
    @Column(nullable = true)
    private double vote_average;
    @Column(nullable = true)
    private int vote_count;
    @Column(nullable = true)
    private String tagline;

    // @Transient                       // Ignore this field
    // Relation type. "mappedBy" references the attribute from the other entity that refers to this Entity (goes only in the not owner of the relationship). FetchType.LAZY saves us from getting into an infinite loop (in toMany ending relationships) when making a SELECT * (it doesn't bring all Users when requesting a Media, it doesn't make the INNER JOIN), it's at JPA level:
    @ManyToMany(
            mappedBy = "favorites",
            fetch = FetchType.LAZY)
    @JsonIgnoreProperties("favorites")   // Tell Jackson to ignore "favorites" prop to avoid infinite loop when creating the json from the getter (it's at Jackson level when sending the response).
    private List<User> fans;
}
