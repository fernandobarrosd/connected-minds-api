package com.fernando.connected_minds_api.managers;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CacheManagerTest {
    @Mock
    private CacheManager cacheManager;

    private String cacheKey;

    @BeforeEach
    public void setup() {
        this.cacheKey = "CACHE_KEY";
    }

    @Test
    public void shouldReturnCacheValueWhenCallGetCacheValueMethod() {
        List<Integer> expectedNumbers = List.of(1, 2, 3);

        when(cacheManager.getCacheValue(cacheKey)).thenReturn(expectedNumbers);

        List<Integer> numbers = cacheManager.getCacheValue(cacheKey);

        assertNotNull(numbers);

        assertFalse(numbers.isEmpty());
        assertEquals(3, numbers.size());

        assertEquals(1, numbers.get(0));
        assertEquals(2, numbers.get(1));
        assertEquals(3, numbers.get(2));
        
    }

    @Test
    public void shouldAddCacheMethodWhenCallAddCacheValue() {
        List<Integer> expectedNumbers = List.of(1, 2, 3);

        when(cacheManager.addCacheValue(cacheKey, expectedNumbers))
        .thenReturn(expectedNumbers);

        List<Integer> numbers = cacheManager.addCacheValue(cacheKey, expectedNumbers);

        assertNotNull(numbers);

        assertFalse(numbers.isEmpty());
        assertEquals(3, numbers.size());

        assertEquals(1, numbers.get(0));
        assertEquals(2, numbers.get(1));
        assertEquals(3, numbers.get(2));
    }


    @Test
    public void shouldReturnTrueWhenCallHasCacheKeyMethod() {
        when(cacheManager.hasCacheKey(cacheKey)).thenReturn(true);

        boolean hasCacheKey = cacheManager.hasCacheKey(cacheKey);

        assertTrue(hasCacheKey);
    }


    @Test
    public void removeCacheValueContains() {
        String expectedCharacters = "CACHE";

        doNothing()
        .when(cacheManager)
        .removeCacheValueContains(expectedCharacters);


        cacheManager.removeCacheValueContains(expectedCharacters);


        verify(cacheManager, times(1))
        .removeCacheValueContains(expectedCharacters);
    }
}