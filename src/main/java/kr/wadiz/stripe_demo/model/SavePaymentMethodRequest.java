package kr.wadiz.stripe_demo.model;

import jakarta.validation.constraints.NotNull;

public record SavePaymentMethodRequest(

    @NotNull
    String paymentMethodId
) {

}
