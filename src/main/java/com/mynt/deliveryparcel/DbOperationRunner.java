package com.mynt.deliveryparcel;

import com.mynt.deliveryparcel.db.domain.ParcelDetails;
import com.mynt.deliveryparcel.db.repository.ParcelRuleRepository;
import com.mynt.deliveryparcel.enums.RuleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This component is to insert value from the ParcelRules table for testing.
 */
@Component
public class DbOperationRunner implements CommandLineRunner {

    @Autowired
    private ParcelRuleRepository parcelRuleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<ParcelDetails> parcelRules = List.of(
                new ParcelDetails(1L, RuleName.HEAVY_PARCEL, 0F ,20.00),
                new ParcelDetails( 2L, RuleName.SMALL_PARCEL, 0.03f, 0.03),
                new ParcelDetails( 3L, RuleName.MEDIUM_PARCEL, 0.04f, 0.04),
                new ParcelDetails( 4L, RuleName.LARGE_PARCEL, 0.05f, 0.05)
        );

        parcelRuleRepository.saveAll(parcelRules);
    }
}
