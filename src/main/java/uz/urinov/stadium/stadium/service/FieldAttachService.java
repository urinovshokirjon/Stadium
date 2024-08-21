package uz.urinov.stadium.stadium.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.exp.AppBadException;
import uz.urinov.stadium.stadium.entity.FieldAttachEntity;
import uz.urinov.stadium.stadium.entity.StadiumAttachEntity;
import uz.urinov.stadium.stadium.repository.FieldAttachRepository;
import uz.urinov.stadium.util.SecurityUtil;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FieldAttachService {
    @Autowired
    private ResourceBundleMessageSource rbms;
    private final FieldAttachRepository fieldAttachRepository;


    public void fieldAttachSave(List<String> attachList, Integer fieldId, Language lang) {
        boolean empty = attachList.isEmpty();
        if (empty) {
            String message = rbms.getMessage("image.not.available", null, new Locale(lang.name()));
            throw new AppBadException(message);
        }
        for (String attach : attachList) {
            FieldAttachEntity entity=new FieldAttachEntity();
            entity.setFieldId(fieldId);
            entity.setAttachId(attach);
            entity.setOwnerId(SecurityUtil.getProfileId());
            fieldAttachRepository.save(entity);
        }

    }

}
