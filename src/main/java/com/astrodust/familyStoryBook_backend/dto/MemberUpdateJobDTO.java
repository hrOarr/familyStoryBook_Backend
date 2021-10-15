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
@ApiModel(value = "MemberUpdateJobDTO", description = "Job fields for update")
public class MemberUpdateJobDTO {

    @ApiModelProperty(value = "JobId")
    private int id;

    @NotEmpty(message = "Company-Name is required")
    private String companyName;

    @NotEmpty(message = "Job-Role is required")
    private String jobRole;

    @NotNull(message = "Start-Date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime joinDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime endDate;
}
