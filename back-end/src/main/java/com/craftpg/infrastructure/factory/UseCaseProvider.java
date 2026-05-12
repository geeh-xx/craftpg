package com.craftpg.infrastructure.factory;

public interface UseCaseProvider {

    <T> T getUseCase(Class<T> useCaseType);
}
