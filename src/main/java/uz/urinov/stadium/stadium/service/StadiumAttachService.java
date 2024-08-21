package uz.urinov.stadium.stadium.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.exp.AppBadException;
import uz.urinov.stadium.stadium.entity.StadiumAttachEntity;
import uz.urinov.stadium.stadium.repository.StadiumAttachRepository;
import uz.urinov.stadium.util.SecurityUtil;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class StadiumAttachService {
    @Autowired
    private ResourceBundleMessageSource rbms;
    private final StadiumAttachRepository stadiumAttachRepository;

    public void stadiumAttachSave(List<String> attachList, Integer stadiumId, Language lang) {
        boolean empty = attachList.isEmpty();
        if (empty) {
            String message = rbms.getMessage("image.not.available", null, new Locale(lang.name()));
            throw new AppBadException(message);
        }
        for (String attach : attachList) {
            StadiumAttachEntity entity=new StadiumAttachEntity();
            entity.setStadiumId(stadiumId);
            entity.setAttachId(attach);
            entity.setOwnerId(SecurityUtil.getProfileId());
            stadiumAttachRepository.save(entity);
        }

    }

}
