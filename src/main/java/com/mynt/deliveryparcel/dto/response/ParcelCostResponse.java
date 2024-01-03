package com.mynt.deliveryparcel.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Total parcel cost")
public class ParcelCostResponse {

    @Schema(description = "Request Id.", example = "188fc4fe-b2cd-4d12-b731-15e8f92b0cba")
    private String requestId;

    @Schema(description = "Parcel cost", example = "100.00")
    private Double parcelCost;

}
