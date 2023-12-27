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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Parcel Delivery Service.
 */
@Service
@AllArgsConstructor
public class ParcelDeliveryServiceImpl implements ParcelDeliveryService {

    private final VoucherService voucherService;

    private final ParcelRuleRepository parcelRuleRepository;

    /**
     * This function is to compute parcel price.
     *
     * @param parcelDetailsRequest parcel details.
     * @return parcel cost.
     */
    public ParcelCostResponse computeParcelPrice(ParcelDetailsRequest parcelDetailsRequest) {

        float volume = computeVolume(parcelDetailsRequest);

        RuleName ruleName = getRuleName(volume, parcelDetailsRequest.getWeight());

        ParcelDetails parcelRule = parcelRuleRepository.findByRuleName(ruleName)
                .orElseThrow(() -> new Exceptions(Constants.NO_PARCEL_RULE_FOUND));

        float cost = computeCost(volume, parcelDetailsRequest.getWeight(),
                parcelRule.getBaseCost(), ruleName);

        if (StringUtils.hasLength(parcelDetailsRequest.getVoucherCode())) {
            cost = getDiscountedPrice(cost, parcelDetailsRequest.getVoucherCode());
        }

        ParcelCostResponse parcelCostResponse = com.mynt.deliveryparcel.dto.response.ParcelCostResponse.builder().requestId(UUID.randomUUID().toString()).parcelCost(cost).build();

        return parcelCostResponse;
    }

    /**
     * Computes height, length and width.
     *
     * @param parcelDetailsRequest parcel details.
     * @return total volume.
     */
    private float computeVolume(ParcelDetailsRequest parcelDetailsRequest){
        return parcelDetailsRequest.getHeight() * parcelDetailsRequest.getLength() * parcelDetailsRequest.getWidth();
    }

    /**
     * Gets the rule name based from volume and weight.
     *
     * @param volume volume of parcel
     * @param weight weight of parcel
     * @return rule name value
     */
    private RuleName getRuleName(float volume, float weight) {

        RuleName ruleName;

        if (Float.compare(weight, 50f) > 0) return RuleName.REJECT;

        if (Float.compare(weight, 10f) > 0) return RuleName.HEAVY_PARCEL;

        if (volume < 1500f) {
            ruleName = RuleName.SMALL_PARCEL;
        } else if (volume >= 1500f && volume < 2500f) {
            ruleName = RuleName.MEDIUM_PARCEL;
        } else {
            ruleName = RuleName.LARGE_PARCEL;
        }

        return ruleName;
    }

    /**
     * Computes the total cost depends on the rule name.
     *
     * @param volume volume of the parcel.
     * @param weight weight of the parcel.
     * @param baseCost cost based of parcel type.
     * @param ruleName enum of rule name.
     * @return total cost value.
     */
    private float computeCost(float volume, float weight, float baseCost, RuleName ruleName) {
        float cost;

        if(ruleName == RuleName.REJECT) {
            throw new Exceptions(Constants.REJECT);
        }else if (ruleName == RuleName.HEAVY_PARCEL) {
            cost = baseCost * weight;
        } else {
            cost = baseCost * volume;
        }

        return cost;
    }

    /**
     * Gets discount price based on voucher.
     *
     * @param originalCost computed cost based from the base cost and weight or volume.
     * @param voucherCode voucher code inputted.
     * @return computed total cost including the discounted price.
     */
    private float getDiscountedPrice(float originalCost, String voucherCode) {
        try {
            VoucherDto voucherDto = voucherService.getVoucherDetails(voucherCode);

            if (voucherDto != null) {
                LocalDate expiryDate = LocalDate.parse(voucherDto.getExpiry());

                if (LocalDate.now().isAfter(expiryDate)) {
                    throw new Exceptions(Constants.VOUCHER_EXPIRED);
                }

                float discountPercentage = Float.parseFloat(voucherDto.getDiscount());
                return originalCost - (originalCost * discountPercentage);
            }
        } catch (HttpClientErrorException exception) {
            throw new Exceptions(Constants.VOUCHER_INVALID);
        }

        return 0; // Default to original cost if voucherDto is null
    }

}
