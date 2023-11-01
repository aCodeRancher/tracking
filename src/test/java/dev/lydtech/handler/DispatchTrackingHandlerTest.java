package dev.lydtech.handler;

import dev.lydtech.service.TrackingService;
import dev.lydtech.message.DispatchPreparing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.UUID;


import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


class DispatchTrackingHandlerTest {
    private DispatchTrackingHandler handler;
    private TrackingService trackingServiceMock;



    @BeforeEach
    void setUp() {
        trackingServiceMock = mock(TrackingService.class);
        handler = new DispatchTrackingHandler(trackingServiceMock);
    }

    @Test
    void listen_Success() throws Exception {
        DispatchPreparing testEvent = DispatchPreparing.builder().orderId(UUID.randomUUID()).build();
        handler.listen(testEvent);
        verify(trackingServiceMock, times(1)).prepare(testEvent);

    }

    @Test
    public void listen_ServiceThrowsException() throws Exception {
        DispatchPreparing testEvent = DispatchPreparing.builder().orderId(UUID.randomUUID()).build();

        doThrow(new RuntimeException("Service failure")).when(trackingServiceMock).prepare(testEvent);

        handler.listen(testEvent);

        verify(trackingServiceMock, times(1)).prepare(testEvent);
    }

}