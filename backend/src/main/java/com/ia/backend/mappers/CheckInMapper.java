package com.ia.backend.mappers;

import com.ia.backend.dtos.checkin.CheckInResponse;
import com.ia.backend.entities.CheckIn;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CheckInMapper {

    CheckInResponse toDto(CheckIn checkIn);
}