package com.mynt.deliveryparcel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelDetailsRequest {

    @NotNull(message = "Please input valid weight.")
    @Min(value = 1, message = "Weight should be at least 1 kg.")
    @Max(value = 50, message = "Weight should not exceed 50 kgs.")
    @Schema(description = "weight of the parcel", example = "50")
    private Float weight;

    @NotNull(message = "Please input valid height.")
    @Min(value = 1, message = "Weight should be at least 1 cm.")
    @Schema(description = "Height of the parcel", example = "20")
    private Float height;

    @NotNull(message = "Please input valid width.")
    @Min(value = 1, message = "Weight should be at least 1 cm.")
    @Schema(description = "Width of the parcel", example = "30")
    private Float width;

    @NotNull(message = "Please input valid length.")
    @Min(value = 1, message = "Weight should be at least 1 cm.")
    @Schema(description = "Length of the parcel", example = "5")
    private Float length;

    @Schema(description = "Voucher Code", example = "MYNT")
    private String voucherCode;

}
