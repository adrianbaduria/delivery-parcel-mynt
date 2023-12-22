package com.mynt.deliveryparcel.service;

import com.mynt.deliveryparcel.db.domain.ParcelDetails;
import com.mynt.deliveryparcel.db.repository.ParcelRuleRepository;
import com.mynt.deliveryparcel.enums.RuleName;
import com.mynt.deliveryparcel.error.Exceptions;
import com.mynt.deliveryparcel.error.ParcelCostException;
import com.mynt.deliveryparcel.external.VoucherDto;
import com.mynt.deliveryparcel.external.VoucherService;
import com.mynt.deliveryparcel.constant.Constants;
import com.mynt.deliveryparcel.dto.request.ParcelDetailsDto;
import com.mynt.deliveryparcel.dto.response.ParcelCostDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ParcelDeliveryService {

    private final VoucherService voucherService;

    private final ParcelRuleRepository parcelRuleRepository;

    public ParcelCostDto computeParcelPrice(ParcelDetailsDto parcelDetailsDto) {

        float volume = computeVolume(parcelDetailsDto);

        RuleName ruleName = getRuleName(volume, parcelDetailsDto.getWeight());

        ParcelDetails parcelRule = parcelRuleRepository.findByRuleName(ruleName)
                .orElseThrow(() -> new Exceptions(Constants.NO_PARCEL_RULE_FOUND));

        float cost = computeCost(volume, parcelDetailsDto.getWeight(),
                parcelRule.getBaseCost(), ruleName);

        if (StringUtils.hasLength(parcelDetailsDto.getVoucherCode())) {
            cost = getDiscountedPrice(cost, parcelDetailsDto.getVoucherCode());
        }

        return new ParcelCostDto(cost);
    }

    private float computeVolume(ParcelDetailsDto parcelDetailsDto){
        return parcelDetailsDto.getHeight() * parcelDetailsDto.getLength() * parcelDetailsDto.getWidth();
    }

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

    private float computeCost(float volume, float weight, float baseCost, RuleName ruleName) {
        float cost;

        if(ruleName == RuleName.REJECT) {
            throw new ParcelCostException(Constants.REJECT);
        }else if (ruleName == RuleName.HEAVY_PARCEL) {
            cost = baseCost * weight;
        } else {
            cost = baseCost * volume;
        }

        return cost;
    }

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
