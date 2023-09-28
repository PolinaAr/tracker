package com.andersen.track;


import com.andersen.exception.DatabaseException;
import com.andersen.exception.EntityNotFoundException;
import com.andersen.track.dao.Track;
import com.andersen.track.dao.TrackDao;
import com.andersen.track.service.TrackCreateDto;
import com.andersen.track.service.TrackResponseDto;
import com.andersen.track.service.TrackServiceImpl;
import com.andersen.user.dao.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackServiceTest {

    @Mock
    private TrackMapper trackMapper;
    @Mock
    private TrackDao trackDao;
    @InjectMocks
    private TrackServiceImpl trackService;

    private Track mockTrack;
    private TrackCreateDto trackCreateDto;
    private TrackResponseDto expectedTrackResponseDto;
    private TrackResponseDto expectedTrackResponseDtoUpdated;
    private User user;
    private LocalDate date;
    private List<Track> mockTrackList;
    private List<TrackResponseDto> expectedTrackResponseDtoList;

    @BeforeEach
    public void initData() {
        long id = 1L;
        date = LocalDate.of(2023, 9, 27);
        double time = 3.5;
        double updatedTime = 5.5;
        String report = "My new report";
        String updatedReport = "My updated report";
        user = new User(id, "Anton", "Anton", "anton@mail.com", "12345");
        mockTrack = new Track(id, time, report, date, user.getId());
        trackCreateDto = new TrackCreateDto(time, report, date, user.getId());
        expectedTrackResponseDto = new TrackResponseDto(id, time, report, date, user.getId());
        expectedTrackResponseDtoUpdated = new TrackResponseDto(id, updatedTime, updatedReport, date, user.getId());
        mockTrackList = List.of(mockTrack);
        expectedTrackResponseDtoList = List.of(expectedTrackResponseDto);
    }

    @Test
    void testGetByData() {
        when(trackDao.getByDate(date)).thenReturn(mockTrackList);
        when(trackMapper.mapToResponseDto(mockTrackList.get(0))).thenReturn(expectedTrackResponseDtoList.get(0));

        List<TrackResponseDto> actualTrackResponseDtoList = trackService.getByData(date);

        assertEquals(1, actualTrackResponseDtoList.size());
        assertEquals(expectedTrackResponseDtoList, actualTrackResponseDtoList);
        verify(trackDao).getByDate(date);
        verify(trackMapper).mapToResponseDto(mockTrack);
    }

    @Test
    void testGetByDataWithNoData() {
        when(trackDao.getByDate(date)).thenReturn(Collections.emptyList());

        List<TrackResponseDto> actualTrackResponseDtoList = trackService.getByData(date);

        assertEquals(0, actualTrackResponseDtoList.size());
        verify(trackDao).getByDate(date);
    }

    @Test
    void testGetById() {
        Long trackId = anyLong();
        when(trackDao.getById(trackId)).thenReturn(mockTrack);
        when(trackMapper.mapToResponseDto(mockTrack)).thenReturn(expectedTrackResponseDto);

        TrackResponseDto actualTrackResponseDto = trackService.getById(trackId);

        assertEquals(expectedTrackResponseDto, actualTrackResponseDto);
        verify(trackDao).getById(trackId);
        verify(trackMapper).mapToResponseDto(mockTrack);
    }

    @Test
    void testGetByIdWithIncorrectId() {
        Long trackWithIncorrectId = anyLong();

        when(trackDao.getById(trackWithIncorrectId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> trackService.getById(trackWithIncorrectId));
        verify(trackDao).getById(trackWithIncorrectId);
        verifyNoInteractions(trackMapper);
    }

    @Test
    void testGetByIdReturnNull() {
        Long trackWithIncorrectId = anyLong();
        when(trackDao.getById(trackWithIncorrectId)).thenReturn(null);

        TrackResponseDto actualTrackResponseDto = trackService.getById(trackWithIncorrectId);

        assertNull(actualTrackResponseDto);
        verify(trackDao).getById(trackWithIncorrectId);
        verify(trackMapper).mapToResponseDto(isNull());
    }

    @Test
    void testGetByUserId() {
        Long userId = user.getId();
        when(trackDao.getByUserId(userId)).thenReturn(List.of(mockTrack));
        when(trackMapper.mapToResponseDto(mockTrack)).thenReturn(expectedTrackResponseDto);

        List<TrackResponseDto> actualList = trackService.getByUserId(userId);
        TrackResponseDto actualTrackResponseDto = actualList.get(0);

        assertEquals(1, actualList.size());
        assertEquals(expectedTrackResponseDto, actualTrackResponseDto);
        verify(trackDao).getByUserId(userId);
        verify(trackMapper).mapToResponseDto(mockTrack);
    }

    @Test
    void testGetByUserIdWithNoData() {
        Long userId = anyLong();

        when(trackDao.getByUserId(userId)).thenReturn(Collections.emptyList());

        List<TrackResponseDto> actualTrackResponseDtoList = trackService.getByUserId(userId);

        assertEquals(0, actualTrackResponseDtoList.size());
        verify(trackDao).getByUserId(userId);
    }

    @Test
    void testCreate() {
        when(trackDao.create(mockTrack)).thenReturn(mockTrack);
        when(trackMapper.mapToEntity(trackCreateDto)).thenReturn(mockTrack);
        when(trackMapper.mapToResponseDto(mockTrack)).thenReturn(expectedTrackResponseDto);

        TrackResponseDto actualTrackResponseDto = trackService.create(trackCreateDto);

        assertEquals(expectedTrackResponseDto, actualTrackResponseDto);
        verify(trackDao).create(mockTrack);
        verify(trackMapper).mapToEntity(trackCreateDto);
        verify(trackMapper).mapToResponseDto(mockTrack);
    }

    @Test
    void testCreateWithDatabaseException() {
        when(trackDao.create(trackMapper.mapToEntity(trackCreateDto)))
                .thenThrow(DatabaseException.class);

        assertThrows(DatabaseException.class, () -> trackService.create(trackCreateDto));
        verify(trackDao).create(trackMapper.mapToEntity(trackCreateDto));
    }

    @Test
    void testUpdate() {
        when(trackDao.update(mockTrack)).thenReturn(mockTrack);
        when(trackMapper.mapToEntity(expectedTrackResponseDtoUpdated)).thenReturn(mockTrack);
        when(trackMapper.mapToResponseDto(mockTrack)).thenReturn(expectedTrackResponseDtoUpdated);

        TrackResponseDto actualTrackResponseDto = trackService.update(expectedTrackResponseDtoUpdated);

        assertEquals(expectedTrackResponseDtoUpdated, actualTrackResponseDto);
        verify(trackDao).update(mockTrack);
        verify(trackMapper).mapToEntity(expectedTrackResponseDtoUpdated);
        verify(trackMapper).mapToResponseDto(mockTrack);
    }

    @Test
    void testUpdateWithDatabaseException() {
        when(trackDao.update(trackMapper.mapToEntity(expectedTrackResponseDto)))
                .thenThrow(DatabaseException.class);

        assertThrows(DatabaseException.class, () -> trackService.update(expectedTrackResponseDto));
        verify(trackDao).update(trackMapper.mapToEntity(expectedTrackResponseDto));
    }

    @Test
    void testUpdateTrackWithInvalidData() {
        TrackResponseDto invalidTrackDto = new TrackResponseDto(null, 0.0, null, null, null);
        when(trackDao.update(trackMapper.mapToEntity(invalidTrackDto))).thenReturn(null);

        TrackResponseDto actualTrackResponseDto = trackService.update(invalidTrackDto);

        assertNull(actualTrackResponseDto);
        verify(trackDao).update(trackMapper.mapToEntity(invalidTrackDto));
        verify(trackMapper).mapToResponseDto(isNull());
    }

    @Test
    void testDeleteById() {
        Long trackToDelete = anyLong();
        when(trackDao.deleteById(trackToDelete)).thenReturn(true);

        boolean result = trackService.deleteById(trackToDelete);

        assertTrue(result);
        verify(trackDao).deleteById(trackToDelete);
    }

    @Test
    void testDeleteByIdWithEntityNotFoundException() {
        Long trackId = anyLong();

        when(trackDao.deleteById(trackId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> trackService.deleteById(trackId));
        verify(trackDao).deleteById(trackId);
    }

    @AfterEach
    public void clearData() {
        mockTrack = null;
        expectedTrackResponseDto = null;
        trackCreateDto = null;
        user = null;
        expectedTrackResponseDtoUpdated = null;
        mockTrackList = null;
        expectedTrackResponseDtoList = null;
    }
}