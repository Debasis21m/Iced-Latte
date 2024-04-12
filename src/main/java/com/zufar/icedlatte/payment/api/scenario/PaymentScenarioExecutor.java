package com.zufar.icedlatte.payment.api.scenario;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;

public interface PaymentScenarioExecutor {

    void execute(final PaymentIntent paymentIntent);

    boolean supports(final Event event);

}
