package com.zufar.icedlatte.payment.api.scenario;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.zufar.icedlatte.email.sender.PaymentEmailConfirmation;
import com.zufar.icedlatte.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.zufar.icedlatte.payment.enums.PaymentStatus.PAYMENT_IS_SUCCEEDED;

/**
 * This class is responsible for handling the successful scenario and updating
 * in database record of payment, with the relevant status and description
 * */

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentSuccessfulScenarioExecutor implements PaymentScenarioExecutor {

    private final PaymentRepository paymentRepository;

    private final PaymentEmailConfirmation paymentEmailConfirmation;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void execute(PaymentIntent paymentIntent) {
        log.info("Handle payment scenario method: start of handling payment intent: {} by successful scenario.", paymentIntent);
        paymentRepository.updateStatusAndDescriptionInPayment(paymentIntent.getId(), PAYMENT_IS_SUCCEEDED.toString(), PAYMENT_IS_SUCCEEDED.getDescription());
        paymentEmailConfirmation.send(paymentIntent);
        log.info("Handle payment scenario method: finish of handling payment intent: {} by successful scenario.", paymentIntent);
    }

    @Override
    public boolean supports(Event event) {return Objects.equals(PAYMENT_IS_SUCCEEDED.getStatus(), event.getType());}
}
