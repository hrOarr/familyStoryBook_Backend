package com.astrodust.familyStoryBook_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Calendar;

@Data
@ApiModel(value = "MemberUpdateEducationDTO", description = "Education DTO for update")
public class MemberUpdateEducationDTO {

    @ApiModelProperty(value = "EduId")
    private int id;

    @NotEmpty(message = "Institution name is required")
    private String institution;
    private String description;

    @NotNull(message = "StartDate is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime endDate;
}
