package com.astrodust.familyStoryBook_backend.dto;

import java.time.LocalDateTime;
import java.util.Calendar;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
	
	@ApiModelProperty(value = "EventId")
	private int id;
	
	@NotEmpty(message = "Event-name is required")
	private String eventName;
	
	@NotEmpty(message = "Event-details is required")
	private String eventDetails;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDateTime eventDateTime;
	
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	
	private int family_id;
}
