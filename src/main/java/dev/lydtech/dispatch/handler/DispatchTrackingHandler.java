package dev.lydtech.dispatch.handler;

import dev.lydtech.dispatch.message.DispatchCompleted;
import dev.lydtech.dispatch.message.DispatchPreparing;
import dev.lydtech.dispatch.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(
        id = "orderDispatchClient",
        topics = "dispatch.tracking",
        groupId = "dispatch.order.dispatch.consumer",
        containerFactory = "kafkaListenerContainerFactory"
)
public class DispatchTrackingHandler {


    private final TrackingService trackingService ;


    @KafkaHandler
    public void listen(DispatchPreparing dispatchPreparing)   {

        log.info("Received message: payload: " + dispatchPreparing);

       try {
           trackingService.prepare(dispatchPreparing);

       }
       catch (Exception e){
           log.error("Tracking failure ",e);
       }

    }

    @KafkaHandler
    public void listen(DispatchCompleted dispatchCompleted)   {

        log.info("Received message: payload: " + dispatchCompleted);

        try {
             trackingService.complete(dispatchCompleted);
        }
        catch (Exception e){
            log.error("Tracking failure ",e);
        }

    }
}
