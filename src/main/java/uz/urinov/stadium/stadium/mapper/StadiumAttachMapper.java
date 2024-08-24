package uz.urinov.stadium.stadium.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StadiumAttachMapper {
    Integer getId();

    String getDescription();

    Integer getDistrictId();

    LocalDate getCreateDate();

    Double getLat();

    Double getLon();

    List<String> getAttachId();

}
