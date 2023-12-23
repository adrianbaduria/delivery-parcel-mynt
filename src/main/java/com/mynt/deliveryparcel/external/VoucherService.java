package com.mynt.deliveryparcel.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VoucherService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${voucher.api.stub.response}")
    private boolean isStubResponse;

    @Value("${voucher.api.stub.value}")
    private String stubValue;

    /**
     * Gets the voucher details from the external api.
     *
     * @param voucherCode voucher code.
     * @return voucherDto details
     */
    public VoucherDto getVoucherDetails(String voucherCode) {

        //stub
        if (isStubResponse){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(stubValue, VoucherDto.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        return restTemplate.getForObject("/{voucherCode}", VoucherDto.class, voucherCode);

    }

}
