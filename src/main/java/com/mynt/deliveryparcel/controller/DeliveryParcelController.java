package com.mynt.deliveryparcel.controller;

import com.mynt.deliveryparcel.service.ParcelDeliveryService;
import com.mynt.deliveryparcel.dto.request.ParcelDetailsDto;
import com.mynt.deliveryparcel.dto.response.ParcelCostDto;
import com.mynt.deliveryparcel.service.impl.ParcelDeliveryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/parcel-delivery/")
public class DeliveryParcelController {
    private final ParcelDeliveryService parcelService;

    /**
     * Gets the parcel details.
     *
     * @param parcelDetailsDto
     * @return parcel cost.
     */
    @Operation(
            description = "",
            summary = "Retrieves total cost of parcel by weight and volume.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful execution.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ParcelCostDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error.",
                    content = @Content)
    })
    @PostMapping(value = "/cost", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParcelCostDto> getParcelCost(@Valid @RequestBody ParcelDetailsDto parcelDetailsDto) {
        return new ResponseEntity<>(parcelService.computeParcelPrice(parcelDetailsDto), HttpStatus.OK);
    }

}
