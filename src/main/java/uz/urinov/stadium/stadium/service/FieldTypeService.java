package uz.urinov.stadium.stadium.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.exp.AppBadException;
import uz.urinov.stadium.stadium.dto.FieldTypeCreateDto;
import uz.urinov.stadium.stadium.dto.FieldTypeResponseDto;
import uz.urinov.stadium.stadium.entity.FieldTypeEntity;
import uz.urinov.stadium.stadium.repository.FieldTypeRepository;
import uz.urinov.stadium.util.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FieldTypeService {
    private final FieldTypeRepository fieldTypeRepository;
    @Autowired
    private ResourceBundleMessageSource rbms;

    // 1. Create fieldType (ADMIN)
    public Result createFieldType(FieldTypeCreateDto dto, Language lang) {
        FieldTypeEntity entity = new FieldTypeEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setNameKr(dto.getNameKr());
        fieldTypeRepository.save(entity);
        String message = rbms.getMessage("created", null, new Locale(lang.name()));
        return new Result("FieldType " + message, true);
    }

    // 2. Update fieldType (ADMIN)
    public Result updateFieldType(int id, FieldTypeCreateDto dto, Language lang) {
        FieldTypeEntity entity = getFieldTypeId(id, lang);
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setNameKr(dto.getNameKr());
        fieldTypeRepository.save(entity);
        String message = rbms.getMessage("changed", null, new Locale(lang.name()));
        return new Result("FieldType " + message, true);
    }

    // 3. FieldType list (ADMIN)
    public List<FieldTypeResponseDto> getFieldTypeList() {

        List<FieldTypeResponseDto> dtos = new ArrayList<>();

        for (FieldTypeEntity entity : fieldTypeRepository.findAllByVisibleTrueOrderByOrderNumber()) {
            dtos.add(toDTO(entity));
        }
        return dtos;
    }

    // 4. Delete fieldType (ADMIN)
    public Result deleteFieldType(int id,Language lang) {
        FieldTypeEntity entity = getFieldTypeId(id, lang);
        entity.setVisible(false);
        fieldTypeRepository.save(entity);
        String message = rbms.getMessage("deleted", null, new Locale(lang.name()));
        return new Result("FieldType " + message, true);
    }

    // 5. FieldType By Lang
    public List<FieldTypeResponseDto> getFieldTypeLang(Language lang) {
        List<FieldTypeResponseDto> dtos = new ArrayList<>();
        for (FieldTypeEntity entity : fieldTypeRepository.findAllByVisibleTrueOrderByOrderNumber()) {
            FieldTypeResponseDto dto = new FieldTypeResponseDto();
            dto.setId(entity.getId());
            dto.setOrderNumber(entity.getOrderNumber());

            switch (lang){
                case UZ->dto.setName(entity.getNameUz());
                case EN->dto.setName(entity.getNameEn());
                case RU->dto.setName(entity.getNameRu());
                case KR->dto.setName(entity.getNameKr());
            }
            dtos.add(dto);
        }
        return dtos;
    }


    public FieldTypeResponseDto toDTO(FieldTypeEntity entity){
        FieldTypeResponseDto dto = new FieldTypeResponseDto();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setNameKr(entity.getNameKr());
        dto.setCreateDate(entity.getCreateDate());
        return dto;
    }



    public FieldTypeEntity getFieldTypeId(int id, Language lang) {
        return fieldTypeRepository.findByIdAndVisibleTrue(id).orElseThrow(()->{
            String message = rbms.getMessage("item.not.found", null, new Locale(lang.name()));
            throw new AppBadException(message);
        });
    }


}
