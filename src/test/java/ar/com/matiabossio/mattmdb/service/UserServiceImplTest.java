package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.PasswordFromRequestDTO;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IUserMapper;
import ar.com.matiabossio.mattmdb.data.DummyMedia;
import ar.com.matiabossio.mattmdb.data.DummyUsers;
import ar.com.matiabossio.mattmdb.repository.IMediaRepository;
import ar.com.matiabossio.mattmdb.repository.IUserRepository;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("TESTS: User Service")
class UserServiceImplTest {
    //@Mock     // Without spring
    @MockBean
    private IUserRepository userRepository;
    @MockBean
    private IMediaRepository mediaRepository;
    private IUserMapper userMapper;
    @Autowired
    private IUserService userService;

    @BeforeEach
    void setUp() {


/*    Without spring:

        // imported mock and all Mockito methods as static
        userRepository = mock(IUserRepository.class);
        userMapper = mock(IUserMapper.class);
        mediaRepository = mock(IMediaRepository.class);

        // I will need userRepository, userMapper and mediaRepository:
        userService = new UserServiceImpl(userRepository, userMapper, mediaRepository);
*/



    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getUsersService() {
        // GIVEN: Give a context to the test
        when(userRepository.findAll()).thenReturn(Arrays.asList(DummyUsers.getMatias(), DummyUsers.getMariangeles(), DummyUsers.getJazmin()));


        // WHEN: When I want to run the test
        List<User> usersList = userService.getUsersService();

        // THEN: Validate that what is happening returns the expected result
        assertThat(usersList.size()).isEqualTo(3);
        assertThat(usersList.isEmpty()).isFalse();


        // Verify that userService is calling one time (optional) the method findAll of userRepository:
        verify(userRepository, times(1)).findAll();

    }

    @Test
    void createUserService() {
        // GIVEN: Give a context to the test
        User user = DummyUsers.getMatias();


        // WHEN: When I want to run the test
        userService.createUserService(user);


        // THEN: Validate that what is happening returns the expected result
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // Verify that userService is calling the method save of userRepository:
        verify(userRepository).save(userArgumentCaptor.capture());  //using argument capture I'm capturing the argument that I pass to save

        // get the value of the argument captor:
        User userCaptor = userArgumentCaptor.getValue();

        // Check that the user received in the save method of the repository is the same that it is passed in userService.createUserService():
        assertThat(userCaptor).isEqualTo(user);

        verify(userRepository).findByEmail(anyString());

    }

    @Test
    void saveWithException() {

        // GIVEN: Give a context to the test
        User user = DummyUsers.getMariangeles();


        // when the repository calls findByEmail with Mariangeles' email (an existing email) it has to return an optional of Mariangeles with a User inside.
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        // WHEN & THEN:

        /* check:
            1) that it is thrown by userService.createUserService
            2) is an instance of a RuntimeException
            3) the message contains "already in use"
         */
        assertThatThrownBy(()->userService.createUserService(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already in use");

    }

    @Test
    void getUserByIdService() {

        // GIVEN: Give a context to the test
        User user = DummyUsers.getMariangeles();
        user.setUserId(2);                          // give the dummy user the id of 2

        // Mock findById:
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(DummyUsers.getMariangeles()));

        // WHEN: When I want to run the test
        // Find the user calling "userService.getUserByIdService":
        User foundUser = userService.getUserByIdService(user.getUserId());

        // THEN: Validate that what is happening returns the expected result
        assertThat(foundUser).isInstanceOf(User.class);
        assertThat(foundUser).isEqualTo(user);


        // Verify that userService is calling one time (optional) the method findById of userRepository:
        verify(userRepository, times(1)).findById(user.getUserId());
    }

    @Test
    void getUserByEmailService() {

        // GIVEN: Give a context to the test
        User user = DummyUsers.getMariangeles();

        // Mock findByEmail:
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(DummyUsers.getMariangeles()));

        // WHEN: When I want to run the test
        // Find the user calling "userService.getUserByEmailService":
        Optional<User> oFoundUser = userService.getUserByEmailService(user.getEmail());

        // THEN: Validate that what is happening returns the expected result
        assertThat(oFoundUser.isPresent()).isTrue();
        assertThat(oFoundUser.get()).isEqualTo(user);


        // Verify that userService is calling one time (optional) the method findById of userRepository:
        verify(userRepository, times(1)).findByEmail(user.getEmail());

    }

    @Test
    void userExists() {

        // GIVEN: Give a context to the test
        User user = DummyUsers.getMariangeles();
        User notInDB = DummyUsers.getNotDB();

        // Mock findByEmail:
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(notInDB.getEmail())).thenReturn(Optional.of(notInDB));

        // WHEN: When I want to run the test
        // Return true when calling "userService.userExists":
        boolean userExists = userService.userExists(user.getEmail());
        boolean userNotExists = userService.userExists(notInDB.getEmail());

        // THEN: Validate that what is happening returns the expected result
        assertThat(userExists).isTrue();
        //assertThat(userNotExists).isFalse();

        // Verify that userService is calling one time (optional) the method userExists of userRepository:
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        //verify(userRepository, times(1)).findByEmail(notInDB.getEmail());

    }

    @Test
    void updateUserService() {

        // GIVEN: Give a context to the test
        User user = DummyUsers.getMariangeles();
        user.setUserId(2);                          // give the dummy user the id of 2
        user.setUsername("Mariangeles Gonzalez");

        // WHEN: When I want to run the test

        // Mock findById:
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(DummyUsers.getMariangeles()));

        userService.updateUserService(user.getUserId(), user);

        // THEN: Validate that what is happening returns the expected result
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // Verify that userService is calling the method save of userRepository:
        verify(userRepository).save(userArgumentCaptor.capture());  //using argument capture I'm capturing the argument that I pass to save

        // get the value of the argument captor:
        User userCaptor = userArgumentCaptor.getValue();

        // Check that the user received in the save method of the repository is the same that it is passed in userService.createUserService():
        assertThat(userCaptor).isEqualTo(user);

        verify(userRepository).findById(2);

    }

    @Test
    void updateWithException() {

        // GIVEN: Give a context to the test
        User user = DummyUsers.getMariangeles();
        user.setUserId(2);                          // give the dummy user the id of 2

        // Mock findById:
        // when the repository calls findById with an incorrect id it has to return an empty optional.
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));

        // WHEN & THEN user not found:

        /* check:
            1) that it is thrown by userService.createUserService
            2) is an instance of a HttpClientErrorException
            3) the message contains "doesn't exist"
         */
        assertThatThrownBy(()->userService.updateUserService(10, user))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("doesn't exist");

        // WHEN & THEN user passwords don't match:

        // Mock findById:
        // when the repository calls findById with an incorrect id it has to return an empty optional.
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));

        /* check:
            1) that it is thrown by userService.createUserService
            2) is an instance of a HttpClientErrorException
            3) the message contains "check your password"
         */

        User userWrongPw = DummyUsers.getMariangeles();
        userWrongPw.setPassword("123");

        assertThatThrownBy(()->userService.updateUserService(2, userWrongPw))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("check your password");

    }

    @Test
    void deleteUserService() {

        // GIVEN: Give a context to the test
        User user = DummyUsers.getMariangeles();
        user.setUserId(2);                          // give the dummy user the id of 2

        PasswordFromRequestDTO password = new PasswordFromRequestDTO(user.getPassword());

        // WHEN: When I want to run the test

        // Mock findById:
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(DummyUsers.getMariangeles()));

        userService.deleteUserService(user.getUserId(), password);

        // THEN: Validate that what is happening returns the expected result
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // Verify that userService is calling the method delete of userRepository:
        verify(userRepository).delete(userArgumentCaptor.capture());  //using argument capture I'm capturing the argument that I pass to delete

        // get the value of the argument captor:
        User userCaptor = userArgumentCaptor.getValue();

        // Check that the user received in the delete method of the repository is the same that it is passed in userService.deleteUserService():
        assertThat(userCaptor).isEqualTo(user);

        verify(userRepository).findById(2);

    }


    @Test
    @Disabled
    void deleteWithException() {

        // GIVEN: Give a context to the test
        User user = DummyUsers.getMariangeles();
        user.setUserId(2);                          // give the dummy user the id of 2

        PasswordFromRequestDTO password = new PasswordFromRequestDTO(user.getPassword());

        // Mock findById:
        // when the repository calls findById with an incorrect id it has to return an empty optional.
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));

        // WHEN & THEN user not found:

        /* check:
            1) that it is thrown by userService.createUserService
            2) is an instance of a HttpClientErrorException
            3) the message contains "doesn't exist"
         */
        assertThatThrownBy(()->userService.deleteUserService(10, password))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("doesn't exist");

        // WHEN & THEN user passwords don't match:

        // Mock findById:
        // when the repository calls findById with an incorrect id it has to return an empty optional.
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));

        /* check:
            1) that it is thrown by userService.createUserService
            2) is an instance of a HttpClientErrorException
            3) the message contains "check your password"
         */

        User userWrongPw = DummyUsers.getMariangeles();
        userWrongPw.setPassword("123");

        assertThatThrownBy(()->userService.deleteUserService(2, password))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("check your password");

    }

    @Test
    void loginUserService() {

        // GIVEN: Give a context to the test
        User user = DummyUsers.getMariangeles();

        // WHEN: When I want to run the test

        // Mock findByEmail:
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(DummyUsers.getMariangeles()));

        userService.loginUserService(user);

        // THEN: Validate that what is happening returns the expected result
        ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);

        // Verify that userService is calling the method findByEmail of userRepository:
        verify(userRepository).findByEmail(emailArgumentCaptor.capture());  //using argument capture I'm capturing the argument that I pass to delete

        // get the value of the argument captor:
        String emailCaptor = emailArgumentCaptor.getValue();

        // Check that the user received in the findByEmail method of the repository is the same that it is passed in userService.loginUserService():
        assertThat(emailCaptor).isEqualTo(user.getEmail());

        verify(userRepository).findByEmail("mariangeles@mail.com");

    }

    @Test
    void loginWithException() {

        // GIVEN: Give a context to the test
        User user = DummyUsers.getMariangeles();
        //user.setUserId(2);                          // give the dummy user the id of 2

        // Mock findById:
        // when the repository calls findByEmail with an incorrect email it has to return an empty optional.
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        // WHEN & THEN user not found:

        User userWrongEmail = DummyUsers.getMariangeles();
        userWrongEmail.setEmail("mariangele@mail.com");

        /* check:
            1) that it is thrown by userService.loginUserService
            2) is an instance of a HttpClientErrorException
            3) the message contains "doesn't exist"
         */
        assertThatThrownBy(()->userService.loginUserService(userWrongEmail))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("Please check your credentials");

        // WHEN & THEN user passwords don't match:

        // Mock findById:
        // when the repository calls findById with an incorrect id it has to return an empty optional.
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        /* check:
            1) that it is thrown by userService.createUserService
            2) is an instance of a HttpClientErrorException
            3) the message contains "check your password"
         */

        User userWrongPw = DummyUsers.getMariangeles();
        userWrongPw.setPassword("123");

        assertThatThrownBy(()->userService.loginUserService(userWrongPw))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("Please check your credentials");

    }

    @Test
    void addTofavorites() {
    }

    @Test
    @Disabled
    void countFavorites() {

        // GIVEN: Give a context to the test
        User user = DummyUsers.getMariangeles();
        user.setUserId(2);                          // give the dummy user the id of 2

        // Mock findById:
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(DummyUsers.getMariangeles()));

        // WHEN: When I want to run the test
        // Find quantity of favorites user has calling "userService.countFavorites":
        int favoritesQty = userService.countFavorites(user.getUserId());

        // THEN: Validate that what is happening returns the expected result
        assertThat(favoritesQty).isGreaterThanOrEqualTo(0);
        assertThat(favoritesQty).isEqualTo(2);

        // Verify that userService is calling one time (optional) the method findById of userRepository:
        verify(userRepository, times(1)).findById(user.getUserId());

    }

    @Test
    void removeFromFavorites() {
    }
}