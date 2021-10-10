package com.astrodust.familyStoryBook_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Data
@ApiModel(value = "MemberInsertJobDTO", description = "Member-job fields for insertion")
public class MemberInsertJobDTO {

    @ApiModelProperty(value = "JobIds")
    private List<Integer> id;

    @NotEmpty(message = "Company-Name is required")
    private List<String> companyName = new ArrayList<>();

    @NotEmpty(message = "Job-Role is required")
    private List<String> jobRole = new ArrayList<>();

    @NotNull(message = "Start-Date is required")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private List<Calendar> joinDate = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private List<Calendar> endDate = new ArrayList<>();
}
