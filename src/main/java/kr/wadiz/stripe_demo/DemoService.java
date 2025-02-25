package kr.wadiz.stripe_demo;

import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

  public Result savePaymentMethod(SavePaymentMethodRequest savePaymentMethodRequest) throws IllegalAccessException {
    try {
      String paymentMethodId = savePaymentMethodRequest.paymentMethodId();
      //String customerId = "your_customer_id_here"; 실제로는 고객 ID를 동적으로 받아야 한다.

      Customer customer = Customer.create(Map.of(
          "email", "junyeong.jang@wadiz.kr"
      ));

      // 결제 메서드를 고객과 연결
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
          .setOffSession(true)  // 고객이 실제로 참석하지 않는 결제
          .setConfirm(true)
          .build();

      PaymentIntent paymentIntent = PaymentIntent.create(params);

      // check your json response in Stripe Dashboard. or use this.
      // return Result.builder().msg(paymentIntent.toJson()).build();

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
      return Result.builder().msg("{\"status\":\"" + status + "\"}").build();

    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalAccessException(e.getMessage());
    }
  }
}
