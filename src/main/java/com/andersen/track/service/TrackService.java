package com.andersen.track.service;

import java.time.LocalDate;
import java.util.List;

public interface TrackService {

    List<TrackResponseDto> getByData(LocalDate localDate);

    TrackResponseDto getById(Long id);

    List<TrackResponseDto> getByUserId(Long userId);

    TrackResponseDto create(TrackCreateDto trackCreateDto);

    TrackResponseDto update(TrackResponseDto trackResponseDto);

    void deleteById(Long id);
}
