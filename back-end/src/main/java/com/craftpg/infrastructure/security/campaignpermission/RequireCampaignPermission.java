package com.craftpg.infrastructure.security.campaignpermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireCampaignPermission {

    CampaignPermissionAction action();

    int campaignIdArgIndex() default 0;
}
