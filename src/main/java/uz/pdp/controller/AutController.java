package uz.pdp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import uz.pdp.payload.BankDto;
import uz.pdp.payload.LoginDto;
import uz.pdp.repository.UserRepository;
import uz.pdp.payload.CardAndBankAutomatDto;
import uz.pdp.service.ApiResponse;
import uz.pdp.service.AutService;
import uz.pdp.service.TransferService;
import uz.pdp.service.AddDataService;

@Slf4j
@RestController
@RequestMapping("api/auth")
public class AutController {

	@Autowired
	AddDataService addDataService;
	
	@Autowired
	AutService autService;
	
	@Autowired
	TransferService transferService;
	
	@Autowired
	UserRepository userRepository;
	
//	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/addAll")
	public HttpEntity<?> addAll(@Valid @RequestBody BankDto bankDto){
		ApiResponse apiResponse = addDataService.addAllU(bankDto);
		return ResponseEntity.status(apiResponse.isSuccesfuly()?201:409).body(apiResponse);
		
	}
	@PostMapping("/login")
	HttpEntity<?> login(@Valid @RequestBody LoginDto loginDto){
		ApiResponse apiResponse= autService.login(loginDto);
		return ResponseEntity.status(apiResponse.isSuccesfuly()? 200:400).body(apiResponse);
	}
	@PreAuthorize("hasAnyRole('EMPLOYER','DIREKTOR','CUSTOM')")
	@PostMapping("/transfer")
	@Transactional
	HttpEntity<?> monyTransfer(@Valid @RequestBody CardAndBankAutomatDto transferDto){
		ApiResponse apiResponse = transferService.monyTransfer(transferDto);
		return ResponseEntity.status(apiResponse.isSuccesfuly()? 200:400).body(apiResponse);
	}

	@GetMapping("/getAll")
	HttpEntity<?> getAllUser(){
		return ResponseEntity.status(200).body(userRepository.findAll());
	}
	
	
	
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return errors;
	}
	
	@RestControllerAdvice()
	public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

		@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
		protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
			String bodyOfResponse = "You are not allowed to execute";
			log.info("In RestResponseEntityExceptionHandler");
			return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
		}
	}
}
