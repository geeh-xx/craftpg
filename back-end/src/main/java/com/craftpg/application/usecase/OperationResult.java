package com.craftpg.application.usecase;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.craftpg.application.usecase.UseCaseOperationResultTypeEnum.ERROR;
import static com.craftpg.application.usecase.UseCaseOperationResultTypeEnum.FAIL;
import static com.craftpg.application.usecase.UseCaseOperationResultTypeEnum.SUCCESS;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OperationResult<T> {

    private T value;
    private String message;
    private UseCaseOperationResultTypeEnum resultType;

    public static <T> OperationResult<T> ok(@NonNull final T value) {
        var useCaseOutput = new OperationResult<T>();
        useCaseOutput.message = "";
        useCaseOutput.value = value;
        useCaseOutput.resultType = SUCCESS;
        return useCaseOutput;
    }

    public static <T> OperationResult<T> ok() {
        var useCaseOutput = new OperationResult<T>();
        useCaseOutput.message = "";
        useCaseOutput.resultType = SUCCESS;
        return useCaseOutput;
    }

    public static <T> OperationResult<T> failure(@NonNull final String message) {
        var useCaseOutput = new OperationResult<T>();
        useCaseOutput.message = message;
        useCaseOutput.resultType = FAIL;
        return useCaseOutput;
    }

    public static <T> OperationResult<T> error(@NonNull final String error) {
        var useCaseOutput = new OperationResult<T>();
        useCaseOutput.message = error;
        useCaseOutput.resultType = ERROR;
        return useCaseOutput;
    }

    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    public boolean isNotSuccess() {
        return (this.resultType == FAIL || this.resultType == ERROR);
    }
}
