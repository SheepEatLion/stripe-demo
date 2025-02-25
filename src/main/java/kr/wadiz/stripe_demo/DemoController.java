package kr.wadiz.stripe_demo;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NON_AUTHORITATIVE_INFORMATION;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DemoController {

  private final DemoService demoService;

  @PostMapping("/save-payment-method")
  public ResponseEntity<String> savePaymentMethod(@RequestBody @Valid SavePaymentMethodRequest request) {
    try {
      Result result = demoService.savePaymentMethod(request);
      return ResponseEntity.ok(result.msg());

    } catch (IllegalAccessException e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

  @PostMapping("/process-payment")
  public ResponseEntity<String> processPayment(@RequestBody @Valid ProcessPaymentRequest request) {
    try {
      Result result = demoService.processPayment(request);

      if (result.msg().equals("succeeded")) {
        return ResponseEntity.ok(result.msg());
      }
      return ResponseEntity.status(NON_AUTHORITATIVE_INFORMATION).body(result.msg());

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }
}
