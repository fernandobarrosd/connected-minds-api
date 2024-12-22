package com.fernando.connected_minds_api.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fernando.connected_minds_api.enums.UserGenre;
import com.fernando.connected_minds_api.managers.CacheManager;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.UserRepository;
import com.fernando.connected_minds_api.responses.UserResponse;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CacheManager cacheManager;



    @Test
    public void shouldReturnUserResponseWhenCallFindByUsernameMethod() {
        String username = "username";
        String userCacheKey = "USER_username";

        UUID userID = UUID.fromString("d76ea812-8dd5-4d7e-848a-8d3f4c103742");
        LocalDate birthDate = LocalDate.of(2004, 12, 20);

        ArgumentCaptor<String> userCacheKeyCaptor = ArgumentCaptor.captor();
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.captor();

        User user = new User(
            userID,
            username,
            "email@email.com",
            "password123",
            birthDate,
            null,
            null,
            null,
            UserGenre.MALE
        );

        when(cacheManager.getCacheValue(userCacheKeyCaptor.capture()))
        .thenReturn(null);


        when(userRepository.findByUsername(usernameCaptor.capture()))
        .thenReturn(Optional.of(user));

        UserResponse userResponse = userService.findUserByUsername(username);

        assertEquals(userCacheKey, userCacheKeyCaptor.getValue());
        assertEquals(username, usernameCaptor.getValue());

        assertNotNull(userResponse);
        
        assertEquals(userID, userResponse.id());
        assertEquals(username, userResponse.username());
        assertEquals(user.getGenre().getValue(), userResponse.genre());
        
        assertNull(userResponse.photoURL());
        assertNull(userResponse.bannerURL());
        assertNull(userResponse.bio());

    }
}