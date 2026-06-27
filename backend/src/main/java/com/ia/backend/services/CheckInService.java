package com.ia.backend.services;

import com.ia.backend.dtos.CheckInRequest;
import com.ia.backend.dtos.CheckInResponse;
import com.ia.backend.entities.CheckIn;
import com.ia.backend.entities.User;
import com.ia.backend.entities.enums.Role;
import com.ia.backend.exceptions.AlreadyExistsException;
import com.ia.backend.exceptions.NotFoundException;
import com.ia.backend.mappers.CheckInMapper;
import com.ia.backend.repositories.CheckInRepository;
import com.ia.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final CheckInMapper checkInMapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CheckInResponse> getAllCheckIns() {
        return checkInRepository
                .findAllByOrderBySubmittedAtDesc()
                .stream()
                .map(checkInMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CheckInResponse> getAllCheckInsOfMember(Long userId) {
        userRepository.findByIdAndRole(userId, Role.MEMBER)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return checkInRepository
                .findBySubmittedBy_IdOrderBySubmittedAtDesc(userId)
                .stream()
                .map(checkInMapper::toDto)
                .toList();
    }

    @Transactional
    public CheckInResponse createCheckIn(User currentUser, CheckInRequest request) {
        LocalDate monday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        LocalDateTime startOfWeek = monday.atStartOfDay();
        LocalDateTime endOfWeek = sunday.atTime(LocalTime.MAX);

        if (checkInRepository.existsBySubmittedBy_IdAndSubmittedAtBetween(currentUser.getId(), startOfWeek, endOfWeek)) {
            throw new AlreadyExistsException("You have already checked in this week");
        }

        CheckIn newCheckIn = CheckIn.builder()
                .completedWork(request.completedWork())
                .blockers(request.blockers())
                .nextSteps(request.nextSteps())
                .submittedBy(currentUser)
                .build();

        return checkInMapper.toDto(checkInRepository.save(newCheckIn));
    }

    @Transactional(readOnly = true)
    public List<CheckInResponse> getMyCheckIns(User currentUser) {

        return checkInRepository
                .findBySubmittedBy_IdOrderBySubmittedAtDesc(currentUser.getId())
                .stream()
                .map(checkInMapper::toDto)
                .toList();
    }
}