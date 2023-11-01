package dev.lydtech.dispatch.handler;

import dev.lydtech.dispatch.message.DispatchPreparing;
import dev.lydtech.dispatch.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DispatchTrackingHandler {


    private final TrackingService trackingService ;


    @KafkaListener(
            id = "orderDispatchClient",
            topics = "dispatch.tracking",
            groupId = "dispatch.order.dispatch.consumer",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(DispatchPreparing dispatchPreparing)   {

        log.info("Received message: payload: " + dispatchPreparing);

       try {
           trackingService.prepare(dispatchPreparing);
       }
       catch (Exception e){
           log.error("Tracking failure ",e);
       }

    }
}
