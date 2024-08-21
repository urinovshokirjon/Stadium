package uz.urinov.stadium.stadium.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.attach.entity.AttachEntity;
import uz.urinov.stadium.attach.repository.AttachRepository;
import uz.urinov.stadium.attach.service.AttachService;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.stadium.dto.FieldCreateDto;
import uz.urinov.stadium.stadium.entity.FieldEntity;
import uz.urinov.stadium.stadium.entity.FieldTypeEntity;
import uz.urinov.stadium.stadium.entity.StadiumEntity;
import uz.urinov.stadium.stadium.repository.FieldRepository;
import uz.urinov.stadium.stadium.repository.StadiumRepository;
import uz.urinov.stadium.util.Result;
import uz.urinov.stadium.util.SecurityUtil;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FieldService {
    @Autowired
    private ResourceBundleMessageSource rbms;
    private final FieldRepository fieldRepository;
    private final FieldTypeService fieldTypeService;
    private final StadiumService stadiumService;
    private final StadiumRepository stadiumRepository;
    private final FieldAttachService fieldAttachService;

    // Field created
    public Result createField(FieldCreateDto dto, Language lang) {

        FieldTypeEntity fieldType = fieldTypeService.getFieldTypeId(dto.getFieldTypeId(), lang);
        StadiumEntity stadium = stadiumService.getStadiumEntityById(dto.getStadiumId(), lang);

        if (stadium.getVisible().equals(false)) {
            stadium.setVisible(true);
            stadiumRepository.save(stadium);
        }

        FieldEntity entity = new FieldEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setFieldTypeId(dto.getFieldTypeId());
        entity.setStadiumId(dto.getStadiumId());
        entity.setOwnerId(SecurityUtil.getProfileId());

        FieldEntity saveField = fieldRepository.save(entity);
        fieldAttachService.fieldAttachSave(dto.getPhotoList(),saveField.getId(),lang);

        String message = rbms.getMessage("created", null, new Locale(lang.name()));
        return new Result("Field " + message,true);
    }
}
