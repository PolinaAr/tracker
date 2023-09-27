package com.andersen.track;

import com.andersen.track.dao.Track;
import com.andersen.track.service.TrackCreateDto;
import com.andersen.track.service.TrackResponseDto;
import com.andersen.user.dao.User;
import com.andersen.user.service.UserCreateDto;
import com.andersen.user.service.UserResponseDto;

public class TrackMapper {

    private static TrackMapper trackMapper;

    public static TrackMapper getInstance() {
        if (trackMapper == null) {
            trackMapper = new TrackMapper();
        }
        return trackMapper;
    }

    public Track mapToEntity(TrackResponseDto trackResponseDto) {
        return Track.builder()
                .id(trackResponseDto.getId())
                .time(trackResponseDto.getTime())
                .note(trackResponseDto.getNote())
                .date(trackResponseDto.getDate())
                .userId(trackResponseDto.getUserId())
                .build();
    }

    public Track mapToEntity(TrackCreateDto trackCreateDto){
        return Track.builder()
                .time(trackCreateDto.getTime())
                .note(trackCreateDto.getNote())
                .date(trackCreateDto.getDate())
                .userId(trackCreateDto.getUserId())
                .build();
    }

    public TrackResponseDto mapToResponseDto(Track track) {
        return TrackResponseDto.builder()
                .id(track.getId())
                .time(track.getTime())
                .note(track.getNote())
                .date(track.getDate())
                .userId(track.getUserId())
                .build();
    }
}
