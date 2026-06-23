package com.ia.backend.mappers;

import com.ia.backend.dtos.TaskResponse;
import com.ia.backend.entities.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponse toDTO(Task task);
}