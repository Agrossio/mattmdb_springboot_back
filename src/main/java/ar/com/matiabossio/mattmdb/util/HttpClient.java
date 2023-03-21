package ar.com.matiabossio.mattmdb.util;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpClient {

    private String trendingUrl = "https://api.themoviedb.org/3/trending/all/day?api_key=e65c4db5bae2b9b0565c97b1e317145e&page=1";
    private int mediaId;
    private RestTemplate httpClient = new RestTemplate();

    public Media getMedia(int mediaId ) {

        // This class is an example of how to get media from TMDB API

        ResponseEntity<Media> foundMedia = this.httpClient.exchange(this.getTrendingUrl(), HttpMethod.GET, null, Media.class);


        System.out.println(foundMedia.getBody());
        System.out.println(foundMedia.getStatusCode());

        return foundMedia.getBody();

    }



}
