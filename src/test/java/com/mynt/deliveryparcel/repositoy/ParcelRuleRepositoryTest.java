package com.mynt.deliveryparcel.repositoy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mynt.deliveryparcel.db.domain.ParcelDetails;
import com.mynt.deliveryparcel.db.repository.ParcelRuleRepository;
import com.mynt.deliveryparcel.enums.RuleName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ParcelRuleRepositoryTest {

    @Autowired
    private ParcelRuleRepository parcelRuleRepository;

    @Test
    void findByRuleName_ExistingRule_ShouldReturnRule() {

        RuleName ruleName = RuleName.SMALL_PARCEL;
        ParcelDetails parcelRule = ParcelDetails.builder()
                .ruleName(ruleName)
                .build();
        parcelRuleRepository.save(parcelRule);

        Optional<ParcelDetails> foundRule = parcelRuleRepository.findByRuleName(ruleName);

        assertTrue(foundRule.isPresent());
        assertEquals(ruleName, foundRule.get().getRuleName());
    }

    @Test
    void findByRuleName_NonExistingRule_ShouldReturnEmptyOptional() {
        RuleName ruleName = RuleName.SMALL_PARCEL;

        Optional<ParcelDetails> foundRule = parcelRuleRepository.findByRuleName(ruleName);

        assertTrue(foundRule.isEmpty());
    }
}