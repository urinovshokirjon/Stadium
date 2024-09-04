package uz.urinov.stadium.stadium.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.exp.AppBadException;
import uz.urinov.stadium.stadium.dto.FieldCreateDto;
import uz.urinov.stadium.stadium.dto.FieldResponseDto;
import uz.urinov.stadium.stadium.dto.FieldResponseMiniDto;
import uz.urinov.stadium.stadium.entity.FieldEntity;
import uz.urinov.stadium.stadium.entity.StadiumEntity;
import uz.urinov.stadium.stadium.enums.Status;
import uz.urinov.stadium.stadium.repository.FieldAttachRepository;
import uz.urinov.stadium.stadium.repository.FieldPriceRepository;
import uz.urinov.stadium.stadium.repository.FieldRepository;
import uz.urinov.stadium.util.Result;
import uz.urinov.stadium.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class FieldService {
    @Autowired
    private ResourceBundleMessageSource rbms;
    private final FieldRepository fieldRepository;
    private final FieldTypeService fieldTypeService;
    private final StadiumService stadiumService;
    private final FieldAttachService fieldAttachService;
    private final FieldAttachRepository fieldAttachRepository;
    private final FieldPriceRepository fieldPriceRepository;

    // Field created
    public Result createField(FieldCreateDto dto, Language lang) {

        boolean empty = dto.getPhotoList().isEmpty();
        if (empty) {
            String message = rbms.getMessage("image.not.available", null, new Locale(lang.name()));
            throw new AppBadException(message);
        }
        fieldTypeService.getFieldTypeId(dto.getFieldTypeId(), lang);
        StadiumEntity stadium = stadiumService.getStadiumOwnerById(dto.getStadiumId(), lang);

        FieldEntity entity = new FieldEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setFieldTypeId(dto.getFieldTypeId());
        entity.setStadiumId(dto.getStadiumId());
        entity.setOwnerId(SecurityUtil.getProfileId());

        FieldEntity saveField = fieldRepository.save(entity);
        fieldAttachService.fieldAttachSave(dto.getPhotoList(), saveField, stadium, lang);

        log.info("Create profile id = {}  phone = {} ", SecurityUtil.getProfile().getId(), SecurityUtil.getProfile().getPhone());

        String message = rbms.getMessage("created", null, new Locale(lang.name()));
        return new Result("Field " + message, true);
    }

    // Field update
    public Result updateField(int fieldId, FieldCreateDto dto, Language lang) {

        boolean empty = dto.getPhotoList().isEmpty();
        if (empty) {
            String message = rbms.getMessage("image.not.available", null, new Locale(lang.name()));
            throw new AppBadException(message);
        }

        fieldTypeService.getFieldTypeId(dto.getFieldTypeId(), lang);
        StadiumEntity stadium = stadiumService.getStadiumOwnerById(dto.getStadiumId(), lang);

        FieldEntity entity = getFieldOwnerById(fieldId, lang);
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setFieldTypeId(dto.getFieldTypeId());
        entity.setStadiumId(dto.getStadiumId());
        entity.setOwnerId(SecurityUtil.getProfileId());
//        entity.setVisible(dto.get);
        FieldEntity saveField = fieldRepository.save(entity);

        fieldAttachService.fieldAttachSave(dto.getPhotoList(), saveField, stadium, lang);

        log.info(" Update profile id = {}  phone = {} ", SecurityUtil.getProfile().getId(), SecurityUtil.getProfile().getPhone());
        String message = rbms.getMessage("changed", null, new Locale(lang.name()));
        return new Result("Field " + message, true);
    }

    // Field delete
    public Result deleteField(int fieldId, Language lang) {
        FieldEntity entity = getFieldOwnerById(fieldId, lang);
        entity.setVisible(false);
        fieldRepository.save(entity);
        log.warn("Delete profile id = {}  phone = {} ", SecurityUtil.getProfile().getId(), SecurityUtil.getProfile().getPhone());
        String message = rbms.getMessage("changed", null, new Locale(lang.name()));
        return new Result("Field " + message, true);
    }

    // get id Field
    public FieldResponseDto getIdField(int fieldId, Language lang) {
        FieldEntity entity = getById(fieldId, lang);
        return fieldDetails(entity, lang);
    }

    // Get by field List
    public List<FieldResponseDto> getFieldListStadium(int stadiumId, Language lang) {
        stadiumService.getStadiumById(stadiumId, lang);
        List<FieldResponseDto> responseDtoList = new ArrayList<>();
        for (FieldEntity entity : fieldRepository.findAllByStadiumIdAndVisibleTrueAndStatus(stadiumId, Status.ACTIVE)) {
            responseDtoList.add(fieldDetails(entity, lang));
        }
        return responseDtoList;
    }

    // Field rating
    public Result ratingField(int fieldId, Integer rating, Language lang) {
        FieldEntity entity = getById(fieldId, lang);
        int count = entity.getRatingCount()+1;
        Double averageRating = (entity.getAverageRating()*(count-1)+rating)/count;
        entity.setRatingCount(count);
        entity.setAverageRating(averageRating);
        fieldRepository.save(entity);
        log.warn("Rating profile id = {}  phone = {} ", SecurityUtil.getProfile().getId(), SecurityUtil.getProfile().getPhone());
        String message = rbms.getMessage("created", null, new Locale(lang.name()));
        return new Result("Price " + message, true);
    }


    public FieldResponseDto fieldDetails(FieldEntity entity, Language lang) {
        FieldResponseDto dto = new FieldResponseDto();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setRating(entity.getAverageRating());
        dto.setStadiumId(entity.getStadiumId());
        dto.setFieldTypeResponseDto(fieldTypeService.toFieldTypeLang(entity.getFieldType(), lang));
        dto.setPhotolist(fieldAttachRepository.findAttachIds(entity.getId()));
        return dto;
    }

    public FieldResponseMiniDto fieldDetailsMini(FieldEntity entity, Language lang) {
        FieldResponseMiniDto dto = new FieldResponseMiniDto();
        dto.setStadiumId(entity.getStadiumId());
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setRating(entity.getAverageRating());
        dto.setFieldTypeResponseDto(fieldTypeService.toFieldTypeLang(entity.getFieldType(), lang));
        dto.setPhotolist(fieldAttachRepository.findAttachIds(entity.getId()));
        return dto;
    }


    public FieldEntity getFieldOwnerById(int id, Language lang) {
        return fieldRepository.findByIdAndOwnerId(id, SecurityUtil.getProfileId()).orElseThrow(() -> {
            String message = rbms.getMessage("item.not.found", null, new Locale(lang.name()));
            throw new AppBadException(message);
        });
    }

    public FieldEntity getById(int id, Language lang) {
        return fieldRepository.findByIdAndVisibleTrueAndStatus(id,Status.ACTIVE).orElseThrow(() -> {
            String message = rbms.getMessage("item.not.found", null, new Locale(lang.name()));
            throw new AppBadException(message);
        });
    }


}
