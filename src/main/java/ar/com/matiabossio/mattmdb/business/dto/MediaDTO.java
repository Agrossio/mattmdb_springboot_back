package ar.com.matiabossio.mattmdb.business.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Response Media", description = "Media sent in the response to the client")
public class MediaDTO {
    private Integer mediaId;
    private String mediaType;
    private String name;
    private String title;
    private String overview;
    private String poster_path;
    private boolean adult;
    private String backdrop_path;
    private String first_air_date;
    private String release_date;
    private List<Integer> genre_ids;
    private List<String> origin_country;
    private String original_language;
    private String original_name;
    private int popularity;
    private int vote_average;
    private int vote_count;
    private String tagline;

}
