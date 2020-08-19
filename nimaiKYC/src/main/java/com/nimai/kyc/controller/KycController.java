package com.nimai.kyc.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nimai.kyc.exception.ErrorDescription;
import com.nimai.kyc.exception.FileStorageException;
import com.nimai.kyc.model.GenericResponse;
import com.nimai.kyc.payload.KycListRequest;
import com.nimai.kyc.payload.KycListStatusRequest;
import com.nimai.kyc.payload.KycUploadResponse;
import com.nimai.kyc.payload.PagedResponse;
import com.nimai.kyc.payload.kycBase64Request;
import com.nimai.kyc.property.FileExtenValidation;
import com.nimai.kyc.service.KycStorageService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/kyc")
public class KycController {

	private static final Logger LOGGER = LoggerFactory.getLogger(KycController.class);

	@Autowired
	private KycStorageService fileStorageService;

	public KycUploadResponse uploadFile(@RequestParam("file") MultipartFile file, String userid, String fileType) {
		String fileName = "";

		if (FileExtenValidation.validateFileExtn(file.getOriginalFilename()))
			fileName = fileStorageService.storeFile(file, userid, fileType);
		else
			throw new FileStorageException("The selected file " + file.getOriginalFilename()
					+ " could not be uploaded. Only PDF, JPG, JPEG, PNG and GIF images are allowed.");

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
				.path(fileName).toUriString();
		System.out.println("File details : " + fileName + " : " + fileDownloadUri);

		return new KycUploadResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}

	@PostMapping("/uploadMultipleFiles")
	public List<KycUploadResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
			@RequestParam("userId") String userId, @RequestParam("fileType") String fileType) {
		LOGGER.info("======================== Kyc Controller -> uploadMultipleFiles========================");

		System.out.println(files.length);
		return Arrays.asList(files).stream().map(file -> uploadFile(file, userId, fileType))
				.collect(Collectors.toList());
	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

		LOGGER.info("======================== Kyc Controller -> downloadFile========================");

		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			LOGGER.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	/**
	 * Get all list of data
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/list")
	public PagedResponse<?> findAllKycDetails(@RequestBody KycListRequest request) {
		LOGGER.info("======================== Kyc Controller -> findAllKycDetails========================");

		return fileStorageService.findAllKycData(request);
	}

	/**
	 * Getting particular userId KycDetails
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<Object> getKycDetailsById(@PathVariable(value = "userId") String userId) {
		LOGGER.info("======================== Kyc Controller -> getKycDetailsById========================");
		return fileStorageService.getKycByUserId(userId);
	}

	/**
	 * Saving Reason For rejecting KycDetails from ADmin portal
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/kycCheckStatus")
	public ResponseEntity<Object> kycStatus(@RequestBody List<KycListStatusRequest> kycStatus) {

		LOGGER.info("======================== Kyc Controller -> kycStatus========================");

		GenericResponse response = new GenericResponse<>();
		kycStatus.forEach(kyc -> fileStorageService.saveKycReasonStatus(kyc));
		response.setMessage("ASA001");
		response.setMessage(ErrorDescription.getDescription("ASA001"));
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	/**
	 * Reason for rejecting response to user
	 * 
	 * @param request
	 * @return
	 */

	@GetMapping("/reason/{userId}")
	public ResponseEntity<Object> getKycReasonByUserId(@PathVariable(value = "userId") String userId) {
		LOGGER.info("======================== Kyc Controller -> getKycReasonByUserId========================");

		return fileStorageService.getReasonByuserID(userId);

	}

	/*
	 * kyc status is keeping 0,1,2 o is for first time or update purpose, 1 is for
	 * approval of kyc,2 is for rejected by nimai team,while sending the data from front end documeny 
	 * list array should contain business data in 0 index and personal data in 1 index 
	 */
	@PostMapping("/saveKycDoc")
	public ResponseEntity<?> uploadFileBase64(@RequestBody kycBase64Request kycRequest){
		return fileStorageService.saveOrUpdatekycDetails(kycRequest);
		
	}
	
	
	
}
