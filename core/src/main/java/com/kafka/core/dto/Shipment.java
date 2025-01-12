package com.kafka.core.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {
    private UUID id;
    private UUID orderId;
    private UUID paymentId;
}
