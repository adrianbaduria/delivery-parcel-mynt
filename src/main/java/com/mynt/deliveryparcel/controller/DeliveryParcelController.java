package com.mynt.deliveryparcel.controller;

import com.mynt.deliveryparcel.dto.request.ParcelDetailsRequest;
import com.mynt.deliveryparcel.dto.response.ParcelCostResponse;
import com.mynt.deliveryparcel.service.ParcelDeliveryService;
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
     * @param parcelDetailsRequest
     * @return parcel cost.
     */
    @Operation(
            description = "Retrieves total cost of parcel by weight and volume.",
            summary = "Retrieves total cost of parcel.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful execution.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ParcelCostResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error.",
                    content = @Content)
    })
    @PostMapping(value = "/cost", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParcelCostResponse> getParcelCost(@Valid @RequestBody ParcelDetailsRequest parcelDetailsRequest) {
        return new ResponseEntity<>(parcelService.computeParcelPrice(parcelDetailsRequest), HttpStatus.OK);
    }

}
