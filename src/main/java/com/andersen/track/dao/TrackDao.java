package com.andersen.track.dao;

import java.time.LocalDate;
import java.util.List;

public interface TrackDao {

    List<Track> getByUserId(Long id);

    Track getById(Long id);

    List<Track> getByDate(LocalDate localDate);

    Track create(Track track);

    Track update(Track track);

    boolean deleteById(Long id);
}
