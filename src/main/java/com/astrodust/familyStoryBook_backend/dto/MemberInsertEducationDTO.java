package com.astrodust.familyStoryBook_backend.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "memberInsertEducationDTO", description = "Member Education Fields")
public class MemberInsertEducationDTO {

    @ApiModelProperty(value = "eduIds")
    private List<Integer> id = new ArrayList<Integer>();

    @NotEmpty(message = "Institution Name is required")
    private List<String> institution = new ArrayList<String>();

    private List<String> description = new ArrayList<String>();

    @NotNull(message = "StartDate is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private List<LocalDateTime> startDate = new ArrayList<LocalDateTime>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private List<LocalDateTime> endDate = new ArrayList<LocalDateTime>();
}
