package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.request.*;
import ru.practicum.repository.RequestRepository;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class RequestService {
    private final RequestRepository requestRepository;
    public Request createRequest(Request request) {
        return requestRepository.save(request);
    }

    public List<Request> getRequestsByRequester(Long userID) {
        return requestRepository.getRequestsByRequester(userID);
    }

    public List<Request> getPersonalEventRequests(Long userId, Long eventId) {
        return requestRepository.getPersonalEventRequests(eventId);
    }

    public Request getRequestById(Long requestId) {
        return requestRepository.findById(requestId).orElse(null);
    }

    public RequestUserInfo answerRequests(Long userId, Long eventId, RequestChangeStatus requestChangeStatus) {
        for (Long requestId : requestChangeStatus.getRequestIds()) {
            Request request = requestRepository.findById(requestId).orElse(null);
            request.setStatus(requestChangeStatus.getStatus());
            requestRepository.save(request);
        }
        List<Request> requestList = requestRepository.getRequestsByEventId(eventId);
        RequestUserInfo requestUserInfo = new RequestUserInfo();
        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();
        for (Request request: requestList) {
            if (request.getStatus().equals("CONFIRMED")) {
                confirmedRequests.add(RequestMapper.toRequestDto(request));
            } else if (request.getStatus().equals("REJECTED")) {
                rejectedRequests.add(RequestMapper.toRequestDto(request));
            }
        }
        requestUserInfo.setConfirmedRequests(confirmedRequests);
        requestUserInfo.setRejectedRequests(rejectedRequests);
        return requestUserInfo;
    }

    public Request getRequestByRequesterAndEvent(Long userId, Long eventId) {
        return requestRepository.getRequestByRequester_idAndEvent_id(userId, eventId);
    }
}
