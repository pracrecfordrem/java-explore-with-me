package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.NewCompilationDto;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public Compilation createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> eventList;
        if (newCompilationDto.getEvents() != null) {
           eventList = newCompilationDto.getEvents().stream().map(eventRepository::getEventById).toList();
        } else {
            eventList = null;
        }
        return compilationRepository.save(new Compilation(eventList,null,newCompilationDto.getPinned() != null ? newCompilationDto.getPinned() : false,newCompilationDto.getTitle()));
    }

    public Compilation updateCompilation(NewCompilationDto newCompilationDto, Long compId) {
        List<Event> eventList;
        if (newCompilationDto.getEvents() != null) {
            eventList = newCompilationDto.getEvents().stream().map(eventRepository::getEventById).toList();
        } else {
            eventList = null;
        }
        return compilationRepository.save(new Compilation(eventList,compId,newCompilationDto.getPinned(),newCompilationDto.getTitle()));
    }



    public List<Compilation> getCompilations(Boolean pinned, Integer from, Integer size) {
        return compilationRepository.getCompilations(pinned);
    }

    public Compilation getCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElse(null);
    }

    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }
}
