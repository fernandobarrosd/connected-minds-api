package com.fernando.connected_minds_api.avatar;

import com.fernando.connected_minds_api.user.UserGenre;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AvatarServiceTest {
    @Autowired
    private AvatarService avatarService;

    @Nested
    public class GenerateRandomAvatarFromUserGenreTests {
        @Test
        public void shouldGenerateRandomMaleAvatarAndReturnAvatar() {
            UserGenre userGenre = UserGenre.MALE;
            String avatar = avatarService.generateAvatarFromUserGenre(userGenre);

            assertNotNull(avatar);
            assertTrue(avatar.contains("male"));
        }


        @Test
        public void shouldGenerateRandomFemaleAndReturnAvatar() {
            UserGenre userGenre = UserGenre.FEMALE;
            String avatar = avatarService.generateAvatarFromUserGenre(userGenre);

            assertNotNull(avatar);
            assertTrue(avatar.contains("female"));
        }

        @Test
        public void shouldNotGenerateRandomAvatarIfUserGenreBeNullAndReturnNull() {
            String avatar = avatarService.generateAvatarFromUserGenre(null);

            assertNull(avatar);
        }
    }
}