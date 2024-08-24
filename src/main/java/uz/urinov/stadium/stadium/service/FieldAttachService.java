package uz.urinov.stadium.stadium.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.exp.AppBadException;
import uz.urinov.stadium.stadium.entity.FieldAttachEntity;
import uz.urinov.stadium.stadium.entity.FieldEntity;
import uz.urinov.stadium.stadium.entity.StadiumEntity;
import uz.urinov.stadium.stadium.repository.FieldAttachRepository;
import uz.urinov.stadium.stadium.repository.FieldRepository;
import uz.urinov.stadium.stadium.repository.StadiumRepository;
import uz.urinov.stadium.util.SecurityUtil;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FieldAttachService {
    @Autowired
    private ResourceBundleMessageSource rbms;
    private final FieldAttachRepository fieldAttachRepository;
    private final StadiumRepository stadiumRepository;
    private final FieldRepository fieldRepository;


    public void fieldAttachSave(List<String> attachList, FieldEntity field, StadiumEntity stadium, Language lang) {

        for (String attachId : attachList) {
            FieldAttachEntity entity = new FieldAttachEntity();
            entity.setFieldId(field.getId());
            entity.setAttachId(attachId);
            entity.setOwnerId(SecurityUtil.getProfileId());
            fieldAttachRepository.save(entity);
        }
        field.setVisible(true);
        fieldRepository.save(field);

        if (stadium.getVisible().equals(false)) {
            stadium.setVisible(true);
            stadiumRepository.save(stadium);
        }


    }

}
