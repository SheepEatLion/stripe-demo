package kr.wadiz.stripe_demo;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

  static {
    Stripe.apiKey = "";
  }

  @PostMapping("/save-payment-method")
  public ResponseEntity<String> savePaymentMethod(@RequestBody Map<String, Object> request) {
    try {
      String paymentMethodId = request.get("paymentMethodId").toString();
      //String customerId = "your_customer_id_here"; 실제로는 고객 ID를 동적으로 받아야 한다.

      Customer customer = Customer.create(Map.of(
          "email", "junyeong.jang@aaa.com"
      ));

      // 결제 메서드를 고객과 연결
      PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
      PaymentMethodAttachParams attachParams = PaymentMethodAttachParams.builder()
          .setCustomer(customer.getId())
          .build();
      paymentMethod.attach(attachParams);

      return ResponseEntity.ok("saved successfully -> customerId: " + customer.getId() + " paymentMethodId: " + paymentMethodId);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

  @PostMapping("/process-payment")
  public ResponseEntity<String> processPayment(@RequestBody Map<String, Object> request) {
    try {
      String customerId = request.get("customerId").toString();
      String paymentMethodId = request.get("paymentMethodId").toString();
      int amount = Integer.parseInt(request.get("amount").toString());

      PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
          .setAmount((long) amount)
          .setCurrency("usd")
          .setCustomer(customerId)
          .setPaymentMethod(paymentMethodId)
          .setOffSession(true)  // 고객이 실제로 참석하지 않는 결제
          .setConfirm(true)
          .build();

      PaymentIntent paymentIntent = PaymentIntent.create(params);

      return ResponseEntity.ok(paymentIntent.toJson());
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

}
