package kr.wadiz.stripe_demo;

import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.SetupIntentCreateParams;
import com.stripe.param.SetupIntentCreateParams.Usage;
import java.util.Map;
import kr.wadiz.stripe_demo.model.ProcessPaymentRequest;
import kr.wadiz.stripe_demo.model.Result;
import kr.wadiz.stripe_demo.model.SavePaymentMethodRequest;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

  public Result savePaymentMethod(SavePaymentMethodRequest savePaymentMethodRequest) throws IllegalAccessException {
    try {
      String paymentMethodId = savePaymentMethodRequest.paymentMethodId();

      Customer customer = Customer.create(Map.of(
          "email", "junyeong.jang@wadiz.kr"
      ));

      PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
      PaymentMethodAttachParams attachParams = PaymentMethodAttachParams.builder()
          .setCustomer(customer.getId())
          .build();
      paymentMethod.attach(attachParams);

      return Result.builder().msg("saved successfully").build();
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalAccessException(e.getMessage());
    }
  }

  public Result processPayment(ProcessPaymentRequest request) throws IllegalAccessException{
    try {
      String customerId = request.customerId();
      String paymentMethodId = request.paymentMethodId();
      int amount = request.amount();

      PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
          .setAmount((long) amount)
          .setCurrency("usd")
          .setCustomer(customerId)
          .setPaymentMethod(paymentMethodId)
          .setOffSession(true)
          .setConfirm(true)
          .build();

      PaymentIntent paymentIntent = PaymentIntent.create(params);

      return Result.builder().msg(paymentIntent.getStatus()).build();
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalAccessException(e.getMessage());
    }
  }

  public Result getPaymentIntentStatus(String paymentIntentId) throws IllegalAccessException{
    try {
      PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
      String status = paymentIntent.getStatus();
      return Result.builder().msg(status).build();

    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalAccessException(e.getMessage());
    }
  }

  public Result createSetupIntent() throws IllegalAccessException {
    try {
      SetupIntentCreateParams params = SetupIntentCreateParams.builder()
          .addPaymentMethodType("card")
          .setUsage(Usage.OFF_SESSION)
          .build();

      SetupIntent setupIntent = SetupIntent.create(params);

      return Result.builder().msg(setupIntent.getClientSecret()).build();
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalAccessException(e.getMessage());
    }
  }
}
