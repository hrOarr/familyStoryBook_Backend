package com.astrodust.familyStoryBook_backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate eventDateTime;
	
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	
	private int family_id;
}
