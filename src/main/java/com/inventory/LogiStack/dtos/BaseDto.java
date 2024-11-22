package com.inventory.LogiStack.dtos;

import java.time.LocalDateTime;

public abstract class BaseDto<T> {
    private T id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
