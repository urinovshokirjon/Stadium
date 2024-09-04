package uz.urinov.stadium.stadium.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.Profile.enums.ProfileRole;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.district.entity.DistrictEntity;
import uz.urinov.stadium.district.repository.DistrictRepository;
import uz.urinov.stadium.district.service.DistrictService;
import uz.urinov.stadium.exp.AppBadException;
import uz.urinov.stadium.region.service.RegionService;
import uz.urinov.stadium.stadium.dto.StadiumCreateDto;
import uz.urinov.stadium.stadium.dto.StadiumResponseDto;
import uz.urinov.stadium.stadium.dto.StadiumResponseMiniDto;
import uz.urinov.stadium.stadium.entity.StadiumEntity;
import uz.urinov.stadium.stadium.enums.Status;
import uz.urinov.stadium.stadium.repository.FieldRepository;
import uz.urinov.stadium.stadium.repository.StadiumAttachRepository;
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
    private final StadiumAttachRepository stadiumAttachRepository;
    private final RegionService regionService;
    private final DistrictRepository districtRepository;

    // 1. Create stadium (ADMIN,OWNER)
    public Result createStadium(StadiumCreateDto dto, Language lang) {

        boolean empty = dto.getPhotoList().isEmpty();
        if (empty) {
            String message = rbms.getMessage("image.not.available", null, new Locale(lang.name()));
            throw new AppBadException(message);
        }

        districtService.getDistrictEntityById(dto.getDistrictId(), lang);
        StadiumEntity stadium = new StadiumEntity();
        stadium.setLat(dto.getLat());
        stadium.setLon(dto.getLon());
        stadium.setDescription(dto.getDescription());
        stadium.setDistrictId(dto.getDistrictId());
        stadium.setProfileId(SecurityUtil.getProfileId());

        StadiumEntity saveStadium = stadiumRepository.save(stadium);

        stadiumAttachService.stadiumAttachSave(dto.getPhotoList(), saveStadium.getId(), lang);

        String message = rbms.getMessage("created", null, new Locale(lang.name()));
        return new Result("Stadium " + message, true);
    }

    // 2. Update stadium (ADMIN,OWNER)
    public Result updateStadium(int id, StadiumCreateDto dto, Language lang) {
        StadiumEntity stadium = getStadiumOwnerById(id, lang);
        stadium.setLat(dto.getLat());
        stadium.setLon(dto.getLon());
        stadium.setDescription(dto.getDescription());
        stadium.setDistrictId(dto.getDistrictId());
        stadium.setProfileId(SecurityUtil.getProfileId());
        stadium.setStatus(Status.INACTIVE);

        StadiumEntity saveStadium = stadiumRepository.save(stadium);
        stadiumAttachService.stadiumAttachSave(dto.getPhotoList(), saveStadium.getId(), lang);

        String message = rbms.getMessage("changed", null, new Locale(lang.name()));
        return new Result("Stadium " + message, true);
    }

    // 3. Delete stadium (ADMIN,OWNER)
    public Result deleteStadium(int stadiumId, Language lang) {

        StadiumEntity entity =null;
        if (SecurityUtil.getProfile().getRole().equals(ProfileRole.ROLE_OWNER)){
             entity = getStadiumOwnerById(stadiumId, lang);
        }
        if (SecurityUtil.getProfile().getRole().equals(ProfileRole.ROLE_ADMIN)){
             entity = getStadiumById(stadiumId, lang);
        }

        Integer effectiveRow = fieldRepository.updateFirstByVisible(entity.getId());
        entity.setVisible(false);
        stadiumRepository.save(entity);
        String message = rbms.getMessage("deleted", null, new Locale(lang.name()));
        return new Result("Stadium with " + effectiveRow + " field " + message, true);

    }

    // 4. Region Stadium List stadium
    public List<StadiumResponseDto> regionStadiumList(int regionId, Language lang, int page, int size, Double lat, Double lon) {
        // 1. Region ID ga mos keladigan barcha tumanlarni topamiz
        regionService.getRegionEntityById(regionId, lang);

        Pageable pageable = PageRequest.of(page, size);

        List<DistrictEntity> districts = districtRepository.findByRegionId(regionId);

        List<Integer> districtIds = districts.stream().map(DistrictEntity::getId).toList();

        List<StadiumEntity> entityList = stadiumRepository.findClosestStadiumsInDistricts(districtIds, lat, lon, pageable);
        return entityList.stream().map(this::stadiumDetails).toList();
    }

    // 5. Closest Stadium List stadium (eng yaqin)
    public List<StadiumResponseDto> closestStadiumList(Language lang, int page, int size, Double lat, Double lon) {

        Pageable pageable = PageRequest.of(page, size);
        List<StadiumEntity> stadiumEntityList = stadiumRepository.findClosestStadiums(lat, lon, pageable);
        return stadiumEntityList.stream().map(this::stadiumDetails).toList();
    }


    public StadiumResponseDto stadiumDetails(StadiumEntity entity) {
        StadiumResponseDto dto = new StadiumResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setDescription(entity.getDescription());
        dto.setLat(entity.getLat());
        dto.setLon(entity.getLon());
        dto.setPhotolist(stadiumAttachRepository.findAttachIds(entity.getId()));

        return dto;
    }

    public StadiumResponseMiniDto stadiumDetailsMini(StadiumEntity entity) {
        StadiumResponseMiniDto dto = new StadiumResponseMiniDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
//        dto.setStatus(entity.getStatus());
//        dto.setDescription(entity.getDescription());
        dto.setLat(entity.getLat());
        dto.setLon(entity.getLon());
//        dto.setPhotolist(stadiumAttachRepository.findAttachIds(entity.getId()));
        return dto;
    }

    public StadiumEntity getStadiumOwnerById(int id, Language lang) {
        return stadiumRepository.findByIdAndProfileIdAndVisibleTrue(id, SecurityUtil.getProfileId()).orElseThrow(() -> {
            String message = rbms.getMessage("item.not.found", null, new Locale(lang.name()));
            throw new AppBadException(message);
        });
    }

    public StadiumEntity getStadiumById(int id, Language lang) {
        return stadiumRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> {
            String message = rbms.getMessage("item.not.found", null, new Locale(lang.name()));
            throw new AppBadException(message);
        });
    }



}
