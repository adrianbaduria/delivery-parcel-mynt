package com.mynt.deliveryparcel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mynt.deliveryparcel.dto.request.ParcelDetailsDto;
import com.mynt.deliveryparcel.dto.response.ParcelCostDto;
import com.mynt.deliveryparcel.service.ParcelDeliveryService;
import com.mynt.deliveryparcel.service.impl.ParcelDeliveryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class DeliveryParcelControllerTest {
    @Mock
    private ParcelDeliveryServiceImpl parcelService;

    @InjectMocks
    private DeliveryParcelController deliveryParcelController;

    @Test
    void testGetParcelCost_ValidParcelDetails_Returns200() {
        ParcelDetailsDto parcelDetailsDto = new ParcelDetailsDto();
        when(parcelService.computeParcelPrice(any())).thenReturn(new ParcelCostDto(50F));

        ResponseEntity<ParcelCostDto> response = deliveryParcelController.getParcelCost(parcelDetailsDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(parcelService, times(1)).computeParcelPrice(any());
    }

}
