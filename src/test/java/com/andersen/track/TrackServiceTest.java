package com.andersen.track;


import com.andersen.track.dao.Track;
import com.andersen.track.dao.TrackDao;
import com.andersen.track.dao.TrackDaoImpl;
import com.andersen.track.service.TrackCreateDto;
import com.andersen.track.service.TrackResponseDto;
import com.andersen.track.service.TrackService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackServiceTest {

    @InjectMocks
    private TrackServiceImpl trackService;
    @Mock
    private TrackDaoImpl trackDaoMock;
    @Mock
    private TrackMapper trackMapperMock;

    private Track mockTrack;
    private TrackCreateDto trackCreateDto;
    private TrackResponseDto expectedTrackResponseDto;

    private User user;

    @BeforeEach
    public void initData() {
        long id = 1L;
        LocalDate date = LocalDate.of(2023, 9, 27);
        double time = 3.5;
        String report = "My new report";
        user = new User(id, "Anton", "Anton", "anton@mail.com", "12345");
        mockTrack = new Track(id, time, report, date, user.getId());
        trackCreateDto = new TrackCreateDto(time, report, date, user.getId());
        expectedTrackResponseDto = new TrackResponseDto(id, time, report, date, user.getId());
    }

    @Test
    void testGetByData() {

    }

    @Test
    void testGetById() {

    }

    @Test
    void testGetByUserId() {

    }

    @Test
    void testCreate() {
        when(trackMapperMock.mapToEntity(trackCreateDto)).thenReturn(mockTrack);
        when(trackDaoMock.create(mockTrack)).thenReturn(mockTrack);
        when(trackMapperMock.mapToResponseDto(mockTrack)).thenReturn(expectedTrackResponseDto);

        TrackResponseDto actualTrackResponseDto = trackService.create(trackCreateDto);

        assertEquals(expectedTrackResponseDto, actualTrackResponseDto);
        verify(trackMapperMock).mapToEntity(trackCreateDto);
        verify(trackDaoMock).create(mockTrack);
        verify(trackMapperMock).mapToResponseDto(mockTrack);
    }

    @Test
    void testUpdate() {

    }

    @Test
    void testDeleteById() {

    }

    @AfterEach
    public void clearData() {
        mockTrack = null;
        expectedTrackResponseDto = null;
        trackCreateDto = null;
        user = null;
    }
}