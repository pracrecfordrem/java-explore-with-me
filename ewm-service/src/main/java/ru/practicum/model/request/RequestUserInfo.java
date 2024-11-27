package ru.practicum.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUserInfo {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;

}
