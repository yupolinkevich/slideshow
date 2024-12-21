package io.ypolin.slideshow.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class GlobalEventListener {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onImageLifecycleEvent(ImageLifecycleEvent imageLifecycleEvent) {
        log.info("Transaction completed");
        log.info(imageLifecycleEvent.getMessage());
        //any further actions
    }
    @Async
    @EventListener()
    public void onSlideshowEvent(SlideshowEvent slideshowEvent) {
        log.info(slideshowEvent.getMessage());
        //any further actions
    }
}
