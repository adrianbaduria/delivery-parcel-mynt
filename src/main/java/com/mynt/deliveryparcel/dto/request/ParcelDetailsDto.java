package com.mynt.deliveryparcel.dto.request;

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
public class ParcelDetailsDto {

    @NotNull(message = "Please input valid weight.")
    @Min(value = 1, message = "Weight should be at least 1 kg.")
    @Max(value = 50, message = "Weight should not exceed 50 kgs.")
    private Float weight;

    @NotNull(message = "Please input valid height.")
    @Min(value = 1, message = "Weight should be at least 1 cm.")
    private Float height;

    @NotNull(message = "Please input valid width.")
    @Min(value = 1, message = "Weight should be at least 1 cm.")
    private Float width;

    @NotNull(message = "Please input valid length.")
    @Min(value = 1, message = "Weight should be at least 1 cm.")
    private Float length;

    private String voucherCode;

}
