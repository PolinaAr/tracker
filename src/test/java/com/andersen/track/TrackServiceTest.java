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
    private TrackMapper trackMapperMock;
    @Mock
    private TrackDao trackDaoMock;
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
        when(trackDaoMock.getByDate(date)).thenReturn(mockTrackList);
        when(trackMapperMock.mapToResponseDto(mockTrackList.get(0))).thenReturn(expectedTrackResponseDtoList.get(0));

        List<TrackResponseDto> actualTrackResponseDtoList = trackService.getByData(date);

        assertEquals(1, actualTrackResponseDtoList.size());
        assertEquals(expectedTrackResponseDtoList, actualTrackResponseDtoList);
        verify(trackDaoMock).getByDate(date);
        verify(trackMapperMock).mapToResponseDto(mockTrack);
    }

    @Test
    void testGetByDataWithNoData() {
        when(trackDaoMock.getByDate(date)).thenReturn(Collections.emptyList());

        List<TrackResponseDto> actualTrackResponseDtoList = trackService.getByData(date);

        assertEquals(0, actualTrackResponseDtoList.size());
        verify(trackDaoMock).getByDate(date);
    }

    @Test
    void testGetById() {
        Long trackId = anyLong();
        when(trackDaoMock.getById(trackId)).thenReturn(mockTrack);
        when(trackMapperMock.mapToResponseDto(mockTrack)).thenReturn(expectedTrackResponseDto);

        TrackResponseDto actualTrackResponseDto = trackService.getById(trackId);

        assertEquals(expectedTrackResponseDto, actualTrackResponseDto);
        verify(trackDaoMock).getById(trackId);
        verify(trackMapperMock).mapToResponseDto(mockTrack);
    }

    @Test
    void testGetByIdWithIncorrectId() {
        Long trackId = anyLong();

        when(trackDaoMock.getById(trackId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> trackService.getById(trackId));
        verify(trackDaoMock).getById(trackId);
    }

    @Test
    void testGetByUserId() {
        Long userId = user.getId();
        when(trackDaoMock.getByUserId(userId)).thenReturn(List.of(mockTrack));
        when(trackMapperMock.mapToResponseDto(mockTrack)).thenReturn(expectedTrackResponseDto);

        List<TrackResponseDto> actualList = trackService.getByUserId(userId);
        TrackResponseDto actualTrackResponseDto = actualList.get(0);

        assertEquals(1, actualList.size());
        assertEquals(expectedTrackResponseDto, actualTrackResponseDto);
        verify(trackDaoMock).getByUserId(userId);
        verify(trackMapperMock).mapToResponseDto(mockTrack);
    }

    @Test
    void testGetByUserIdWithNoData() {
        Long userId = anyLong();

        when(trackDaoMock.getByUserId(userId)).thenReturn(Collections.emptyList());

        List<TrackResponseDto> actualTrackResponseDtoList = trackService.getByUserId(userId);

        assertEquals(0, actualTrackResponseDtoList.size());
        verify(trackDaoMock).getByUserId(userId);
    }

    @Test
    void testCreate() {
        when(trackDaoMock.create(mockTrack)).thenReturn(mockTrack);
        when(trackMapperMock.mapToEntity(trackCreateDto)).thenReturn(mockTrack);
        when(trackMapperMock.mapToResponseDto(mockTrack)).thenReturn(expectedTrackResponseDto);

        TrackResponseDto actualTrackResponseDto = trackService.create(trackCreateDto);

        assertEquals(expectedTrackResponseDto, actualTrackResponseDto);
        verify(trackDaoMock).create(mockTrack);
        verify(trackMapperMock).mapToEntity(trackCreateDto);
        verify(trackMapperMock).mapToResponseDto(mockTrack);
    }

    @Test
    void testCreateWithEntityNotFoundException() {
        when(trackDaoMock.create(trackMapperMock.mapToEntity(trackCreateDto)))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> trackService.create(trackCreateDto));
        verify(trackDaoMock).create(trackMapperMock.mapToEntity(trackCreateDto));
    }

    @Test
    void testCreateWithDatabaseException() {
        when(trackDaoMock.create(trackMapperMock.mapToEntity(trackCreateDto)))
                .thenThrow(DatabaseException.class);

        assertThrows(DatabaseException.class, () -> trackService.create(trackCreateDto));
        verify(trackDaoMock).create(trackMapperMock.mapToEntity(trackCreateDto));
    }

    @Test
    void testUpdate() {
        when(trackDaoMock.update(mockTrack)).thenReturn(mockTrack);
        when(trackMapperMock.mapToEntity(expectedTrackResponseDtoUpdated)).thenReturn(mockTrack);
        when(trackMapperMock.mapToResponseDto(mockTrack)).thenReturn(expectedTrackResponseDtoUpdated);

        TrackResponseDto actualTrackResponseDto = trackService.update(expectedTrackResponseDtoUpdated);

        assertEquals(expectedTrackResponseDtoUpdated, actualTrackResponseDto);
        verify(trackDaoMock).update(mockTrack);
        verify(trackMapperMock).mapToEntity(expectedTrackResponseDtoUpdated);
        verify(trackMapperMock).mapToResponseDto(mockTrack);
    }

    @Test
    void testUpdateWithEntityNotFoundException() {
        when(trackDaoMock.update(trackMapperMock.mapToEntity(expectedTrackResponseDto)))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> trackService.update(expectedTrackResponseDto));
        verify(trackDaoMock).update(trackMapperMock.mapToEntity(expectedTrackResponseDto));
    }

    @Test
    void testUpdateWithDatabaseException() {
        when(trackDaoMock.update(trackMapperMock.mapToEntity(expectedTrackResponseDto)))
                .thenThrow(DatabaseException.class);

        assertThrows(DatabaseException.class, () -> trackService.update(expectedTrackResponseDto));
        verify(trackDaoMock).update(trackMapperMock.mapToEntity(expectedTrackResponseDto));
    }

    @Test
    void testDeleteById() {
        Long trackId = anyLong();
        when(trackDaoMock.deleteById(trackId)).thenReturn(true);

        boolean result = trackService.deleteById(trackId);

        assertTrue(result);
        verify(trackDaoMock).deleteById(trackId);
    }

    @Test
    void testDeleteByIdWithEntityNotFoundException() {
        Long trackId = anyLong();

        when(trackDaoMock.deleteById(trackId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> trackService.deleteById(trackId));
        verify(trackDaoMock).deleteById(trackId);
    }

    @Test
    void testDeleteByIdWithDatabaseException() {
        Long trackId = anyLong();

        when(trackDaoMock.deleteById(trackId)).thenThrow(DatabaseException.class);

        assertThrows(DatabaseException.class, () -> trackService.deleteById(trackId));
        verify(trackDaoMock).deleteById(trackId);
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