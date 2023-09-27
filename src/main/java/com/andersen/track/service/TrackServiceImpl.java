package com.andersen.track.service;

import com.andersen.track.TrackMapper;
import com.andersen.track.dao.Track;
import com.andersen.track.dao.TrackDao;
import com.andersen.track.dao.TrackDaoImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TrackServiceImpl implements TrackService {

    private TrackDao trackDao = TrackDaoImpl.getInstance();
    private TrackMapper trackMapper = TrackMapper.getInstance();
    private static TrackService trackService;

    public static TrackService getInstance() {
        if (trackService == null) {
            trackService = new TrackServiceImpl();
        }
        return trackService;
    }

    @Override
    public List<TrackResponseDto> getByData(LocalDate localDate) {
        return trackDao.getByDate(localDate).stream()
                .map(trackMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public TrackResponseDto getById(Long id) {
        return trackMapper.mapToResponseDto(trackDao.getById(id));
    }

    @Override
    public List<TrackResponseDto> getByUserId(Long userId) {
        return trackDao.getByUserId(userId).stream()
                .map(trackMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public TrackResponseDto create(TrackCreateDto trackCreateDto) {
        Track track = trackDao.create(trackMapper.mapToEntity(trackCreateDto));
        return trackMapper.mapToResponseDto(track);
    }

    @Override
    public TrackResponseDto update(TrackResponseDto trackResponseDto) {
        Track track = trackDao.update(trackMapper.mapToEntity(trackResponseDto));
        return trackMapper.mapToResponseDto(track);
    }

    @Override
    public boolean deleteById(Long id) {
        return trackDao.deleteById(id);
    }
}
