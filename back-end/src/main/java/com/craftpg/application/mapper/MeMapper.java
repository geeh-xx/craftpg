package com.craftpg.application.mapper;

import com.craftpg.domain.model.AppUser;
import com.craftpg.infrastructure.web.dto.MeResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class MeMapper {

    @NonNull
    public MeResponse toResponse(@NonNull final AppUser user) {
        return new MeResponse(user.getId(), user.getEmail(), user.getDisplayName());
    }
}
