package ru.practicum.model.location;

public class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        return new Location(null, locationDto.getLat(), locationDto.getLon());
    }
}
