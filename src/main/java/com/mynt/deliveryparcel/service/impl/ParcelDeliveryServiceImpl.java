package com.mynt.deliveryparcel.service.impl;

import com.mynt.deliveryparcel.constant.Constants;
import com.mynt.deliveryparcel.db.domain.ParcelDetails;
import com.mynt.deliveryparcel.db.repository.ParcelRuleRepository;
import com.mynt.deliveryparcel.dto.request.ParcelDetailsRequest;
import com.mynt.deliveryparcel.dto.response.ParcelCostResponse;
import com.mynt.deliveryparcel.enums.RuleName;
import com.mynt.deliveryparcel.error.Exceptions;
import com.mynt.deliveryparcel.external.VoucherDto;
import com.mynt.deliveryparcel.external.VoucherService;
import com.mynt.deliveryparcel.service.ParcelDeliveryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.net.ConnectException;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Parcel Delivery Service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ParcelDeliveryServiceImpl implements ParcelDeliveryService {

    private final VoucherService voucherService;

    private final ParcelRuleRepository parcelRuleRepository;

    private static final Logger logger = LoggerFactory.getLogger(ParcelDeliveryServiceImpl.class);

    /**
     * This function is to compute parcel price.
     *
     * @param parcelDetailsRequest parcel details.
     * @return parcel cost.
     */
    public ParcelCostResponse computeParcelPrice(ParcelDetailsRequest parcelDetailsRequest) {

        double volume = computeVolume(parcelDetailsRequest);

        RuleName ruleName = getRuleName(volume, parcelDetailsRequest.getWeight());

        logger.info("Rule Name: {} ", ruleName);

        ParcelDetails parcelRule = parcelRuleRepository.findByRuleName(ruleName)
                .orElseThrow(() -> new Exceptions(Constants.NO_PARCEL_RULE_FOUND));

        double cost = computeTotalCost(volume, parcelDetailsRequest.getWeight(),
                parcelRule.getCost(), ruleName);

        logger.info("cost: {} ", cost);

        if (StringUtils.hasLength(parcelDetailsRequest.getVoucherCode())) {
            cost = getDiscount(cost, parcelDetailsRequest.getVoucherCode());
        }

        ParcelCostResponse parcelCostResponse
                = ParcelCostResponse.builder().requestId(UUID.randomUUID().toString()).parcelCost(cost).build();

        logger.info("parcelCostResponse: {} ", parcelCostResponse);

        return parcelCostResponse;
    }

    /**
     * Computes height, length and width.
     *
     * @param parcelDetailsRequest parcel details.
     * @return total volume.
     */
    private double computeVolume(ParcelDetailsRequest parcelDetailsRequest){
        return parcelDetailsRequest.getHeight() * parcelDetailsRequest.getLength() * parcelDetailsRequest.getWidth();
    }


    /**
     * Gets the rule name based from volume and weight.
     *
     * @param volume volume of parcel
     * @param weight weight of parcel
     * @return rule name value
     */
    private RuleName getRuleName(Double volume, Double weight) {

        RuleName ruleName;

        if (weight > 50f) {
            ruleName = RuleName.REJECT;
        } else if (weight > 10f) {
            ruleName = RuleName.HEAVY_PARCEL;
        } else {
            if (volume < 1500f) {
                ruleName = RuleName.SMALL_PARCEL;
            } else if (volume >= 1500f && volume < 2500f) {
                ruleName = RuleName.MEDIUM_PARCEL;
            } else {
                ruleName = RuleName.LARGE_PARCEL;
            }
        }

        return ruleName;
    }

    /**
     * Computes the total cost depends on the rule name.
     *
     * @param volume volume of the parcel.
     * @param weight weight of the parcel.
     * @param cost cost based of parcel type.
     * @param ruleName enum of rule name.
     * @return total cost value.
     */
    private Double computeTotalCost(Double volume, Double weight, Double cost, RuleName ruleName) {
        double computedCost;

        if(ruleName == RuleName.REJECT) {
            throw new Exceptions(Constants.REJECT);
        }else if (ruleName == RuleName.HEAVY_PARCEL) {
            computedCost = cost * weight;
        } else {
            computedCost = cost * volume;
        }

        return computedCost;
    }

    /**
     * Gets discount price based on voucher.
     *
     * @param originalCost computed cost based from the base cost and weight or volume.
     * @param voucherCode voucher code inputted.
     * @return computed total cost including the discounted price.
     */
    private double getDiscount(double originalCost, String voucherCode) {
        try {
            VoucherDto voucherDto = voucherService.getVoucherDetails(voucherCode);

            logger.debug("voucherDTO : {}", voucherDto);

            if (voucherDto != null) {
                LocalDate expiryDate = LocalDate.parse(voucherDto.getExpiry());

                if (LocalDate.now().isAfter(expiryDate)) {
                    logger.warn("voucher expired");
                    throw new Exceptions(Constants.VOUCHER_EXPIRED);
                }

                float discountPercentage = Float.parseFloat(voucherDto.getDiscount());
                return originalCost - (originalCost * discountPercentage);
            }
        } catch (HttpClientErrorException exception) {
            logger.error("Error encountered : {}", exception);
            throw new Exceptions(Constants.VOUCHER_INVALID);
        }

        return 0; // Default to original cost if voucherDto is null
    }

}
