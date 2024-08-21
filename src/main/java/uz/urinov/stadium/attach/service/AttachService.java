package uz.urinov.stadium.attach.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.urinov.stadium.attach.dto.AttachDTO;
import uz.urinov.stadium.attach.entity.AttachEntity;
import uz.urinov.stadium.attach.repository.AttachRepository;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.exp.AppBadException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachService {

    @Value("${attach.upload.url}")
    private  String attachUrl;

    @Value("${server.url}")
    private String serverUrl;

    private final AttachRepository attachRepository;
    private final ResourceBundleMessageSource rbms;

    // Attachni saqlash;
    public AttachDTO  saveAttach(MultipartFile file) {
        try {
            String pathFolder = getYmDString();

            File folder = new File(attachUrl + pathFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String key = UUID.randomUUID().toString();
            String extension = getExtension(file.getOriginalFilename());
            // save to system
            byte[] bytes = file.getBytes();

//            Path path = Paths.get(attachUrl + pathFolder + "/" + key + "." + extension);
            Path path = Paths.get(folder.getAbsolutePath() + "/" + key + "." + extension);

            Files.write(path, bytes);
            // save to db
            AttachEntity entity = new AttachEntity();
            entity.setId(key + "." + extension); // dasdasd-dasdasda-asdasda-asdasd.jpg
            entity.setPath(pathFolder);    // 2024/06/08
            entity.setOriginalName(file.getOriginalFilename());
            entity.setSize(file.getSize());
            entity.setExtension(extension);
            attachRepository.save(entity);

            return toDTO(entity);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // imag ni ochish
    public byte[] openGeneral(String attachId,Language lang) {

        byte[] data;
        try {
            AttachEntity entity = get(attachId,lang);
            String path = entity.getPath() + "/" +attachId;
            Path file = Paths.get(attachUrl + path);
            data = Files.readAllBytes(file);
            return data;

        }catch (IOException e){
            e.printStackTrace();
        }
        return new byte[0];

    }

    // imag ni  yuklab olish
    public Resource download(String attachId,Language lang) {
        try {
            AttachEntity entity = get(attachId,lang);
            String path = entity.getPath() + "/" +attachId;
            Path file = Paths.get(attachUrl+path);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()){
                return resource;
            }else {
                String response=rbms.getMessage("not.read.the.file",null, new Locale(lang.name()));
                throw new RuntimeException(response);
            }

        }catch (MalformedURLException e){
            throw new RuntimeException("Error: "+e.getMessage());
        }
    }



    // Kunlik ochiladigan papkalar
    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day;  // 2024/06/08
    }

    // imegni tipini oladi // mp3/jpg/npg/mp4....
    public String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setCreatedData(entity.getCreatedData());
        dto.setExtension(entity.getExtension());
        dto.setSize(entity.getSize());
        dto.setOriginalName(entity.getOriginalName());
        dto.setUrl(serverUrl+"/attach/open_general/"+entity.getId());
        return dto;

    }

    public AttachEntity get(String id, Language lang) {
        return attachRepository.findById(id).orElseThrow(() -> {
            String response=rbms.getMessage("item.not.found",null, new Locale(lang.name()));
            throw new AppBadException(response);
        });
    }


}
