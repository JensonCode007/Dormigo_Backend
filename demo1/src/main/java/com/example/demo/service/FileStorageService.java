//package com.example.demo.service;
//
//
//import com.example.demo.Repository.ProductImageRepository;
//import com.example.demo.config.FileStorageConfig;
//import com.example.demo.exception.FileStorageException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.parameters.P;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.Arrays;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class FileStorageService {
//
//
//
//    private final ProductImageRepository productImageRepository;
//    private final FileStorageConfig fileStorageConfig;
//    private final Path fileStorageLocation;
//
//
//    /*Intialize the storage Directory*/
//    public void init(){
//
//        try{
//
//            Path path = Paths.get(fileStorageConfig.getDirectory());
//            if (!Files.exists(path)) {
//                Files.createDirectories(path);
//                log.info("Directory {} has been created", fileStorageConfig.getDirectory());
//            }
//        }
//        catch(IOException e){
//            throw new FileStorageException("Could not create Upload Directory", e);
//        }
//
//    }
//
//    public String storeFile(MultipartFile file){
//
//        validateFile(file);
//        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
//        String extension = getFileExtension(originalFilename);
//        String newFilename = UUID.randomUUID().toString() + "." + extension;
//
//
//        try{
//            init();
//            Path path = Paths.get(fileStorageConfig.getDirectory());
//            Path targetLocation = path.resolve(newFilename);
//
//            /*Copy the contents of the file to our machine*/
//
//            Files.copy(path, targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            return newFilename;
//        }
//        catch(IOException e){
//            throw new FileStorageException("Could not store file " + newFilename, e);
//        }
//    }
//    public String generateFileUrl(String filename){
//
//        return ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("api/files/")
//                .path(filename)
//                .toUriString();
//    }
//    public void DeleteFile(String fileName){
//        try{
//
//            Path path = Paths.get(fileStorageConfig.getDirectory()).resolve(fileName);
//            Files.deleteIfExists(path);
//            log.info("File {} has been deleted", fileName);
//
//        }
//        catch (IOException e){
//            log.error("Could not delete file: {}", fileName, e);
//        }
//    }
//
//
//
//
//    public void validateFile(MultipartFile file){
//        if(file.isEmpty()){
//            throw new FileStorageException("File is Empty");
//        }
//
//        if(file.getSize() > fileStorageConfig.getMaxSize()){
//            throw new FileStorageException(
//                    String.format("File Size Exceeds Maximum Size: %d", fileStorageConfig.getMaxSize())
//            );
//        }
//
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        String extension = getFileExtension(fileName);
//
//        boolean isAllowed = Arrays.asList(fileStorageConfig.getAllowedExtensions())
//                .contains(extension.toLowerCase());
//
//        if(!isAllowed){
//            throw new FileStorageException(
//                    String.format("This file extension is not allowed %s. Allowed Extensions : %s", extension,
//                            Arrays.toString(fileStorageConfig.getAllowedExtensions()))
//            );
//        }
//
//        String contentType = file.getContentType();
//        if(contentType == null || !contentType.contains("image/")){
//            throw new FileStorageException("File content type is not supported");
//        }
//
//
//    }
//    public String getFileExtension(String fileName){
//        if(fileName==null || !fileName.contains(".")){
//            throw new FileStorageException("Invalid File Name : " + fileName);
//
//        }
//        return fileName.substring(fileName.lastIndexOf(".")+1);
//    }
//}
