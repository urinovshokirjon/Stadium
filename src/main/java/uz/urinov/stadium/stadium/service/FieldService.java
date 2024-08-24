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
import uz.urinov.stadium.stadium.entity.FieldEntity;
import uz.urinov.stadium.stadium.entity.FieldPriceEntity;
import uz.urinov.stadium.stadium.entity.FieldTypeEntity;
import uz.urinov.stadium.stadium.entity.StadiumEntity;
import uz.urinov.stadium.stadium.repository.FieldAttachRepository;
import uz.urinov.stadium.stadium.repository.FieldPriceRepository;
import uz.urinov.stadium.stadium.repository.FieldRepository;
import uz.urinov.stadium.stadium.repository.StadiumRepository;
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
    private final StadiumRepository stadiumRepository;
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
        StadiumEntity stadium = stadiumService.getStadiumEntityById(dto.getStadiumId(), lang);

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
        StadiumEntity stadium = stadiumService.getStadiumEntityById(dto.getStadiumId(), lang);

        FieldEntity entity = getFieldById(fieldId, lang);
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
        FieldEntity entity = getFieldById(fieldId, lang);
        entity.setVisible(false);
        log.warn("Delete profile id = {}  phone = {} ", SecurityUtil.getProfile().getId(), SecurityUtil.getProfile().getPhone());
        String message = rbms.getMessage("changed", null, new Locale(lang.name()));
        return new Result("Field " + message, true);
    }

    // get id Field
    public FieldResponseDto getIdField(int fieldId, Language lang) {

        return null;
    }

    // Get by field List
    public List<FieldResponseDto> getFieldListStadium(int stadiumId, Language lang) {
        stadiumService.getStadiumById(stadiumId, lang);
        List<FieldResponseDto> responseDtoList = new ArrayList<>();
        for (FieldEntity entity : fieldRepository.findAllByStadiumIdAndVisibleTrue(stadiumId)) {
            responseDtoList.add(getListField(entity, lang));
        }
        return responseDtoList;
    }

    // Field rating
    public Result ratingField(int fieldId, Integer rating, Language lang) {
        getById(fieldId, lang);
        FieldPriceEntity entity=new FieldPriceEntity();
        entity.setFieldId(fieldId);
        entity.setAverageRating(rating);
        entity.setOwnerId(SecurityUtil.getProfileId());
        fieldPriceRepository.save(entity);
        log.warn("Rating profile id = {}  phone = {} ", SecurityUtil.getProfile().getId(), SecurityUtil.getProfile().getPhone());
        String message = rbms.getMessage("created", null, new Locale(lang.name()));
        return new Result("Price " + message, true);
    }


    public FieldResponseDto getListField(FieldEntity entity, Language lang) {
        FieldResponseDto dto = new FieldResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setRating(fieldPriceRepository.ratingByFieldId(entity.getId()));
        dto.setStadiumId(entity.getStadiumId());
        dto.setFieldTypeResponseDto(fieldTypeService.toFieldTypeLang(entity.getFieldType(), lang));
        dto.setPhotolist(fieldAttachRepository.findAttachIds(entity.getId()));
        return dto;
    }


    public FieldEntity getFieldById(int id, Language lang) {
        return fieldRepository.findByIdAndOwnerId(id, SecurityUtil.getProfileId()).orElseThrow(() -> {
            String message = rbms.getMessage("item.not.found", null, new Locale(lang.name()));
            throw new AppBadException(message);
        });
    }

    public FieldEntity getById(int id, Language lang) {
        return fieldRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> {
            String message = rbms.getMessage("item.not.found", null, new Locale(lang.name()));
            throw new AppBadException(message);
        });
    }


}
