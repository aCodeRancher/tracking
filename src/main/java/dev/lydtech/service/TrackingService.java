package dev.lydtech.service;

import dev.lydtech.message.DispatchPreparing;
import dev.lydtech.message.Status;
import dev.lydtech.message.TrackingStatusUpdated;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private static final String TRACKING_STATUS_TOPIC ="tracking.status";
    private final KafkaTemplate<String, Object> kafkaProducer;

    public void prepare(DispatchPreparing dispatchPreparing) throws Exception{
        TrackingStatusUpdated status= TrackingStatusUpdated.builder()
                        .orderId(dispatchPreparing.getOrderId()).status(Status.PREPARING).build();
        kafkaProducer.send(TRACKING_STATUS_TOPIC, status).get();
    }
}
