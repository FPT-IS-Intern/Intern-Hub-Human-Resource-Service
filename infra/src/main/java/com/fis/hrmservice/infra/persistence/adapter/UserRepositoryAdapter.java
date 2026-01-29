package com.fis.hrmservice.infra.persistence.adapter;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.UserRepositoryPort;
import com.fis.hrmservice.infra.persistence.entity.User;
import com.fis.hrmservice.infra.persistence.repository.PositionJpaRepository;
import com.fis.hrmservice.infra.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    private final PositionJpaRepository positionJpaRepository;

    @Override
    public UserModel save(UserModel user) {
        User entity = toEntity(user);
        User savedEntity = userJpaRepository.save(entity);
        return toModel(savedEntity);
    }

    @Override
    public Optional<UserModel> findById(Long userId) {
        return userJpaRepository.findById(userId)
                .map(this::toModel);
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return userJpaRepository.findByCompanyEmail(email)
                .map(this::toModel);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByCompanyEmail(email);
    }

    @Override
    public boolean existsByIdNumber(String idNumber) {
        return userJpaRepository.existsByIdNumber(idNumber);
    }

    // === Mapping methods ===

    private User toEntity(UserModel model) {
        User entity = new User();
        entity.setId(model.getUserId());
        entity.setPosition(positionJpaRepository.findById((model.getPositionId())).get());
        entity.setMentor(userJpaRepository.findMentorById((model.getMentorId())));
        entity.setFullName(model.getFullName());
        entity.setIdNumber(model.getIdNumber());
        entity.setDateOfBirth(model.getDateOfBirth());
        entity.setCompanyEmail(model.getCompanyEmail());
        entity.setPhoneNumber(model.getPhoneNumber());
        entity.setAddress(model.getAddress());
        entity.setInternshipStartDate(model.getInternshipStartDate());
        entity.setInternshipEndDate(model.getInternshipEndDate());
        entity.setStatus(model.getStatus());
        return entity;
    }

    private UserModel toModel(User entity) {
        return UserModel.builder()
                .userId(entity.getId())
                .positionId(entity.getPosition().getId())
                .mentorId(entity.getMentor().getId())
                .fullName(entity.getFullName())
                .idNumber(entity.getIdNumber())
                .dateOfBirth(entity.getDateOfBirth())
                .companyEmail(entity.getCompanyEmail())
                .phoneNumber(entity.getPhoneNumber())
                .address(entity.getAddress())
                .internshipStartDate(entity.getInternshipStartDate())
                .internshipEndDate(entity.getInternshipEndDate())
                .status(entity.getStatus())
                .build();
    }
}
