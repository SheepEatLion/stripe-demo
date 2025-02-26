package kr.wadiz.stripe_demo.model;

import lombok.Builder;

@Builder
public record SetupIntentResponse(
    String clientSecret
) {

}
