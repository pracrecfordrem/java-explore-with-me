package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.StatsRequestDto;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;
import ru.practicum.model.StatsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    public Stats post(StatsRequestDto statsRequestDto) {
        return statsRepository.save(StatsMapper.toStats(statsRequestDto));
    }

    public List<Hit> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean isUnique) {
        return isUnique ? statsRepository.getDistinctIpHits(start,end,uris).stream().sorted().toList() : statsRepository.getHits(start,end,uris).stream().sorted().toList();
    }
}
