package uz.urinov.stadium.stadium.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.district.service.DistrictService;
import uz.urinov.stadium.exp.AppBadException;
import uz.urinov.stadium.stadium.dto.StadiumCreateDto;
import uz.urinov.stadium.stadium.dto.StadiumResponseDto;
import uz.urinov.stadium.stadium.entity.StadiumEntity;
import uz.urinov.stadium.stadium.repository.FieldRepository;
import uz.urinov.stadium.stadium.repository.StadiumRepository;
import uz.urinov.stadium.util.Result;
import uz.urinov.stadium.util.SecurityUtil;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class StadiumService {
    @Autowired
    private ResourceBundleMessageSource rbms;
    private final StadiumRepository stadiumRepository;
    private final DistrictService districtService;
    private final FieldRepository fieldRepository;
    private final StadiumAttachService stadiumAttachService;

    // 1. Create stadium (ADMIN,OWNER)
    public Result createStadium(StadiumCreateDto dto, Language lang) {
        districtService.getDistrictEntityById(dto.getDistrictId(), lang);
        StadiumEntity stadium = new StadiumEntity();
        stadium.setLat(dto.getLat());
        stadium.setLon(dto.getLon());
        stadium.setDescription(dto.getDescription());
        stadium.setDistrictId(dto.getDistrictId());
        stadium.setProfileId(SecurityUtil.getProfileId());

        StadiumEntity saveStadium = stadiumRepository.save(stadium);

        stadiumAttachService.stadiumAttachSave(dto.getPhotoList(),saveStadium.getId(),lang);

        String message = rbms.getMessage("created", null, new Locale(lang.name()));
        return new Result("Stadium " + message, true);
    }

    // 2. Update stadium (ADMIN,OWNER)
    public Result updateStadium(int id, StadiumCreateDto dto, Language lang) {
        StadiumEntity stadium = getStadiumEntityById(id, lang);
        stadium.setLat(dto.getLat());
        stadium.setLon(dto.getLon());
        stadium.setDescription(dto.getDescription());
        stadium.setDistrictId(dto.getDistrictId());
        stadium.setProfileId(SecurityUtil.getProfileId());

        StadiumEntity saveStadium = stadiumRepository.save(stadium);
        stadiumAttachService.stadiumAttachSave(dto.getPhotoList(),saveStadium.getId(),lang);

        String message = rbms.getMessage("changed", null, new Locale(lang.name()));
        return new Result("Stadium " + message, true);
    }

    // 3. Delete stadium (ADMIN,OWNER)
    public Result deleteStadium(int stadiumId, Language lang) {
        StadiumEntity entity = getStadiumEntityById(stadiumId, lang);
        Integer effectiveRow = fieldRepository.updateFirstByVisible(SecurityUtil.getProfileId());
        entity.setVisible(false);
        stadiumRepository.save(entity);
        String message = rbms.getMessage("deleted", null, new Locale(lang.name()));
        return new Result("Stadium with "+effectiveRow+ " field "+  message, true);
    }

    // 4. Delete stadium (ADMIN,OWNER)
    public List<StadiumResponseDto> regionStadiumList(int regionId, Language lang) {
        return null;
    }


    public StadiumEntity getStadiumEntityById(int id, Language lang) {
        return stadiumRepository.findByIdAndProfileId(id,SecurityUtil.getProfileId()).orElseThrow(() -> {
            String message = rbms.getMessage("item.not.found", null, new Locale(lang.name()));
            throw new AppBadException(message);
        });
    }


}
