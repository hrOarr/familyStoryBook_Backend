package com.astrodust.familyStoryBook_backend.helpers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import com.astrodust.familyStoryBook_backend.dto.*;
import com.astrodust.familyStoryBook_backend.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Converter {
	private static final Logger logger = LogManager.getLogger(Converter.class);
	private ModelMapper modelMapper;
	private ImageCompressionDecom imageCompressionDecom;
	
	@Autowired
	public Converter(ModelMapper modelMapper, ImageCompressionDecom imageCompressionDecom) {
		this.modelMapper = modelMapper;
		this.imageCompressionDecom = imageCompressionDecom;
	}
	
	public MemberAccount memberInfoGeneralToMem(MemberInfoGeneralDTO memberInfoGeneralDTO, FamilyAccount familyAccount, MemberAccount parent) {
		logger.info("Converter memberInfoGeneralToMem() method init->>>>>>>>>>");
		MemberAccount account = modelMapper.map(memberInfoGeneralDTO, MemberAccount.class);
		// family account set
		account.setFamilyAccount(familyAccount);
		
		// if root or have parent
		if(parent!=null) {
			account.setParent(parent);
			if(account.getId()<=0) parent.getChildren().add(account);
		}
		
		if(account.getId()<=0) { // insertion
			account.setCreatedDate(LocalDateTime.now());
		}
		else { // updation
			account.setUpdatedDate(LocalDateTime.now());
		}
		
		familyAccount.getMemberAccounts().add(account);
		
		return account;
	}
	
	public MemberInfoGeneralDTO memberToMemberInfoGeneralDTO(MemberAccount memberAccount) {
		logger.info("Converter memberToMemberInfoGeneralDTO() method init->>>>>>>>>>>>");
		MemberInfoGeneralDTO memberInfoGeneralDTO = modelMapper.map(memberAccount, MemberInfoGeneralDTO.class);
		return memberInfoGeneralDTO;
	}
	
	public MemberDTO memberToMemDTO(MemberAccount account) {
		logger.info("Converter memberToMemDTO() method init->>>>>>>>>>>>");
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		MemberDTO memberDTO = modelMapper.map(account, MemberDTO.class);
		memberDTO.setName(memberDTO.getFirstName()+" "+memberDTO.getLastName());
		memberDTO.setFamily_id(account.getFamilyAccount().getId());
		//if(account.getParent()!=null)memberDTO.setParent_id(account.getParent().getId());
		System.out.println(memberDTO + " -???????");
		return memberDTO;
	}
	
	public Event eventDTOtoEvent(EventDTO eventDTO, FamilyAccount familyAccount) {
		logger.info("Converter eventDTOtoEvent() method init->>>>>>>>>>>>>>>");
		Event event = modelMapper.map(eventDTO, Event.class);
		if(eventDTO.getId()<=0) { // insertion
			event.setCreatedDate(LocalDateTime.now());
			event.setFamilyAccount(familyAccount);
		}
		else { // updation
			event.setUpdatedDate(LocalDateTime.now());
			event.setFamilyAccount(familyAccount);
		}
		return event;
	}
	
	public List<MemberEducation> memberInsertEducationDTOtoMemberEducation(List<MemberInsertEducationDTO> memberInsertEducationDTO, MemberAccount memberAccount){
		logger.info("Converter memberEducationDTOtoMemberEducation() method init->>>>>>>>>>>>>>>");
		List<MemberEducation> educations = new ArrayList<MemberEducation>();

		for(MemberInsertEducationDTO insertEducationDTO:memberInsertEducationDTO) {
			MemberEducation memberEducation = new MemberEducation();
			memberEducation.setInstitution(insertEducationDTO.getInstitution());
			memberEducation.setDescription(insertEducationDTO.getDescription());
			memberEducation.setStartDate(insertEducationDTO.getStartDate());
			memberEducation.setEndDate(insertEducationDTO.getEndDate());
			memberEducation.setMemberAccount(memberAccount);
			educations.add(memberEducation);
			logger.info("SoA:: memberEducation = " + memberEducation);
		}
		
		return educations;
	}

	public MemberEducation memberUpdateEducationDTOtoMemberEducation(MemberUpdateEducationDTO memberUpdateEducationDTO, MemberAccount memberAccount){
		MemberEducation memberEducation = modelMapper.map(memberUpdateEducationDTO, MemberEducation.class);
		memberEducation.setMemberAccount(memberAccount);
		logger.info("SoA:: " + memberEducation.getId() + " " + memberEducation.getInstitution());
		return memberEducation;
	}

	public List<MemberJob> memberInsertJobDTOtoMemberJob(List<MemberInsertJobDTO> memberInsertJobDTO, MemberAccount memberAccount){
		List<MemberJob> memberJobs = new ArrayList<>();

		for(MemberInsertJobDTO insertJobDTO:memberInsertJobDTO){
			MemberJob memberJob = new MemberJob();
			memberJob.setCompanyName(insertJobDTO.getCompanyName());
			memberJob.setJobRole(insertJobDTO.getJobRole());
			memberJob.setDescription(insertJobDTO.getDescription());
			memberJob.setLocation(insertJobDTO.getLocation());
			memberJob.setJoinDate(insertJobDTO.getJoinDate());
			memberJob.setEndDate(insertJobDTO.getEndDate());
			memberJob.setMemberAccount(memberAccount);
			memberJobs.add(memberJob);
		}
		return memberJobs;
	}

	public MemberJob MemberUpdateJobDTOtoMemberJob(MemberUpdateJobDTO memberUpdateJobDTO, MemberAccount memberAccount){
		MemberJob memberJob = modelMapper.map(memberUpdateJobDTO, MemberJob.class);
		memberJob.setMemberAccount(memberAccount);
		return memberJob;
	}

	public MiscellaneousDocument insertDocumentDTOtoDocument(InsertDocumentDTO insertDocumentDTO, FamilyAccount familyAccount) throws IOException {
		MiscellaneousDocument document = new MiscellaneousDocument();
		// title
		document.setTitle(insertDocumentDTO.getTitle());
		// desc
		document.setDescription(insertDocumentDTO.getDescription());
		// addedDate
		document.setAddedDate(LocalDateTime.now());
		// familyAccount
		document.setFamilyAccount(familyAccount);
		// images
		for(MultipartFile image:insertDocumentDTO.getImages()){
			ImageModel imageModel = new ImageModel();
			imageModel.setName(image.getOriginalFilename());
			imageModel.setType(image.getContentType());
			imageModel.setPicByte(imageCompressionDecom.compressBytes(image.getBytes()));
			document.getImages().add(imageModel);
		}
		// files
		for(MultipartFile file:insertDocumentDTO.getFiles()){
			FileModel fileModel = new FileModel();
			fileModel.setName(file.getOriginalFilename());
			fileModel.setType(file.getContentType());
			fileModel.setFileByte(imageCompressionDecom.compressBytes(file.getBytes()));
			document.getFiles().add(fileModel);
		}

		return document;
	}
}
