package com.craftpg.infrastructure.factory;

import com.craftpg.infrastructure.exception.ApiException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UseCaseFactory implements UseCaseProvider {

    private static final String USECASE_PACKAGE = "com.craftpg.application.usecase";
    private final ApplicationContext applicationContext;
    private final Map<Class<?>, Object> useCaseMap = new HashMap<>();

    @PostConstruct
    public void init() {
        applicationContext.getBeansOfType(Object.class)
                .values()
                .forEach(this::registerUseCaseInterfaces);
    }

    @Override
    public <T> T getUseCase(final Class<T> useCaseType) {
        var useCase = useCaseMap.get(useCaseType);
        if (useCase == null) {
            throw new ApiException("Use case not found for type: " + useCaseType.getName());
        }
        return useCaseType.cast(useCase);
    }

    private void registerUseCaseInterfaces(final Object bean) {
        var targetClass = AopUtils.getTargetClass(bean);
        for (var interfaceType : targetClass.getInterfaces()) {
            if (interfaceType.getPackageName().startsWith(USECASE_PACKAGE)) {
                useCaseMap.put(interfaceType, bean);
            }
        }
    }
}
