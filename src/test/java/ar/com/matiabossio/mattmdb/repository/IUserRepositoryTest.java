package ar.com.matiabossio.mattmdb.repository;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.data.DummyMedia;
import ar.com.matiabossio.mattmdb.data.DummyUsers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("TESTS: User Repository")
class IUserRepositoryTest {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IMediaRepository mediaRepository;

    @BeforeEach
    void setUp() {

        mediaRepository.save(DummyMedia.getMedia1());
        mediaRepository.save(DummyMedia.getMedia2());
        mediaRepository.save(DummyMedia.getMedia3());

        Optional<Media> oMedia1 = mediaRepository.findById(1);
        Media media1 = oMedia1.get();

        Optional<Media> oMedia2 = mediaRepository.findById(2);
        Media media2 = oMedia2.get();

        Optional<Media> oMedia3 = mediaRepository.findById(3);
        Media media3 = oMedia3.get();

        // Jazmin doesn't have media1:
        User jazmin = DummyUsers.getJazmin();
        jazmin.setFavorites(List.of(media2, media3));

        userRepository.save(DummyUsers.getMatias());
        userRepository.save(DummyUsers.getMariangeles());
        //userRepository.save(DummyUsers.getJazmin());
        userRepository.save(jazmin);

    }

    @AfterEach
    void tearDown() {
            userRepository.deleteAll();
            mediaRepository.deleteAll();
    }

    @Test
    @DisplayName("Find User By Email" )
    // @Disabled    // disables the test
    void findByEmail() {

        // GIVEN: Give a context to the test
        String emailTest = "jazmin@mail.com";


        // WHEN: When I want to run the test
        Optional<User> oUserByEmail = userRepository.findByEmail(emailTest);

        // THEN: Validate that what is happening returns the expected result
        assertThat(oUserByEmail.isPresent()).isTrue();
        assertThat(oUserByEmail.get().getEmail()).isEqualTo(emailTest);


    }

    @Test
    @DisplayName("Find User By Email and Password" )
    void findUserByEmailAndPassword() {
        // GIVEN: Give a context to the test
        String emailTest = "mariangeles@mail.com";
        String passwordTest = "123456Aa";


        // WHEN: When I want to run the test
        Optional<User> oUserByEmailAndPassword = userRepository.findUserByEmailAndPassword(emailTest, passwordTest);

        // THEN: Validate that what is happening returns the expected result
        assertThat(oUserByEmailAndPassword.isPresent()).isTrue();
        assertThat(oUserByEmailAndPassword).isEqualTo(userRepository.findByEmail(emailTest));

    }

    @Test
    @DisplayName("Is Favorite?" )
    void isFavorite() {
        // GIVEN: Give a context to the test
        Integer userIdJazmin = 3;
        Integer favoriteIdJazmin = 3;
        Integer notFavoriteIdJazmin = 1;


        // WHEN: When I want to run the test
        boolean isFavoriteJazmin = userRepository.isFavorite(userIdJazmin, favoriteIdJazmin);
        boolean isFavoriteJazmin2 = userRepository.isFavorite(userIdJazmin, notFavoriteIdJazmin);
        boolean isFavoriteMatias = userRepository.isFavorite(userIdJazmin, notFavoriteIdJazmin);

        // THEN: Validate that what is happening returns the expected result
        assertThat(isFavoriteJazmin).isTrue();  // it works if tested individualy but not with others
        assertThat(isFavoriteJazmin2).isFalse();
        assertThat(isFavoriteMatias).isFalse();

    }
}