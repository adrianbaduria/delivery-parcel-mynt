package com.mynt.deliveryparcel.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDto {

    private String code;

    private String discount;

    private String expiry;

}
