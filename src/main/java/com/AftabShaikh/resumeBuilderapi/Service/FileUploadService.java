package com.AftabShaikh.resumeBuilderapi.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.AftabShaikh.resumeBuilderapi.Dto.AuthResponse;
import com.AftabShaikh.resumeBuilderapi.Entity.Resume;
import com.AftabShaikh.resumeBuilderapi.Repository.ResumeRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
/*
“This class uploads images to Cloudinary and stores their URLs in the database.”
*/
@Service
@Slf4j

public class FileUploadService {

    private final Cloudinary cloudinary;
    private final AuthService authService;
    private final ResumeRepository resumeRepository;


    // FIXED: Constructor add kiya taaki Spring 'CloudinaryConfig' se bean yahan bhej sake
    public FileUploadService(Cloudinary cloudinary,AuthService authService,ResumeRepository resumeRepository) {
        this.cloudinary = cloudinary;
        this.authService=authService;
        this.resumeRepository=resumeRepository;
    }

    public Map<String, String> uploadSingleImage(MultipartFile file) throws IOException 
    {
        
        // Cloudinary documentation ke mutabiq file bytes bhej rahe hain
        // "resource_type", "image" batata hai ki hum sirf photo upload kar rahe hain
        @SuppressWarnings("unchecked") // Casting warning hatane ke liye
        //we can call this method inside our controller 
        Map<String, Object> imageUploadResult = cloudinary.uploader().upload(
            file.getBytes(), 
            ObjectUtils.asMap("resource_type", "auto") // 'auto' video/image dono handle kar leta hai
        );

        
        // Secure URL nikal kar Map mein return kar rahe hain
        String imageUrl = imageUploadResult.get("secure_url").toString();
        log.info("Upload result: {}", imageUploadResult);
        
        return Map.of("imageUrl", imageUrl);        
    }

	public Map<String, String> uploadResumeImages(Long resumeid, 
			                                      Object principal,
			                                      MultipartFile thumbnail,
			                                      MultipartFile profileImage) throws IOException
	{
		//Step 1: get the current profile 
		AuthResponse response = authService.getProfile(principal);
		
		//Step 2: get the existing resume
		Resume existingResume = resumeRepository.findByUserIdAndId(response.getId(), resumeid)
				  .orElseThrow(()-> new RuntimeException("Resume not found "));
		//Step 3:upload the resume images and set the resume
		Map<String,String> returnValue=new HashMap<>();
		Map<String,String> uploadResult;
		
		if(Objects.nonNull(thumbnail))
		{
        uploadResult = uploadSingleImage(thumbnail);
		existingResume.setThumbnailLink(uploadResult.get("imageUrl"));
		returnValue.put("thumbnailLink",uploadResult.get("imageUrl"));
		}
		
		if(Objects.nonNull(profileImage))
		{
		uploadResult = uploadSingleImage(profileImage);
		//if(existingResume.getProfileInfo()==null)
		if(Objects.isNull(existingResume.getProfileInfo())) 
		{
			existingResume.setProfileInfo(new Resume.ProfileInfo());
		}
		
		existingResume.getProfileInfo().setProfilePreviewUrl(uploadResult.get("imageUrl"));
		
		returnValue.put("profilePreviewUrl",uploadResult.get("imageUrl"));
		}
		
		//Step 4: update the details into database
		resumeRepository.save(existingResume);
		returnValue.put("message","Image upload successfully");
		
		//step 5 : return the result
		return returnValue;
	}
}

/*
 * Ye class kyu use ki gayi hai?

Images store karna (Cloud pe)
Local storage avoid karna
Resume ke liye:
Thumbnail image
Profile image
 */

/*
 * uploadSingleImage()
File aayi (MultipartFile)
Cloudinary ko bytes bheje
Upload hua
URL mila
Return kar diya

 uploadResumeImages()
Current user nikala (authService)
Resume DB se fetch kiya
Thumbnail upload kiya (optional)
Profile image upload ki
Resume object update kiya
DB me save kiya
Response return kiya
 */

/*
 * Q1: MultipartFile kya hota hai?
“It represents an uploaded file in Spring Boot.”

Q2: Cloudinary kyu use kiya?
“To store images on cloud instead of local storage.”

Q3: secure_url kya hota hai?
“It is the HTTPS URL of the uploaded image.”

Code Based
Q4: ObjectUtils.asMap kyu use kiya?
“To pass configuration options like resource type.”

Q5: resource_type = auto kyu use kiya?
“To allow both image and video uploads.”

Q6: Objects.nonNull kyu use kiya?
“To check if the file is not null before processing.”

Q7: DB save kyu last me kiya?
“To ensure all updates are applied before saving.”

 Advanced
Q8: Agar Cloudinary fail ho jaye to?
“We should handle exception and return proper error response.”

Q9: Local storage vs Cloud storage?
“Cloud storage is scalable and reliable, local is not.”

Q10: Security concern kya hai?
“File validation and size/type restrictions should be applied.”
 */


