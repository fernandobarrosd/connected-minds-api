package com.fernando.connected_minds_api.user;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.singleton.FakerSingleton;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private final Faker FAKER = FakerSingleton.getInstance();

    @Nested
    @DisplayName("Save user in database tests")
    class SaveUserInDatabaseTests {
        @Test
        @DisplayName("Should save user in database with success")
        public void shouldSaveUserInDatabaseWithSuccess() {
            User user = UserFactory.createUserWithoutID(FAKER);
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
    @DisplayName("Find user by id from database tests")
    public class FindUserByIdFromDatabaseTests {
        @Test
        @DisplayName("Should find user by id from database and return user")
        public void shouldFindUserByIdFromDatabaseAndReturnUser() {
            User userSaved = userRepository.save(UserFactory.createUserWithoutID(FAKER));

            Optional<User> userOptional = userRepository.findById(userSaved.getId());

            assertTrue(userOptional.isPresent());

            User userFind = userOptional.get();

            assertEquals(userSaved.getId(), userFind.getId());
            assertEquals(userSaved.getUsername(), userFind.getUsername());
            assertEquals(userSaved.getEmail(), userFind.getEmail());
            assertEquals(userSaved.getPassword(), userFind.getPassword());
            assertEquals(userSaved.getPhotoURL(), userFind.getPhotoURL());
            assertEquals(userSaved.getBannerURL(), userFind.getBannerURL());
            assertEquals(userSaved.getBio(), userFind.getBio());
            assertEquals(userSaved.getBirthDate(), userFind.getBirthDate());
        }

        @Test
        @DisplayName("Should throws EntityNotFoundException if user id was not found")
        public void shouldThrowsEntityNotFoundExceptionIfUserIdWasNotFound() {
            UUID userID = UUID.randomUUID();

            var entityNotFoundException = assertThrows(
                    EntityNotFoundException.class,
                    () -> userRepository.findById(userID)
                            .orElseThrow(() -> new EntityNotFoundException("User not found"))
            );

            assertEquals("User not found", entityNotFoundException.getMessage());
        }
    }

    @Nested
    @DisplayName("Find user by username from database tests")
    public class FindUserByUsernameFromDatabaseTests {
        @Test
        @DisplayName("Should find user by username from database and return user")
        public void shouldFindUserByUsernameFromDatabaseAndReturnUser() {
            User userSaved = userRepository.save(UserFactory.createUserWithoutID(FAKER));

            Optional<User> userOptional = userRepository.findByUsername(userSaved.getUsername());

            assertTrue(userOptional.isPresent());

            User userFind = userOptional.get();

            assertEquals(userSaved.getId(), userFind.getId());
            assertEquals(userSaved.getUsername(), userFind.getUsername());
            assertEquals(userSaved.getEmail(), userFind.getEmail());
            assertEquals(userSaved.getPassword(), userFind.getPassword());
            assertEquals(userSaved.getPhotoURL(), userFind.getPhotoURL());
            assertEquals(userSaved.getBannerURL(), userFind.getBannerURL());
            assertEquals(userSaved.getBio(), userFind.getBio());
            assertEquals(userSaved.getBirthDate(), userFind.getBirthDate());

        }

        @Test
        @DisplayName("Should throws EntityNotFoundException if user username was not found")
        public void shouldThrowsEntityNotFoundExceptionIfUserIdWasNotFound() {
            String username = FAKER.name().username().replace(".", "_");

            var entityNotFoundException = assertThrows(
                    EntityNotFoundException.class,
                    () -> userRepository.findByUsername(username)
                            .orElseThrow(() -> new EntityNotFoundException("User not found"))
            );

            assertEquals("User not found", entityNotFoundException.getMessage());
        }

    }

    @Nested
    @DisplayName("Find user by email from database tests")
    public class FindUserByEmailFromDatabaseTests {
        @Test
        @DisplayName("Should find user by email from database and return user")
        public void shouldFindUserByEmailFromDatabaseAndReturnUser() {
            User userSaved = userRepository.save(UserFactory.createUserWithoutID(FAKER));

            Optional<User> userOptional = userRepository.findByEmail(userSaved.getEmail());

            assertTrue(userOptional.isPresent());

            User userFind = userOptional.get();

            assertEquals(userSaved.getId(), userFind.getId());
            assertEquals(userSaved.getUsername(), userFind.getUsername());
            assertEquals(userSaved.getEmail(), userFind.getEmail());
            assertEquals(userSaved.getPassword(), userFind.getPassword());
            assertEquals(userSaved.getPhotoURL(), userFind.getPhotoURL());
            assertEquals(userSaved.getBannerURL(), userFind.getBannerURL());
            assertEquals(userSaved.getBio(), userFind.getBio());
            assertEquals(userSaved.getBirthDate(), userFind.getBirthDate());
        }

        @Test
        @DisplayName("Should throws EntityNotFoundException if user email was not found")
        public void shouldThrowsEntityNotFoundExceptionIfUserEmailWasNotFound() {
            String email = FAKER.name().username()
                    .replace(".", "_")
                    .concat("@test.com");

            var entityNotFoundException = assertThrows(
                    EntityNotFoundException.class,
                    () -> userRepository.findByUsername(email)
                            .orElseThrow(() -> new EntityNotFoundException("User not found"))
            );

            assertEquals("User not found", entityNotFoundException.getMessage());
        }
    }
}