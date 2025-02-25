package kr.wadiz.stripe_demo;

import jakarta.validation.constraints.Min;

public record ProcessPaymentRequest(
    String customerId,
    String paymentMethodId,
    @Min(500) // it's cent, not dollar. should be greater than 500 cent (0.5 dollar)
    Integer amount
) {

}
