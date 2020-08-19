package com.nimai.kyc.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nimai.kyc.controller.KycController;
import com.nimai.kyc.exception.ErrorDescription;
import com.nimai.kyc.exception.FileStorageException;
import com.nimai.kyc.exception.MyFileNotFoundException;
import com.nimai.kyc.exception.ResourceNotFoundException;
import com.nimai.kyc.model.GenericResponse;
import com.nimai.kyc.model.NimaiFKyc;
import com.nimai.kyc.model.NimaiKyc;
import com.nimai.kyc.model.NimaiMCustomer;
import com.nimai.kyc.payload.KycListRequest;
import com.nimai.kyc.payload.KycListStatusRequest;
import com.nimai.kyc.payload.KycResponse;
import com.nimai.kyc.payload.PagedResponse;
import com.nimai.kyc.payload.kycBase64Request;
import com.nimai.kyc.property.FileStorageProperties;
import com.nimai.kyc.repository.NimaiCustomerRepository;
import com.nimai.kyc.repository.NimaiKycBase64Repository;
import com.nimai.kyc.repository.NimaiKycRepository;
import com.nimai.kyc.util.ModelMapper;
import com.nimai.kyc.util.kycValidator;

@Service
public class KycStorageServiceImpl implements KycStorageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(KycController.class);

	private final Path fileStorageLocation;

	@Autowired
	NimaiKycRepository nimaiKycRepository;

	@Autowired
	NimaiCustomerRepository nimaiCustomerRepository;

	@Autowired
	NimaiKycBase64Repository kycRepository;
	
	@Autowired
	kycValidator  kycValidator;

	@Autowired
	public KycStorageServiceImpl(FileStorageProperties fileStorageProperties) {
		LOGGER.info("KycStorageServiceImpl is been called");
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			LOGGER.error("Unable to find the directory proivded for the uploaded files");
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	public String storeFile(MultipartFile file, String userid, String fileType) {
		LOGGER.info("storeFile is been called");
		if (file.isEmpty()) {
			throw new FileStorageException("Failed to store empty file");
		}

		Optional<NimaiMCustomer> mCustomer = nimaiCustomerRepository.findByUserId(userid);
		if (mCustomer.isPresent()) {
			// System.out.println("<<>><<>>" + mCustomer);

			// Normalize file name
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());

			try {
				// Check if the file's name contains invalid characters
				if (fileName.contains("..")) {
					throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
				}

				String systemName = mCustomer.get().getUserid()
						+ new SimpleDateFormat("dd_MMM_yyyy_kk_mm").format(Calendar.getInstance().getTime()) + "_"
						+ file.getOriginalFilename();

				NimaiFKyc dbFile = new NimaiFKyc();
				dbFile.setFileName(fileName);
				dbFile.setSystemGeneratedName(systemName);
				dbFile.setUserid(mCustomer.get());
				dbFile.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1)
						.toUpperCase());

				nimaiKycRepository.save(dbFile);

				// Copy file to the target location
				Path targetLocation = this.fileStorageLocation.resolve(fileName);
				Files.copy(file.getInputStream(), targetLocation.resolveSibling(systemName),
						StandardCopyOption.REPLACE_EXISTING);

				return systemName;

			} catch (IOException ex) {
				throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
			}
		} else {
			LOGGER.error("User with provided user id is not Present");
			throw new FileStorageException("User Id not presesnt in system");
		}
	}

	public Resource loadFileAsResource(String fileName) {
		LOGGER.info("loadFileAsResource is been called");
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}

	@Override
	public PagedResponse<?> findAllKycData(KycListRequest request) {

		LOGGER.info("findAllKycData is been called");
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.Direction.DESC, "modifiedDate");
		Page<NimaiFKyc> kycList = nimaiKycRepository.findAll(pageable);

		List<KycResponse> kycResponses = kycList.map(kyc -> {
			return ModelMapper.mapKycDetailsToKycResponse(kyc);
		}).getContent();

		return new PagedResponse<>(kycResponses, kycList.getNumber(), kycList.getSize(), kycList.getSize(),
				kycList.getTotalPages(), kycList.isLast());

	}

	@Override
	public ResponseEntity<Object> getKycByUserId(String userId) {
		LOGGER.info("getKycByUserId is been called");

		List<NimaiFKyc> kyc = nimaiKycRepository.getKycDetailsByUserId(userId);

		if (kyc.size() != 0) {

			Stream<Object> kycResponses = kyc.stream().map(kycdETAILS -> {
				return ModelMapper.mapKycDetailsToKycResponse(kycdETAILS);
			});

			return new ResponseEntity<Object>(kycResponses, HttpStatus.OK);
		} else {
			LOGGER.error("User with provided user id is not Present");
			throw new ResourceNotFoundException("No Kyc Details exist for given userId");
		}

	}

	@Override
	public NimaiFKyc saveKycReasonStatus(KycListStatusRequest request) {

		LOGGER.info("saveKycReasonStatus is been called");
		NimaiFKyc fKyc = nimaiKycRepository.getOne(request.getKycId());
		NimaiMCustomer nc = nimaiCustomerRepository.getOne(request.getUserId());

		if (fKyc != null) {

			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date today = Calendar.getInstance().getTime();
			String todayAsString = df.format(today);
			fKyc.setApprovalDate(todayAsString);
			fKyc.setCheckedBy(request.getUserId());
			fKyc.setReason(request.getReason());
			fKyc.setStatus(request.getStatus());

			if (request.getStatus().equals("Success")) {
				nc.setKycStatus("Approved");
				nc.setKycApprovalDate(today);
			} else {
				nc.setKycStatus("Declined");
			}

			nimaiKycRepository.save(fKyc);

			return fKyc;

		} else {
			LOGGER.error("KYC Details not present");
			throw new ResourceNotFoundException("Kyc Details Not found");
		}

	}

	@Override
	public ResponseEntity<Object> getReasonByuserID(String userId) {
		LOGGER.info("getReasonByuserID is been called");

		List<NimaiFKyc> kyc = nimaiKycRepository.getKycDetailsByUserId(userId);

		if (kyc.size() != 0) {

			Stream<Object> kycResponses = kyc.stream().map(kycReasonDetails -> {
				return ModelMapper.mapKycDetailsToKycStatus(kycReasonDetails);
			});

			return new ResponseEntity<Object>(kycResponses, HttpStatus.OK);
		} else {
			throw new ResourceNotFoundException("No Kyc Status exist for given userId");
		}
	}

	/*
	 * while uploading the kyc the kyc status will be 0 in nimaiFKYC as well as it
	 * will update the status on the nimaiCustomer with 0(customer upload the kyc)
	 */
	@Override
	public ResponseEntity<?> saveOrUpdatekycDetails(kycBase64Request kycRequest) {
		GenericResponse response = new GenericResponse();
		String errorString = this.kycValidator.kycRequestValidator(kycRequest);
		if (errorString.equalsIgnoreCase("success")) {
			NimaiMCustomer userId = nimaiCustomerRepository.findByUId(kycRequest.getUserId());
			String customerUserId = userId.getUserid();
			List<NimaiKyc> kycDetailList = kycRepository.getKycDetailsByUserId(kycRequest.getUserId());
			if (userId != null) {

				if (kycRequest.getBusinessDocumentList().size() > 0) {
					List<NimaiKyc> kycList = kycRequest.getBusinessDocumentList().stream().map(mapper -> {
						NimaiKyc nimaiKycDoc = new NimaiKyc();
						nimaiKycDoc.setCustUserId(new NimaiMCustomer(customerUserId));
						nimaiKycDoc = ModelMapper.convertEntityKycRequestToDbObj(mapper, nimaiKycDoc);
						return nimaiKycDoc;
					}).collect(Collectors.toList());

					kycRepository.saveAll(kycList);
					response.setMessage("Busines kyc save successfully");
				}
				if (kycRequest.getPersonalDocumentList().size() > 0) {
					List<NimaiKyc> kycList = kycRequest.getPersonalDocumentList().stream().map(mapper -> {
						NimaiKyc nimaiKycDoc = new NimaiKyc();
						nimaiKycDoc.setCustUserId(new NimaiMCustomer(customerUserId));
						nimaiKycDoc = ModelMapper.convertEntityKycRequestToDbObj(mapper, nimaiKycDoc);
						return nimaiKycDoc;
					}).collect(Collectors.toList());

					kycRepository.saveAll(kycList);
					response.setMessage("Personal kyc save successfully");
				}

				nimaiCustomerRepository.updateKycStatus("Uploaded", userId.getUserid());
				response.setErrCode("EXE000");
				return new ResponseEntity<Object>(response, HttpStatus.OK);

			}
		} else {
			response.setErrCode("EXE000");
			response.setMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

		response.setErrCode("User not registred");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
}
