package kr.wadiz.stripe_demo;

import jakarta.validation.constraints.NotNull;

public record SavePaymentMethodRequest(

    @NotNull
    String paymentMethodId
) {

}
