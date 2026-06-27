package com.AftabShaikh.resumeBuilderapi.Exception;

// Zaroori imports: Collections aur Spring classes
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice // Ye class poore project ke exceptions (errors) ko ek jagah handle karegi
@Slf4j
public class GlobalExceptionHandler
{


    // @Valid validation fail hone par MethodArgumentNotValidException aata hai
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) 
    {
        
    	log.info("Inside GlobalException - handleValidationExceptions()");
    	
        // Ek map banaya field-wise errors store karne ke liye (e.g. "email": "invalid format")
        Map<String, String> errors = new HashMap<>();

        // Saare validation errors par loop chala kar unka naam aur message nikaal rahe hain
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField(); // Field ka naam (e.g. 'name' ya 'email')
            String errorMessage = error.getDefaultMessage();  // Wo message jo DTO mein likha tha
            errors.put(fieldName, errorMessage); // Map mein add kar diya
        });

        // Final response structure taiyar kar rahe hain (JSON format ke liye)
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Validation Failed"); // Ek general message
        response.put("errors", errors);               // Detailed field errors ka list

        // HTTP 400 (Bad Request) status ke saath response bhej rahe hain
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Duplicate User ya koi aur Runtime error handle karne ke liye (Optional par useful)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }
    
    //create method to handal exception 
 // 1. @ExceptionHandler mein class ka naam sahi (Resource) hona chahiye
    @ExceptionHandler(ResourceExistsException.class)
    // 2. Map ke baad < lagana zaroori hai: Map<String, Object>
    public ResponseEntity<Map<String, Object>> handleResourceExistsException(ResourceExistsException ex) 
    {
        

        // Response map taiyar kar rahe hain
        Map<String, Object> response = new HashMap<>();
        
        // "message" mein ek general title
        response.put("message", "Resource exists");
        
        // "errors" mein wo message jo Service se aaya (e.g. "User already exists")
        response.put("errors", ex.getMessage());
        
        // HTTP 409 CONFLICT: SQL mein duplicate data ke liye ye sabse best status code hai
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    //jab user pehle se hi exit kr rha hoga tb ye exception aayega  409 conflict
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGenericException(Exception ex)
    {
    	log.info("Inside GlobalException - handleGenericException()");

        // Response map taiyar kar rahe hain
        Map<String, Object> response = new HashMap<>();
        
        // "message" mein ek general title
        response.put("message", "Something went wrong.Contact administrator");
        
        // "errors" mein wo message jo Service se aaya (e.g. "User already exists")
        response.put("errors", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    	
    }

}
