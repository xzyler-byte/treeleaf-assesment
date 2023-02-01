package com.xzyler.microservices.securityservice.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class MultipartFileHandler {

    private final String documentPath = new StringBuilder().append(System.getProperty("user.dir")).append("/test").toString();

    public String handle(MultipartFile multipartFiles) throws Exception {
        String imagePath = new StringBuilder().append(documentPath).append("/userimages/").toString();
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            if (!multipartFiles.isEmpty()) {
                String imageName = new StringBuilder().append(UUID.randomUUID()).append(".jpg").toString();
                multipartFiles.transferTo(new File(new StringBuilder().append(imagePath).append(imageName).toString()));
                return new StringBuilder().append("api/resources/").append(imageName).toString();

            }else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String dynamicFile(MultipartFile multipartFiles) throws Exception {
        String finalPath = "userimages/";
        String imagePath = new StringBuilder().append(documentPath).append(finalPath).toString();
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String extension = FilenameUtils.getExtension(multipartFiles.getOriginalFilename());
        try {
            if (!multipartFiles.isEmpty()) {
                String imageName = new StringBuilder().append(UUID.randomUUID()).append(".").append(extension).toString();
                multipartFiles.transferTo(new File(new StringBuilder().append(imagePath).append(imageName).toString()));
                //return new StringBuilder().append("resources/").append(imageName).toString();
                return imageName;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    public String dynamicFile(MultipartFile multipartFiles,String finalPath) throws Exception {
        String imagePath = new StringBuilder().append(documentPath).append("/").append(finalPath).append("/").toString();
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String extension = FilenameUtils.getExtension(multipartFiles.getOriginalFilename());
        try {
            if (!multipartFiles.isEmpty()) {
                String imageName = new StringBuilder().append(UUID.randomUUID()).append(".").append(extension).toString();
                multipartFiles.transferTo(new File(new StringBuilder().append(imagePath).append(imageName).toString()));
                //return new StringBuilder().append("resources/").append(imageName).toString();
                return new StringBuilder().append(finalPath.split("/")[1]).append("/").append(imageName).toString();
            } else {
                throw  new Exception("File Empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }




    public Boolean fileMove(String filename) throws Exception {

        String imagePath = new StringBuilder().append(documentPath).append("/docs/").append(filename.split("/")[0]).toString();
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        String fileFrom =new StringBuilder().append(documentPath).append("/temp/").append(filename).toString();
        String fileTo = new StringBuilder().append(documentPath).append("/docs/").append(filename).toString();


        try {
            Path temp = Files.move(Paths.get(fileFrom), Paths.get(fileTo), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("!!File renamed and moved successfully!!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to move the file");
        }
    }


    public void fileMoveReverse(String filename) throws Exception {
        String imagePath = new StringBuilder().append(documentPath).append("/temp/").append(filename.split("/")[0]).toString();
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        String fileTo =new StringBuilder().append(documentPath).append("/temp/").append(filename).toString();
        String fileFrom = new StringBuilder().append(documentPath).append("/docs/").append(filename).toString();
        if(new File(fileFrom).exists())

            try {
                Path temp = Files.move(Paths.get(fileFrom), Paths.get(fileTo), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("!!File renamed and moved successfully!!");
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Failed to move the file");
            }
    }

    public void deleteFile(String path) throws Exception {
        String location =  new StringBuilder().append(documentPath).append("/").append(path).toString();

        File myFile = new File(location);
        if(myFile.exists())
            myFile.delete();
        else
            throw new Exception("No Such File Exists");


    }
}
