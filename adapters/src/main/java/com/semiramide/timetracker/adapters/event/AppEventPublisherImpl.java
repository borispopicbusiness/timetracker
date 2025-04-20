package com.semiramide.timetracker.adapters.event;

import com.semiramide.timetracker.core.event.AppEventPublisher;
import lombok.Builder;
import org.springframework.context.ApplicationEventPublisher;

@Builder
public class AppEventPublisherImpl implements AppEventPublisher {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publishEvent(Object event) {
        publisher.publishEvent(event);
    }
}
