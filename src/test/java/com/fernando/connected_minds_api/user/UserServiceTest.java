package com.fernando.connected_minds_api.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fernando.connected_minds_api.singleton.FakerSingleton;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.github.javafaker.Faker;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private Faker faker;

    @BeforeEach
    public void setup() {
        faker = FakerSingleton.getInstance();
    }


    @Test
    public void shouldReturnUserWhenCallFindUserByUsername() {
        User user = UserFactory.createUser(faker);
        String input = user.getUsername();

        when(userRepository.findByUsername(input)).thenReturn(Optional.of(user));

        UserResponse response = userService.findUserByUsername(input);
        
        assertEquals(user.getId(), response.userId());
        assertEquals(user.getUsername(), response.username());
        assertEquals(user.getPhotoURL(), response.photoURL());
        assertEquals(user.getBannerURL(), response.bannerURL());
        assertEquals(user.getBio(), response.bio());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenUserIsNotExists() {
        String input = "fernandobarros";

        when(userRepository.findByUsername(input)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> userService.findUserByUsername(input)
        );

        assertEquals("User is not found", exception.getMessage());
    }
}