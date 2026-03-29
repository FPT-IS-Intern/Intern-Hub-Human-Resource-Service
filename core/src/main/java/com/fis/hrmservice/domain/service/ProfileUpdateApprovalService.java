package com.fis.hrmservice.domain.service;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.feign.CreateAuthIdentityPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.PositionRepositoryPort;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProfileUpdateApprovalService {

    UserRepositoryPort userRepositoryPort;
    PositionRepositoryPort positionRepositoryPort;
    CreateAuthIdentityPort createAuthIdentityPort;

    @Transactional
    public void applyApprovedProfile(Long ticketId, Map<String, Object> payload) {
        log.info("Applying approved profile update. TicketId: {}", ticketId);

        if (payload == null) {
            throw new ConflictDataException("Ticket payload is null. TicketId: " + ticketId);
        }

        Long userId = extractLong(payload, "userId");
        if (userId == null) {
            throw new ConflictDataException("userId not found in ticket payload. TicketId: " + ticketId);
        }

        UserModel user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        applyProfileChanges(user, payload);

        userRepositoryPort.save(user);

        log.info("Profile update approved and applied for userId: {}, ticketId: {}", userId, ticketId);
    }

    @SuppressWarnings("unchecked")
    private void applyProfileChanges(UserModel user, Map<String, Object> payload) {
        Map<String, Object> newProfile = (Map<String, Object>) payload.get("newProfile");
        if (newProfile == null) {
            log.warn("newProfile not found in ticket payload, skipping field updates.");
            return;
        }

        applyStringField(user, newProfile, "fullName", user::setFullName);
        applyStringField(user, newProfile, "companyEmail", user::setCompanyEmail);
        applyStringField(user, newProfile, "idNumber", user::setIdNumber);
        applyStringField(user, newProfile, "address", user::setAddress);
        applyStringField(user, newProfile, "phoneNumber", user::setPhoneNumber);
        applyStringField(user, newProfile, "cvUrl", user::setCvUrl);
        applyStringField(user, newProfile, "avatarUrl", user::setAvatarUrl);

        applyLocalDateField(user, newProfile, "dateOfBirth", user::setDateOfBirth);

        applyLongField(user, newProfile, "positionId", positionId -> {
            if (positionId != null) {
                user.setPosition(positionRepositoryPort.findById(positionId)
                        .orElseThrow(() -> new NotFoundException("Position not found with id: " + positionId)));
            } else {
                user.setPosition(null);
            }
        });

        applyStringField(user, newProfile, "sysStatus", rawStatus -> {
            if (rawStatus != null && !rawStatus.isBlank()) {
                UserStatus newStatus = UserStatus.valueOf(rawStatus.toUpperCase());
                UserStatus oldStatus = user.getSysStatus();

                user.setSysStatus(newStatus);

                if (!Objects.equals(oldStatus, newStatus)) {
                    if (newStatus == UserStatus.SUSPENDED) {
                        createAuthIdentityPort.lockAuthIdentity(user.getUserId());
                    } else if (newStatus == UserStatus.APPROVED) {
                        createAuthIdentityPort.unlockAuthIdentity(user.getUserId());
                    }
                }
            }
        });
    }

    private void applyStringField(UserModel user, Map<String, Object> source, String key,
            java.util.function.Consumer<String> setter) {
        Object value = source.get(key);
        if (value != null) {
            setter.accept(value.toString());
        }
    }

    private void applyLocalDateField(UserModel user, Map<String, Object> source, String key,
            java.util.function.Consumer<LocalDate> setter) {
        Object value = source.get(key);
        if (value != null) {
            if (value instanceof LocalDate ld) {
                setter.accept(ld);
            } else if (value instanceof String str) {
                setter.accept(LocalDate.parse(str));
            }
        }
    }

    private void applyLongField(UserModel user, Map<String, Object> source, String key,
            java.util.function.Consumer<Long> setter) {
        Object value = source.get(key);
        if (value != null) {
            if (value instanceof Number num) {
                setter.accept(num.longValue());
            } else {
                setter.accept(Long.parseLong(value.toString()));
            }
        }
    }

    private Long extractLong(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number num) {
            return num.longValue();
        }
        return Long.parseLong(value.toString());
    }
}
