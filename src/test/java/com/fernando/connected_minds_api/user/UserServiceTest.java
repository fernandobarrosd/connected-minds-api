package com.fernando.connected_minds_api.user;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.singleton.FakerSingleton;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final Faker FAKER = FakerSingleton.getInstance();

    @Nested
    public class SaveUserTests {
        @Test
        public void shouldSaveUserAndReturnUserSaved() {
            User user = UserFactory.createUserWithoutID(FAKER);

            when(userRepository.save(user)).thenReturn(user);

            User userSaved = userRepository.save(user);

            assertEquals(user.getId(), userSaved.getId());
            assertEquals(user.getUsername(), userSaved.getUsername());
            assertEquals(user.getEmail(), userSaved.getEmail());
            assertEquals(user.getPassword(), userSaved.getPassword());
            assertEquals(user.getPhotoURL(), userSaved.getPhotoURL());
            assertEquals(user.getBannerURL(), userSaved.getBannerURL());
            assertEquals(user.getBio(), userSaved.getBio());
            assertEquals(user.getBirthDate(), userSaved.getBirthDate());
        }
    }

    @Nested
    public class FindUserByIDTests {
        @Test
        public void shouldFindUserByIDAndReturnUser() {
            User user = UserFactory.createUserWithoutID(FAKER);

            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.of(user));

            Optional<User> userOptional = userRepository.findById(user.getId());

            assertTrue(userOptional.isPresent());

            User userFind = userOptional.get();

            assertEquals(user.getId(), userFind.getId());
            assertEquals(user.getUsername(), userFind.getUsername());
            assertEquals(user.getEmail(), userFind.getEmail());
            assertEquals(user.getPassword(), userFind.getPassword());
            assertEquals(user.getPhotoURL(), userFind.getPhotoURL());
            assertEquals(user.getBannerURL(), userFind.getBannerURL());
            assertEquals(user.getBio(), userFind.getBio());
            assertEquals(user.getBirthDate(), userFind.getBirthDate());
        }

        @Test
        public void shouldThrowsEntityNotFoundExceptionIfUserIdWasNotFound() {
            UUID userID = UUID.randomUUID();

            when(userRepository.findById(userID)).thenReturn(Optional.empty());

            var entityNotFoundException = assertThrows(
                    EntityNotFoundException.class,
                    () -> userService.findUserByID(userID)
            );

            assertEquals("User not found", entityNotFoundException.getMessage());
        }
    }

    @Nested
    public class FindUserByUsernameTests {
        @Test
        public void shouldFindUserByUsernameAndReturnUser() {
            User user = UserFactory.createUserWithoutID(FAKER);

            when(userRepository.findByUsername(user.getUsername()))
                    .thenReturn(Optional.of(user));

            UserResponse userResponse = userService.findUserByUsername(user.getUsername());


            assertEquals(user.getId(), userResponse.userID());
            assertEquals(user.getUsername(), userResponse.username());
            assertEquals(user.getPhotoURL(), userResponse.photoURL());
            assertEquals(user.getBannerURL(), userResponse.bannerURL());
            assertEquals(user.getBio(), userResponse.bio());
            assertEquals(user.getBirthDate().toString(), userResponse.birthDate());
        }

        @Test
        public void shouldThrowsEntityNotFoundExceptionIfUserUsernameWasNotFound() {
            User user = UserFactory.createUserWithoutID(FAKER);

            when(userRepository.findByUsername(user.getUsername()))
                    .thenReturn(Optional.empty());

            var entityNotFoundException = assertThrows(
                    EntityNotFoundException.class,
                    () -> userService.findUserByUsername(user.getUsername())
            );

            assertEquals("User not found", entityNotFoundException.getMessage());
        }
    }

    @Nested
    public class ExistsUserByEmailTests {
        @Test
        public void shouldReturnTrueIfUserEmailExists() {
            User user = UserFactory.createUserWithoutID(FAKER);

            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));

            boolean userIsExists = userService.existsUserByEmail(user.getEmail());

            assertTrue(userIsExists);

        }

        @Test
        public void shouldReturnFalseIfUserEmailNotExists() {
            User user = UserFactory.createUserWithoutID(FAKER);

            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.empty());

            boolean userIsExists = userService.existsUserByEmail(user.getEmail());

            assertFalse(userIsExists);

        }
    }

    @Nested
    public class ExistsUserByUsernameTests {
        @Test
        public void shouldReturnTrueIfUserUsernameExists() {
            User user = UserFactory.createUserWithoutID(FAKER);

            when(userRepository.findByUsername(user.getUsername()))
                    .thenReturn(Optional.of(user));

            boolean userIsExists = userService.existsUserByUsername(user.getUsername());

            assertTrue(userIsExists);
        }

        @Test
        public void shouldReturnFalseIfUserUsernameNotExists() {
            User user = UserFactory.createUserWithoutID(FAKER);

            when(userRepository.findByUsername(user.getUsername()))
                    .thenReturn(Optional.empty());

            boolean userIsExists = userService.existsUserByUsername(user.getUsername());

            assertFalse(userIsExists);
        }
    }
}
