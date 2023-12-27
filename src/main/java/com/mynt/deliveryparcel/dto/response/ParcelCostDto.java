package com.mynt.deliveryparcel.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Total parcel cost")
public class ParcelCostDto {

    @Schema(description = "Request Id.", example = "")
    private String requestId;

    @Schema(description = "Parcel cost", example = "100.00")
    private Float parcelCost;

}
