package com.fernando.connected_minds_api.services;

import java.net.URL;
import java.util.Random;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.connected_minds_api.models.Avatar;
import com.fernando.connected_minds_api.models.Avatars;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvatarsService {
    private final ObjectMapper mapper;
    private static final Random RANDOM = new Random();

    public Avatars getAvatars() {
        URL avatarsURL = getClass().getResource("/avatars.json");
        
        try {
            return mapper.readValue(avatarsURL, Avatars.class);
        
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Avatar getRandomMaleAvatar() {
        Avatars avatars = getAvatars();
        Integer randomIndex = RANDOM.nextInt(avatars.getMales().size());
        return avatars.getMales().get(randomIndex);
    }

    public Avatar getRandomFemaleAvatar() {
        Avatars avatars = getAvatars();
        Integer randomIndex = RANDOM.nextInt(avatars.getFemales().size());
        return avatars.getFemales().get(randomIndex);
    }
}