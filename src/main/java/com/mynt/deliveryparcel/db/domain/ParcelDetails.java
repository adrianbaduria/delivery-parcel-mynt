package com.mynt.deliveryparcel.db.domain;

import com.mynt.deliveryparcel.enums.RuleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ParcelDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated
    @Column(name = "rule_name", unique = true)
    private RuleName ruleName;

    @Column(name = "condition")
    private Float condition;

    @Column(name = "cost")
    private Float cost;

}
