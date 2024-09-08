package com.fernando.connected_minds_api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {
    @Mock
    private JWTService jwtService;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> userIDArgumentCaptor;

    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldGenerateJWTTokenAndReturnToken() throws Exception {
        String email = "fernandotest@test.com";
        String instant = generateInstant(2024, 9, 5, 19, 0);
        String jwtJson = """
            {
               \"sub\": \"%s\",
               \"iss\": \"%s\",
               \"iat\": \"%s\",
               \"exp\": \"%s\"
            }
        """.formatted(email, "Unit test", instant, instant);
        

        when(jwtService.generateJWT(stringArgumentCaptor.capture())).thenReturn(jwtJson);

        String jwtJSON = jwtService.generateJWT(email);

        Map<String, String> jwtMap = convertJSONToMap(jwtJSON);
        
        assertNotNull(jwtMap);
        assertNotNull(stringArgumentCaptor.getValue());
        assertEquals(email, jwtMap.get("sub"));
        assertEquals("Unit test", jwtMap.get("iss"));
        assertEquals(instant, jwtMap.get("iat"));
        assertEquals(instant, jwtMap.get("exp"));
    }

    @Test
    public void shouldGenerateRefreshTokenAndReturnToken() {
        UUID userID = UUID.randomUUID();
        String instant = generateInstant(2024, 9, 5, 19, 0);
        String jwtJson = """
            {
               \"sub\": \"%s\",
               \"iss\": \"%s\",
               \"iat\": \"%s\",
               \"exp\": \"%s\"
            }
        """.formatted(userID.toString(), "Unit test", instant, instant);
        

        when(jwtService.generateRefreshToken(userIDArgumentCaptor.capture())).thenReturn(jwtJson);

        String jwtJSON = jwtService.generateRefreshToken(userID);

        Map<String, String> jwtMap = convertJSONToMap(jwtJSON);
        
        assertNotNull(jwtMap);
        assertNotNull(userIDArgumentCaptor.getValue());
        assertEquals(userID.toString(), jwtMap.get("sub"));
        assertEquals("Unit test", jwtMap.get("iss"));
        assertEquals(instant, jwtMap.get("iat"));
        assertEquals(instant, jwtMap.get("exp"));
    }

    @Test
    public void shouldReturnOptionalWithSubjectWhenCallGetSubjectMethod() {
        String email = "fernandotest@test.com";

        when(jwtService.getSubject(stringArgumentCaptor.capture())).thenReturn(Optional.of(email));

        Optional<String> subject = jwtService.getSubject("jwt-token");

        assertNotNull(stringArgumentCaptor.getValue());
        assertEquals(Optional.of(email), subject);
        assertTrue(subject.isPresent());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenCallGetSubjectMethod() {
        when(jwtService.getSubject("jwt-token")).thenReturn(Optional.empty());

        Optional<String> emptySubject = jwtService.getSubject("jwt-token");

        assertTrue(emptySubject.isEmpty());
    }

    @Test
    public void shouldReturnOptionalWithJWTExpiresAtWhenCallGetSubjectMethod() {
        String instant = generateInstant(2024, 9, 5, 19, 0);

        when(jwtService.getExpiresAt(stringArgumentCaptor.capture())).thenReturn(Optional.of(instant));

        Optional<String> expiresAtOptional = jwtService.getExpiresAt("jwt-token");

        assertNotNull(stringArgumentCaptor.getValue());
        assertEquals(Optional.of(instant), expiresAtOptional);
        assertTrue(expiresAtOptional.isPresent());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenCallGetExpiresAtMethod() {
        when(jwtService.getExpiresAt("jwt-token")).thenReturn(Optional.empty());

        Optional<String> emptySubject = jwtService.getExpiresAt("jwt-token");

        assertTrue(emptySubject.isEmpty());
    }

    @Test
    public void shouldValidateTokenWithSuccess() {
        when(jwtService.isValidToken(stringArgumentCaptor.capture())).thenReturn(true);

        boolean isValidToken = jwtService.isValidToken("jwt-token");

        
        assertNotNull(stringArgumentCaptor.getValue());
        assertTrue(isValidToken);
        
    }

    @Test
    public void shouldNotValidateToken() {
        when(jwtService.isValidToken("jwt-token")).thenReturn(false);

        boolean isValidToken = jwtService.isValidToken("jwt-token");

        assertFalse(isValidToken);
        
    }



    private String generateInstant(int year, int month, int date, int hour, int minute) {
        return LocalDateTime.of(
            year, month, date, hour, minute)
        .toInstant(ZoneOffset.of("-03:00")).toString();
    }

    private Map<String, String> convertJSONToMap(String jwtJSON) {
            TypeReference<Map<String, String>> jwtMaTypeReference 
            = new TypeReference<Map<String, String>>() {};
        try {
            return objectMapper.readerFor(jwtMaTypeReference).readValue(jwtJSON);
        }
        catch (Exception e) {
            return null;
        }

    }
}