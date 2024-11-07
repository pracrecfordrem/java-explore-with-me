package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.StatsRequestDto;
import ru.practicum.model.Stats;
import ru.practicum.model.StatsMapper;
import ru.practicum.repository.StatsRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class StatsService {
    private final StatsRepository statsRepository;
    public Stats post(StatsRequestDto statsRequestDto) {
        return statsRepository.save(StatsMapper.toStats(statsRequestDto));
    }

    public List<Stats> get(String start, String end, List<String> uris, Boolean isUnique) {

    }
}
