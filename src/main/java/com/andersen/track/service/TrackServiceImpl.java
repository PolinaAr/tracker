package com.andersen.track.service;

import com.andersen.track.dao.TrackDao;
import com.andersen.track.dao.TrackDaoImpl;

import java.time.LocalDate;
import java.util.List;

public class TrackServiceImpl implements TrackService{

    private final TrackDao trackDao = TrackDaoImpl.getInstance();

    @Override
    public List<TrackResponseDto> getAllTracks() {
        return null;
    }

    @Override
    public List<TrackResponseDto> getByData(LocalDate localDate) {
        return null;
    }

    @Override
    public TrackResponseDto getById(Long id) {
        return null;
    }

    @Override
    public TrackResponseDto create(TrackCreateDto trackCreateDto) {
        return null;
    }

    @Override
    public TrackResponseDto update(TrackResponseDto track) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
