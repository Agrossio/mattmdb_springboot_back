package ar.com.matiabossio.mattmdb.repository;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.data.DummyData;
import ar.com.matiabossio.mattmdb.data.DummyUsers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class IMediaRepositoryTest {
    @Autowired
    private IMediaRepository mediaRepository;
    @Autowired
    private IUserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findMediaByFansContains() {

        User user1 = DummyUsers.getMatias();
        user1.setFavorites(List.of(new Media[]{ DummyData.getMedia1(), DummyData.getMedia2(), DummyData.getMedia3() }));

        User user2 = DummyUsers.getMariangeles();
        user2.setFavorites(List.of(new Media[]{ DummyData.getMedia2(), DummyData.getMedia3() }));

        User user3 = DummyUsers.getJazmin();
        user3.setFavorites(List.of(new Media[]{ DummyData.getMedia3() }));

        /*// user1.setUserId(1);
        user1.setUsername("Matias");
        user1.setEmail("matias@mail.com");
        user1.setPassword("123456Aa");

        // user1.setUserId(2);
        user2.setUsername("Jazmin");
        user2.setEmail("jazmin@mail.com");
        user2.setPassword("123456Aa");

        // user1.setUserId(3);
        user3.setUsername("Victoria");
        user3.setEmail("victoria@mail.com");
        user3.setPassword("123456Aa");*/

        // 1, "Matias", "matias@mail.com", "123456Aa", new ArrayList<>()

/*        Media media1 = new Media(1, "movie", new ArrayList<>());
        Media media2 = new Media(2, "movie", new ArrayList<>());
        Media media3 = new Media(3, "tv", new ArrayList<>());*/

        Media media1 = DummyData.getMedia1();
        Media media2 = DummyData.getMedia2();
        Media media3 = DummyData.getMedia3();

        List<Media> favorites1 = List.of(new Media[]{media1, media2, media3});

        // GIVEN: Give a context to the test
        this.userRepository.save(user1);
        this.userRepository.save(user2);
        this.userRepository.save(user3);
        this.mediaRepository.save(DummyData.getMedia1());
        this.mediaRepository.save(DummyData.getMedia2());
        this.mediaRepository.save(DummyData.getMedia3());

        // WHEN: When I want to run the test
        List<Media> user1FavoriteList = this.mediaRepository.findMediaByFansContains(user1);

        System.out.println("FAVORITES LIST --------" + user1FavoriteList);

        // THEN: Validate that what is happening returns the expected result
        assertThat(user1FavoriteList).isEqualTo(favorites1);


        // user1FavoriteList.equals(new ArrayList<Media>(List.of(new Media[]{movie1, movie2, tv3})));



    }
}