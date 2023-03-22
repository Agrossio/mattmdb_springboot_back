package ar.com.matiabossio.mattmdb.data;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;

import java.util.ArrayList;
import java.util.List;

public class DummyData {
    private static User user1 = new User();
    private static User user2 = new User();
    private static User user3 = new User();

    public static Media getMedia1(){

        Media media1 = new Media();

        user1.setUserId(1);
        user1.setUsername("Matias");
        user1.setEmail("matias@mail.com");
        user1.setPassword("123456Aa");

        media1.setMediaId(0);
        media1.setMediaType("movie");
        media1.setFans(new ArrayList<>(List.of(user1)));

        return media1;

/*        return new Media(1, "movie", new ArrayList<>(List.of(new User[]{
                new User(1, "Matias", "matias@mail.com", "123456Aa", new ArrayList<>())
        })));*/
    }
    public static Media getMedia2(){

        Media media2 = new Media();

        user1.setUserId(1);
        user1.setUsername("Matias");
        user1.setEmail("matias@mail.com");
        user1.setPassword("123456Aa");

        user1.setUserId(2);
        user2.setUsername("Jazmin");
        user2.setEmail("jazmin@mail.com");
        user2.setPassword("123456Aa");

        media2.setMediaId(0);
        media2.setMediaType("movie");
        media2.setFans(new ArrayList<>(List.of(user1, user2)));

        return media2;

/*        return new Media(2, "movie", new ArrayList<>(List.of(new User[]{
                new User(1, "Matias", "matias@mail.com", "123456Aa", new ArrayList<>()),
                new User(2, "Jazmin", "jazmin@mail.com", "123456Aa", new ArrayList<>()),
                new User(3, "Victoria", "victoria@mail.com", "123456Aa", new ArrayList<>()),
        })));*/
    }

    public static Media getMedia3(){

        Media media3 = new Media();

        user1.setUserId(0);
        user1.setUsername("Matias");
        user1.setEmail("matias@mail.com");
        user1.setPassword("123456Aa");

        user1.setUserId(0);
        user2.setUsername("Jazmin");
        user2.setEmail("jazmin@mail.com");
        user2.setPassword("123456Aa");

        user1.setUserId(0);
        user3.setUsername("Victoria");
        user3.setEmail("victoria@mail.com");
        user3.setPassword("123456Aa");

        media3.setMediaId(0);
        media3.setMediaType("tv");
        media3.setFans(new ArrayList<>(List.of(user1, user2, user3)));

        return media3;


/*        return new Media(3, "tv", new ArrayList<>(List.of(new User[]{
                new User(1, "Matias", "matias@mail.com", "123456Aa", new ArrayList<>()),
                new User(2, "Jazmin", "jazmin@mail.com", "123456Aa", new ArrayList<>()),
                new User(3, "Victoria", "victoria@mail.com", "123456Aa", new ArrayList<>()),
        })));*/


    }

}
