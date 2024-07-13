package com.fernando.connected_minds_api.enums.converters;

import com.fernando.connected_minds_api.enums.UserGenre;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter(autoApply = true)
public class UserGenreConverter implements AttributeConverter<UserGenre, String> {

    @Override
    public String convertToDatabaseColumn(UserGenre genre) {
        return genre.getValue();
    }

    @Override
    public UserGenre convertToEntityAttribute(String genreValue) {
        List<UserGenre> userGenre = Arrays.stream(UserGenre.values())
                .filter(genre -> genre.getValue().equals(genreValue))
                .toList();

        if (userGenre.isEmpty()) {
            throw new RuntimeException("Enum conversion error");
        }
        return userGenre.getFirst();

    }
}
