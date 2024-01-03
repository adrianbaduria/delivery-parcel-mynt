package com.mynt.deliveryparcel.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mynt.deliveryparcel.constant.Constants;
import com.mynt.deliveryparcel.db.domain.ParcelDetails;
import com.mynt.deliveryparcel.db.repository.ParcelRuleRepository;
import com.mynt.deliveryparcel.dto.request.ParcelDetailsRequest;
import com.mynt.deliveryparcel.dto.response.ParcelCostResponse;
import com.mynt.deliveryparcel.enums.RuleName;
import com.mynt.deliveryparcel.error.Exceptions;
import com.mynt.deliveryparcel.external.VoucherDto;
import com.mynt.deliveryparcel.external.VoucherService;
import com.mynt.deliveryparcel.service.impl.ParcelDeliveryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Optional;

class ParcelDeliveryServiceTest {

    @Mock
    private VoucherService voucherService;

    @Mock
    private ParcelRuleRepository parcelRuleRepository;

    @InjectMocks
    private ParcelDeliveryServiceImpl parcelDeliveryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testComputeParcelPrice_noVoucher() {
        ParcelDetailsRequest parcelDetailsRequest = new ParcelDetailsRequest(1.00, 2.00, 3.00, 4.00, null);
        RuleName ruleName = RuleName.SMALL_PARCEL;

        ParcelDetails smallParcel = ParcelDetails.builder().ruleName(ruleName).cost(0.03).build();

        when(parcelRuleRepository.findByRuleName(ruleName)).thenReturn(Optional.of(smallParcel));
        when(voucherService.getVoucherDetails(anyString())).thenReturn(null);

        ParcelCostResponse result = parcelDeliveryService.computeParcelPrice(parcelDetailsRequest);

        assertEquals(0.72, result.getParcelCost());
    }

    @Test
    void testComputeParcelPrice_withVoucher() {
        ParcelDetailsRequest parcelDetailsRequest = new ParcelDetailsRequest(1.00, 2.00, 3.00, 4.00, "MYNT");

        RuleName ruleName = RuleName.SMALL_PARCEL;

        ParcelDetails parcelDetails = new ParcelDetails( 2L, ruleName, 0.03f, 0.03);
        when(parcelRuleRepository.findByRuleName(ruleName)).thenReturn(Optional.of(parcelDetails));

        VoucherDto voucherDto = new VoucherDto("MYNT", "0.1", LocalDate.now().plusDays(1).toString());
        when(voucherService.getVoucherDetails("MYNT")).thenReturn(voucherDto);

        ParcelCostResponse result = parcelDeliveryService.computeParcelPrice(parcelDetailsRequest);

        assertEquals(0.6479999989271163, result.getParcelCost());
    }

    @Test
    void testComputeParcelPrice_withVoucherHeavyParcel() {
        ParcelDetailsRequest parcelDetailsRequest = new ParcelDetailsRequest(20.00, 2.00, 3.00, 4.00, "VOUCHER_CODE");
        RuleName ruleName = RuleName.HEAVY_PARCEL;

        ParcelDetails parcelDetails = new ParcelDetails(1L, ruleName, 0F ,20.00);
        when(parcelRuleRepository.findByRuleName(ruleName)).thenReturn(Optional.of(parcelDetails));

        VoucherDto voucherDto = new VoucherDto("MYNT", "0.1", LocalDate.now().plusDays(1).toString());
        when(voucherService.getVoucherDetails("VOUCHER_CODE")).thenReturn(voucherDto);

        ParcelCostResponse result = parcelDeliveryService.computeParcelPrice(parcelDetailsRequest);

        assertEquals(359.99999940395355, result.getParcelCost());
    }

    @Test
    void testComputeParcelPrice_withVoucherMediumParcel() {
        ParcelDetailsRequest parcelDetailsRequest = new ParcelDetailsRequest(1.00, 50.00, 10.00, 4.00, "VOUCHER_CODE");
        RuleName ruleName = RuleName.MEDIUM_PARCEL;

        ParcelDetails parcelDetails = new ParcelDetails( 3L, ruleName, 0.04f, 0.04);
        when(parcelRuleRepository.findByRuleName(ruleName)).thenReturn(Optional.of(parcelDetails));

        VoucherDto voucherDto = new VoucherDto("MYNT", "0.1", LocalDate.now().plusDays(1).toString());
        when(voucherService.getVoucherDetails("VOUCHER_CODE")).thenReturn(voucherDto);

        ParcelCostResponse result = parcelDeliveryService.computeParcelPrice(parcelDetailsRequest);

        assertEquals(71.99999988079071, result.getParcelCost());
    }

    @Test
    void computeParcelPrice_withVoucherLargeParcel() {
        ParcelDetailsRequest parcelDetailsRequest = new ParcelDetailsRequest(10.00, 50.00, 10.00, 6.00, "MYNT");
        RuleName ruleName = RuleName.LARGE_PARCEL;

        ParcelDetails parcelDetails = new ParcelDetails( 4L, ruleName, 0.05f, 0.05);
        when(parcelRuleRepository.findByRuleName(ruleName)).thenReturn(Optional.of(parcelDetails));

        VoucherDto voucherDto = new VoucherDto("MYNT", "0.1", LocalDate.now().plusDays(1).toString());
        when(voucherService.getVoucherDetails("MYNT")).thenReturn(voucherDto);

        ParcelCostResponse result = parcelDeliveryService.computeParcelPrice(parcelDetailsRequest);

        assertEquals(134.99999977648258, result.getParcelCost());
    }

    @Test
    void computeParcelPrice_withVoucherLargeParcelExpiredVoucher() {
        ParcelDetailsRequest parcelDetailsRequest = new ParcelDetailsRequest(10.00, 50.00, 10.00, 6.00, "MYNT");
        RuleName ruleName = RuleName.LARGE_PARCEL;

        ParcelDetails parcelDetails = new ParcelDetails( 4L, ruleName, 0.05f, 0.05);
        when(parcelRuleRepository.findByRuleName(ruleName)).thenReturn(Optional.of(parcelDetails));

        VoucherDto voucherDto = new VoucherDto("MYNT", "0.1", LocalDate.now().minusDays(90).toString());
        when(voucherService.getVoucherDetails("MYNT")).thenReturn(voucherDto);

        Exceptions exception = assertThrows(Exceptions.class,
                () -> parcelDeliveryService.computeParcelPrice(parcelDetailsRequest));

        assertEquals(Constants.VOUCHER_EXPIRED, exception.getMessage());
    }

}
