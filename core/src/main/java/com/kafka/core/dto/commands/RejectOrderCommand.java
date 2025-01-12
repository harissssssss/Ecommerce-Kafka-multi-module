package com.kafka.core.dto.commands;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RejectOrderCommand {
    private UUID orderId;
}
