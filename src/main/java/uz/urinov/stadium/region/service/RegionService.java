package uz.urinov.stadium.region.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.exp.AppBadException;
import uz.urinov.stadium.region.dto.RegionCreateDTO;
import uz.urinov.stadium.region.dto.RegionResponseDTO;
import uz.urinov.stadium.region.entity.RegionEntity;
import uz.urinov.stadium.region.mapper.RegionMapper;
import uz.urinov.stadium.region.repository.RegionRepository;
import uz.urinov.stadium.util.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class RegionService {

    private final RegionRepository regionRepository;
    @Autowired
    private ResourceBundleMessageSource rbms;

    // 1. Region create (ADMIN)
    public RegionResponseDTO createRegion(RegionCreateDTO createDTO) {
        RegionEntity entity = new RegionEntity();
        entity.setOrderNumber(createDTO.getOrderNumber());
        entity.setNameUz(createDTO.getNameUz());
        entity.setNameRu(createDTO.getNameRu());
        entity.setNameEn(createDTO.getNameEn());

        regionRepository.save(entity);
        return toDTO(entity);
    }

    // 2. Region update (ADMIN)
    public Result updateRegion(RegionCreateDTO regionDto, int id,Language lang) {
      RegionEntity regionEntity=getRegionEntityById(id,lang);
      regionEntity.setOrderNumber(regionDto.getOrderNumber());
      regionEntity.setNameUz(regionDto.getNameUz());
      regionEntity.setNameRu(regionDto.getNameRu());
      regionEntity.setNameEn(regionDto.getNameEn());
      regionRepository.save(regionEntity);
        String message=rbms.getMessage("changed",null, new Locale(lang.name()));
      return new Result("Region"+message,true);
    }

    // 3. Region list (ADMIN)
    public List<RegionResponseDTO> getRegionList() {

        List<RegionResponseDTO> regionDtoList = new ArrayList<>();

        for (RegionEntity regionEntity : regionRepository.findAll()) {
            regionDtoList.add(toDTO(regionEntity));
        }
        return regionDtoList;
    }

    //4. Region delete (ADMIN)
    public Result deleteRegion(int id, Language lang) {
        RegionEntity regionEntity = getRegionEntityById(id,lang);
        regionRepository.delete(regionEntity);
        String message=rbms.getMessage("deleted",null, new Locale(lang.name()));
        return new Result("Region"+message,true);
    }

    // 5. Region By Lang
    public List<RegionResponseDTO> getRegionByLang(LanguageEnum lang) {

        List<RegionResponseDTO> regionLangDtoList = new ArrayList<>();

        List<RegionEntity> allByVisibleTrue = regionRepository.findAllVisible();

        for (RegionEntity regionEntity : allByVisibleTrue) {

            RegionResponseDTO regionLangDto = new RegionResponseDTO();
            regionLangDto.setId(regionEntity.getId());
            switch (lang) {
                case UZ -> regionLangDto.setName(regionEntity.getNameUz());
                case RU -> regionLangDto.setName(regionEntity.getNameRu());
                case EN -> regionLangDto.setName(regionEntity.getNameEn());
            }
            regionLangDtoList.add(regionLangDto);
        }
        return regionLangDtoList;
    }

    // 5. Region By Lang (Native query)
    public List<RegionResponseDTO> getRegionByLang2(LanguageEnum lang) {

        List<RegionResponseDTO> regionLangDtoList = new ArrayList<>();

        List<RegionMapper> allByVisibleTrue = regionRepository.findAll(lang.name());

        for (RegionMapper regionMapper : allByVisibleTrue) {
            RegionResponseDTO regionLangDto = new RegionResponseDTO();
            regionLangDto.setId(regionMapper.getId());
            regionLangDto.setName(regionMapper.getName());
            regionLangDtoList.add(regionLangDto);
        }
        return regionLangDtoList;
    }


    public RegionResponseDTO getRegion(Integer id, Language lang) {
        RegionEntity region = getRegionEntityById(id,lang);
        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setId(region.getId());
        switch (lang) {
            case UZ -> dto.setName(region.getNameUz());
            case RU -> dto.setName(region.getNameRu());
            default -> dto.setName(region.getNameEn());
        }
        return dto;
    }



    public RegionResponseDTO toDTO(RegionEntity entity){
        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setCreateDate(entity.getCreateDate());
        return dto;
    }

    public RegionEntity getRegionEntityById(int id, Language lang) {
        return regionRepository.findById(id).orElseThrow(() -> {
            String message=rbms.getMessage("item.not.found",null, new Locale(lang.name()));
            throw new AppBadException(message);
        });
    }


}
