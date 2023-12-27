package com.mynt.deliveryparcel.service;

import com.mynt.deliveryparcel.dto.request.ParcelDetailsRequest;
import com.mynt.deliveryparcel.dto.response.ParcelCostResponse;

/**
 * Parcel Delivery Service.
 */
public interface ParcelDeliveryService {

    ParcelCostResponse computeParcelPrice(ParcelDetailsRequest parcelDetailsRequest);

}
