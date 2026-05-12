package com.craftpg.application.usecase.example;

public interface CustomUseCaseHandler<U extends UseCaseInput<I>, I> {
    I handleCommand(U input);
}
