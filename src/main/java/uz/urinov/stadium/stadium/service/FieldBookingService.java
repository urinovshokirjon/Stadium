package uz.urinov.stadium.stadium.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.exp.AppBadException;
import uz.urinov.stadium.stadium.dto.BookingFieldCreateDto;
import uz.urinov.stadium.stadium.dto.BookingResponseDto;
import uz.urinov.stadium.stadium.entity.FieldBookingEntity;
import uz.urinov.stadium.stadium.entity.FieldEntity;
import uz.urinov.stadium.stadium.entity.StadiumEntity;
import uz.urinov.stadium.stadium.enums.BookingStatus;
import uz.urinov.stadium.stadium.enums.Status;
import uz.urinov.stadium.stadium.repository.FieldBookingRepository;
import uz.urinov.stadium.stadium.repository.FieldRepository;
import uz.urinov.stadium.util.Result;
import uz.urinov.stadium.util.SecurityUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FieldBookingService {
    @Autowired
    private ResourceBundleMessageSource rbms;
    private final FieldBookingRepository fieldBookingRepository;
    private final FieldService fieldService;
    private final FieldRepository fieldRepository;
    private final StadiumService stadiumService;

    // 1.Field order
    public Result fieldBookingOrder(BookingFieldCreateDto dto, Language lang) {
        Duration duration = Duration.between(dto.getStartTime(), dto.getEndTime());
        if (duration.toMinutes() < 60 || duration.toMinutes() > 181) {
            String message = rbms.getMessage("time.is.short", null, new Locale(lang.name()));
            return new Result(message, false);
        }

        fieldService.getById(dto.getFieldId(), lang);
        List<String> statusList = List.of(BookingStatus.CHECKING.name(), BookingStatus.BOOKED.name());
        List<FieldBookingEntity> fieldBookingEntityList = fieldBookingRepository.findByFieldIdAndStartTimeBeforeAndEndTimeAfter(dto.getFieldId(), dto.getStartTime(), dto.getEndTime(), statusList);

        if (!fieldBookingEntityList.isEmpty()) {
            String message = rbms.getMessage("booked", null, new Locale(lang.name()));
            return new Result(message, false);
        }


        FieldBookingEntity entity = new FieldBookingEntity();
        entity.setFieldId(dto.getFieldId());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setCustomerId(SecurityUtil.getProfileId());
        fieldBookingRepository.save(entity);

        String message = rbms.getMessage("being.checked", null, new Locale(lang.name()));
        return new Result(message, true);
    }

    // 2.Field cancellation user
    public Result fieldBookingIdCancellationUser(String bookingId, Language lang) {
//        getFieldBooking(bookingId,lang);
        List<String> statusList = List.of(BookingStatus.CHECKING.name(), BookingStatus.BOOKED.name());

        Optional<FieldBookingEntity> fieldBookingEntity = fieldBookingRepository.fieldBookingIdCancellationUser(bookingId, SecurityUtil.getProfileId(), statusList);
        if (fieldBookingEntity.isEmpty()) {
            String message = rbms.getMessage("item.not.found", null, new Locale(lang.name()));
            return new Result(message, false);
        }
        FieldBookingEntity entity = fieldBookingEntity.get();
        LocalDateTime startTime = entity.getStartTime();


        Duration duration = Duration.between(startTime, LocalDateTime.now());
        long minutes = duration.abs().toMinutes();
        if (minutes < 120) {
            String message = rbms.getMessage("not.cancelled", null, new Locale(lang.name()));
            return new Result(message, false);
        }
        entity.setStatus(BookingStatus.CANCELED);
        fieldBookingRepository.save(entity);
        String message = rbms.getMessage("cancelled", null, new Locale(lang.name()));
        return new Result(message, true);
    }

    // 3.Field cancellation owner
    public Result fieldBookingIdCancellationOwner(String bookingId, Language lang) {

        List<String> statusList = List.of(BookingStatus.CHECKING.name());
        Optional<FieldBookingEntity> fieldBooking = fieldBookingRepository.fieldBookingIdCancellationOwner(bookingId, statusList);

        if (fieldBooking.isEmpty()) {
            String message = rbms.getMessage("item.not.found", null, new Locale(lang.name()));
            return new Result(message, false);
        }
        FieldBookingEntity entity = fieldBooking.get();
        fieldService.getFieldOwnerById(entity.getFieldId(), lang);

        entity.setStatus(BookingStatus.NOT_CONFIRMED);
        fieldBookingRepository.save(entity);
        String message = rbms.getMessage("cancelled", null, new Locale(lang.name()));
        return new Result(message, true);
    }


    // Field order user
    public List<BookingResponseDto> getOrderUser(BookingStatus status, Language lang) {

        List<BookingResponseDto> responseList = new ArrayList<>();
        for (FieldBookingEntity bookingEntity : fieldBookingRepository.findAllByCustomerIdAndStatusAndVisibleTrue(SecurityUtil.getProfileId(), status)) {
            BookingResponseDto dto = new BookingResponseDto();
            dto.setStartTime(bookingEntity.getStartTime());
            dto.setEndTime(bookingEntity.getEndTime());
            dto.setCreateDate(bookingEntity.getCreateDate());
            dto.setFieldResponseMiniDto(fieldService.fieldDetailsMini(fieldService.getById(bookingEntity.getFieldId(), lang), lang));
            dto.setStadiumResponseMiniDto(stadiumService.stadiumDetailsMini(stadiumService.getStadiumById(dto.getFieldResponseMiniDto().getStadiumId(), lang)));
            responseList.add(dto);
        }
        return responseList;
    }


    // Field order owner field id
    public List<BookingResponseDto> getOrderOwnerFieldId(Integer fieldId, Language lang) {
        FieldEntity fieldEntity = fieldService.getFieldOwnerById(fieldId, lang);
        List<BookingResponseDto> responseList = new ArrayList<>();

        for (FieldBookingEntity bookingEntity : fieldBookingRepository.findAllByFieldIdAndVisibleTrue(fieldEntity.getId())) {
            BookingResponseDto dto = new BookingResponseDto();
            dto.setStartTime(bookingEntity.getStartTime());
            dto.setEndTime(bookingEntity.getEndTime());
            dto.setStatus(bookingEntity.getStatus());
            dto.setCreateDate(bookingEntity.getCreateDate());
            dto.setFieldResponseMiniDto(fieldService.fieldDetailsMini(fieldService.getById(bookingEntity.getFieldId(), lang), lang));
            dto.setStadiumResponseMiniDto(stadiumService.stadiumDetailsMini(stadiumService.getStadiumById(dto.getFieldResponseMiniDto().getStadiumId(), lang)));
            responseList.add(dto);

        }
        return responseList;
    }

    // Field order owner stadium id
    public List<BookingResponseDto> getOrderOwnerStadiumId(Integer stadiumId, Language lang) {

        List<BookingResponseDto> responseList = new ArrayList<>();
        StadiumEntity stadiumOwnerById = stadiumService.getStadiumOwnerById(stadiumId, lang);
        for (FieldEntity fieldEntity : fieldRepository.findAllByStadiumIdAndVisibleTrueAndStatus(stadiumOwnerById.getId(), Status.ACTIVE)) {

            for (FieldBookingEntity bookingEntity : fieldBookingRepository.findAllByFieldIdAndVisibleTrue(fieldEntity.getId())) {
                BookingResponseDto dto = new BookingResponseDto();
                dto.setStartTime(bookingEntity.getStartTime());
                dto.setEndTime(bookingEntity.getEndTime());
                dto.setStatus(bookingEntity.getStatus());
                dto.setCreateDate(bookingEntity.getCreateDate());
                dto.setFieldResponseMiniDto(fieldService.fieldDetailsMini(fieldService.getById(bookingEntity.getFieldId(), lang), lang));
                dto.setStadiumResponseMiniDto(stadiumService.stadiumDetailsMini(stadiumService.getStadiumById(dto.getFieldResponseMiniDto().getStadiumId(), lang)));
                responseList.add(dto);
            }

        }

        return responseList;
    }

    public List<BookingResponseDto> getOrderOwnerStadiumId2(Integer stadiumId, Language lang) {


        StadiumEntity stadiumOwnerById = stadiumService.getStadiumOwnerById(stadiumId, lang);
        List<FieldEntity> fieldEntityList = fieldRepository.findAllByStadiumIdAndVisibleTrueAndStatus(stadiumOwnerById.getId(), Status.ACTIVE);
        List<Integer> fieldIdList = fieldEntityList.stream().map(FieldEntity::getId).toList();

        List<FieldBookingEntity> allByFieldIdInAndVisibleTrue = fieldBookingRepository.findAllByFieldIdInAndVisibleTrue(fieldIdList);

        List<BookingResponseDto> responseList = new ArrayList<>(allByFieldIdInAndVisibleTrue.size());

        for (FieldBookingEntity bookingEntity : allByFieldIdInAndVisibleTrue) {

            BookingResponseDto dto = new BookingResponseDto();
            dto.setStartTime(bookingEntity.getStartTime());
            dto.setEndTime(bookingEntity.getEndTime());
            dto.setStatus(bookingEntity.getStatus());
            dto.setCreateDate(bookingEntity.getCreateDate());

            dto.setFieldResponseMiniDto(fieldService.fieldDetailsMini(bookingEntity.getField(), lang));
            dto.setStadiumResponseMiniDto(stadiumService.stadiumDetailsMini(stadiumOwnerById));
            responseList.add(dto);
        }

        return responseList;
    }


    public FieldBookingEntity getFieldBooking(String bookingId, Language lang) {
        return fieldBookingRepository.findByIdAndVisibleTrue(bookingId).orElseThrow(() -> {
            String message = rbms.getMessage("item.not.found", null, new Locale(lang.name()));
            throw new AppBadException(message);
        });
    }


}
