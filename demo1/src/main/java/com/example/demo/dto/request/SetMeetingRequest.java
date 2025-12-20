package com.example.demo.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetMeetingRequest {

    @NotBlank(message = "Meeting Location is Required")
    @Size(min = 100, max = 500)
    private String meetingLocation;

    @Size(max=2000, message = "Meeting notes should not exceed 2000 characters")
    private String meetingNotes;


    @Future(message = "Meeting time should be in the future")
    private LocalDateTime meetingTime;
}
