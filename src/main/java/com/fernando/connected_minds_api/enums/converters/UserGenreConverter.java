package com.fernando.connected_minds_api.enums.converters;

import com.fernando.connected_minds_api.enums.UserGenre;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserGenreConverter implements AttributeConverter<UserGenre, String> {

    @Override
    public String convertToDatabaseColumn(UserGenre genre) {
        return genre.getValue();
    }

    @Override
    public UserGenre convertToEntityAttribute(String genreValue) {
        return UserGenre.valueOf(genreValue);
    }
}
