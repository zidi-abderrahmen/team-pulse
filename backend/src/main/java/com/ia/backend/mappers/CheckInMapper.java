package com.ia.backend.mappers;

import com.ia.backend.dtos.CheckInRequest;
import com.ia.backend.dtos.CheckInResponse;
import com.ia.backend.entities.CheckIn;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CheckInMapper {

    CheckInResponse toDto(CheckIn checkIn);
}