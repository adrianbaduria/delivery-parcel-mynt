package com.mynt.deliveryparcel.db.repository;

import com.mynt.deliveryparcel.db.domain.ParcelDetails;
import com.mynt.deliveryparcel.enums.RuleName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ParcelRuleRepository extends CrudRepository<ParcelDetails, Long> {

    Optional<ParcelDetails> findByRuleName(RuleName ruleName);

}
