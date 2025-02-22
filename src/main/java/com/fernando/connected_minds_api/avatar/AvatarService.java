package com.fernando.connected_minds_api.avatar;

import com.fernando.connected_minds_api.exceptions.FileNotExistsException;
import com.fernando.connected_minds_api.user.UserGenre;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class AvatarService {
    public String generateAvatarFromUserGenre(UserGenre userGenre) {
        if (userGenre == null) return null;

        Random random = new Random();
        String folder = userGenre == UserGenre.MALE ?
                "avatars/male" : "avatars/female";

        ClassLoader classLoader = getClass().getClassLoader();
        URL avatarsURL = classLoader.getResource(folder);

        if (avatarsURL == null) return null;

        try {
            File avatarsPathFile = new File(avatarsURL.toURI());

            List<File> avatarsFiles = Arrays.stream(Objects.requireNonNull(avatarsPathFile.listFiles()))
                    .toList();

            if (avatarsFiles.isEmpty()) return null;

            int randomIndex = random.nextInt(avatarsFiles.toArray().length);
            File randomAvatarFile = avatarsFiles.get(randomIndex);

            return randomAvatarFile.getName();
        }
        catch (Exception e) {
            return null;
        }
    }

    public byte[] findAvatarFileByName(String avatarFileName) {
        String avatarFilePath = avatarFileName.contains("male") ?
                "avatars/male/%s" : "avatars/female/%s";
        ClassLoader classLoader = getClass().getClassLoader();

        String avatarFilePath2 = avatarFilePath.formatted(avatarFileName);


        try(InputStream avatarFileInputStream = classLoader.getResourceAsStream(avatarFilePath2)) {
            if (avatarFileInputStream == null) return null;

            return avatarFileInputStream.readAllBytes();
        } catch (Exception e) {
            throw new FileNotExistsException("Avatar file name is not found");
        }
    }
}
