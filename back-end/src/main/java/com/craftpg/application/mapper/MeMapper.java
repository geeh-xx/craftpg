package com.craftpg.application.mapper;

import com.craftpg.domain.model.user.AppUser;
import com.craftpg.infrastructure.web.dto.MeResponse;
import org.springframework.stereotype.Component;

@Component
public class MeMapper {

    public MeResponse toResponse(final AppUser user) {
        return new MeResponse(user.getId(), user.getEmail(), user.getDisplayName());
    }
}
