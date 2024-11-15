package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.request.Request;
import ru.practicum.repository.RequestRepository;

@AllArgsConstructor
@Service
public class RequestService {
    private final RequestRepository requestRepository;
    public Request createRequest(Request request) {
        return requestRepository.save(request);
    }
}
