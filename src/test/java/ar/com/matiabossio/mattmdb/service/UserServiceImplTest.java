package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.business.domain.User;
import ar.com.matiabossio.mattmdb.business.dto.mapper.IUserMapper;
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
    }

    @Test
    void getUserByEmailService() {
    }

    @Test
    void userExists() {
    }

    @Test
    void updateUserService() {
    }

    @Test
    void deleteUserService() {
    }

    @Test
    void loginUserService() {
    }

    @Test
    void addTofavorites() {
    }

    @Test
    void countFavorites() {
    }

    @Test
    void removeFromFavorites() {
    }
}