package com.mynt.deliveryparcel.external;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class VoucherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private VoucherService voucherService;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetVoucherDetails() throws JsonProcessingException {
        String voucherCode = "MYNT";
        VoucherDto expectedVoucherDto = new VoucherDto();
        expectedVoucherDto.setCode("MYNT");
        expectedVoucherDto.setDiscount("0.15");
        expectedVoucherDto.setExpiry("2023-10-02");

        when(restTemplate.getForObject("/{voucherCode}", VoucherDto.class, voucherCode)).thenReturn(expectedVoucherDto);

        VoucherDto actualVoucherDto = voucherService.getVoucherDetails(voucherCode);

        assertEquals(expectedVoucherDto, actualVoucherDto);

    }
}
