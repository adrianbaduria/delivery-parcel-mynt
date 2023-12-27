package com.mynt.deliveryparcel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mynt.deliveryparcel.dto.request.ParcelDetailsRequest;
import com.mynt.deliveryparcel.dto.response.ParcelCostResponse;
import com.mynt.deliveryparcel.error.Exceptions;
import com.mynt.deliveryparcel.service.impl.ParcelDeliveryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(MockitoExtension.class)
public class DeliveryParcelControllerTest {

    @Mock
    private ParcelDeliveryServiceImpl parcelService;

    @InjectMocks
    private DeliveryParcelController deliveryParcelController;

    @Test
    void testGetParcelCost_ValidParcelDetails_Returns200() {
        ParcelDetailsRequest parcelDetailsRequest = new ParcelDetailsRequest();
        when(parcelService.computeParcelPrice(any()))
                .thenReturn(new ParcelCostResponse("188fc4fe-b2cd-4d12-b731-15e8f92b0cba",50F));

        ResponseEntity<ParcelCostResponse> response = deliveryParcelController.getParcelCost(parcelDetailsRequest);

        assertEquals("188fc4fe-b2cd-4d12-b731-15e8f92b0cba",response.getBody().getRequestId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(parcelService, times(1)).computeParcelPrice(any());
    }

}
