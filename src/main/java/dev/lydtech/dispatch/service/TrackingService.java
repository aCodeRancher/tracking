package dev.lydtech.dispatch.service;

import dev.lydtech.dispatch.message.DispatchCompleted;
import dev.lydtech.dispatch.message.DispatchPreparing;
import dev.lydtech.dispatch.message.Status;
import dev.lydtech.dispatch.message.TrackingStatusUpdated;
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

    public void complete(DispatchCompleted dispatchCompleted) throws Exception{
        TrackingStatusUpdated status= TrackingStatusUpdated.builder()
                .orderId(dispatchCompleted.getOrderId()).status(Status.COMPLETED).build();
        kafkaProducer.send(TRACKING_STATUS_TOPIC, status).get();
    }
}
