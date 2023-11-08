package dev.lydtech.dispatch.service;

import dev.lydtech.dispatch.message.DispatchCompleted;
import dev.lydtech.dispatch.message.DispatchPreparing;
import dev.lydtech.dispatch.message.TrackingStatusUpdated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TrackingServiceTest {

    private TrackingService service;
    private KafkaTemplate kafkaProducerMock;

    @BeforeEach
    void setUp() {
        kafkaProducerMock = mock(KafkaTemplate.class);
        service = new TrackingService(kafkaProducerMock);
    }

    @Test
    void process_PrepSuccess() throws Exception {
       when(kafkaProducerMock.send(anyString(), any(TrackingStatusUpdated.class))).thenReturn(mock(CompletableFuture.class));
       DispatchPreparing testEvent = DispatchPreparing.builder().orderId(UUID.randomUUID()).build();

       service.prepare(testEvent);

       verify(kafkaProducerMock, times(1)).send(eq("tracking.status"), any(TrackingStatusUpdated.class));
    }

    @Test
    void process_CompSuccess() throws Exception {
        when(kafkaProducerMock.send(anyString(), any(TrackingStatusUpdated.class))).thenReturn(mock(CompletableFuture.class));
        DispatchPreparing testEvent = DispatchPreparing.builder().orderId(UUID.randomUUID()).build();
        DispatchCompleted completed = DispatchCompleted.builder().orderId(testEvent.getOrderId())
                .date(LocalDateTime.now().toString()).build();

        service.complete(completed);
        verify(kafkaProducerMock, times(1)).send(eq("tracking.status"), any(TrackingStatusUpdated.class));
    }


    @Test
    public void process_PrepThrowsException() {
        DispatchPreparing testEvent = DispatchPreparing.builder().orderId(UUID.randomUUID()).build();
        doThrow(new RuntimeException("Producer failure")).when(kafkaProducerMock).send(eq("tracking.status"), any(TrackingStatusUpdated.class));

        Exception exception = assertThrows(RuntimeException.class, () -> service.prepare(testEvent));

        verify(kafkaProducerMock, times(1)).send(eq("tracking.status"), any(TrackingStatusUpdated.class));
        assertThat(exception.getMessage(), equalTo("Producer failure"));
    }


    @Test
    public void process_CompThrowsException() {
        DispatchPreparing testEvent = DispatchPreparing.builder().orderId(UUID.randomUUID()).build();
        DispatchCompleted completedEvent = DispatchCompleted.builder().orderId(testEvent.getOrderId())
                                          .date(LocalDateTime.now().toString()).build();
        doThrow(new RuntimeException("Producer failure")).when(kafkaProducerMock).send(eq("tracking.status"), any(TrackingStatusUpdated.class));

        Exception exception = assertThrows(RuntimeException.class, () -> service.complete(completedEvent));

        verify(kafkaProducerMock, times(1)).send(eq("tracking.status"), any(TrackingStatusUpdated.class));
        assertThat(exception.getMessage(), equalTo("Producer failure"));
    }
}