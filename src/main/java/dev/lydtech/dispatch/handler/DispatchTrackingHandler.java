package dev.lydtech.dispatch.handler;

import dev.lydtech.dispatch.message.DispatchPreparing;
import dev.lydtech.dispatch.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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
}
