package com.andersen.track.service;

import java.time.LocalDate;
import java.util.List;

public interface TrackService {

    List<TrackResponseDto> getAllTracks();

    List<TrackResponseDto> getByData(LocalDate localDate);

    TrackResponseDto getById(Long id);

    TrackResponseDto create(TrackCreateDto trackCreateDto);

    TrackResponseDto update(TrackResponseDto track);

    void deleteById(Long id);
}
