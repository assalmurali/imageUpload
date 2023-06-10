
package com.microservice.UploadPhoto.controller;



import com.microservice.UploadPhoto.message.ResponseMessage;
import com.microservice.UploadPhoto.message.ResponsePhotoUpload;
import com.microservice.UploadPhoto.model.PhotoUpload;
import com.microservice.UploadPhoto.service.PhotoUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:8080")
public class PhotoUploadController {
    @Autowired
    private PhotoUploadService photoUploadService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		LOGGER.info("Starting Photo Upload process");
        String message = "";
        try {
            photoUploadService.store(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			LOGGER.debug("Could not upload the file"+ file.getOriginalFilename() + "!");
			LOGGER.debug("Could not upload the file"+ file.getOriginalFilename() + "!");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<ResponsePhotoUpload>> getListFiles() {
		LOGGER.info("Starting view the existing list of  process");
        List<ResponsePhotoUpload> files = photoUploadService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponsePhotoUpload(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    public ResponseEntity<byte[]> getFile(@PathVariable String id){
        PhotoUpload photoUpload = photoUploadService.getFile(id);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\"" + photoUpload.getName() + "\"")
                .body(photoUpload.getData());
    }
}

