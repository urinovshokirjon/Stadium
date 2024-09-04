package uz.urinov.stadium.stadium.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.stadium.dto.FieldResponseDto;
import uz.urinov.stadium.stadium.dto.StadiumResponseDto;
import uz.urinov.stadium.stadium.entity.FieldEntity;
import uz.urinov.stadium.stadium.entity.StadiumEntity;
import uz.urinov.stadium.stadium.enums.Status;
import uz.urinov.stadium.stadium.repository.StadiumRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckService {
    private final StadiumRepository stadiumRepository;
    private final StadiumService stadiumService;
    private final FieldService fieldService;

    // List status stadium
    public List<StadiumResponseDto> listStatusStadium(int page, int size, Status status, Language lang) {

        Pageable pageable = PageRequest.of(page, size);
        List<StadiumResponseDto> responseDtoList = new ArrayList<>();
        for (StadiumEntity stadium : stadiumRepository.listStatusStadium(status, pageable)) {
            responseDtoList.add(stadiumService.stadiumDetails(stadium));
        }
        return responseDtoList;
    }

    // List status field
    public List<FieldResponseDto> listStatusField(int page, int size, Status status, Language lang) {
        Pageable pageable = PageRequest.of(page, size);
        List<FieldResponseDto> responseDtoList = new ArrayList<>();
        for (FieldEntity field : stadiumRepository.listStatusField(status, pageable)) {
            responseDtoList.add(fieldService.fieldDetails(field, lang));
        }
        return responseDtoList;
    }

    // Status stadium
    public StadiumResponseDto statusStadium(int id, Status status, Language lang) {
        StadiumEntity stadiumById = stadiumService.getStadiumById(id, lang);
        stadiumById.setStatus(status);
        StadiumEntity entity = stadiumRepository.save(stadiumById);

        return stadiumService.stadiumDetails(entity);
    }
}
