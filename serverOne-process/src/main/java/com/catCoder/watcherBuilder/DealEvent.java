package com.catCoder.watcherBuilder;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DealEvent extends ApplicationEvent {

    private CreateOb createOb;

    public DealEvent(Object source, CreateOb createOb) {
        super(source);
        this.createOb = createOb;
    }
}
