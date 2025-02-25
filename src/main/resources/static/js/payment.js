document.addEventListener('DOMContentLoaded', function () {
  const stripe = Stripe('pk_test_51QvpsoPwOv85bxeOiuIstS44cDLwJCbTalFehOsOs1HokfI27M9jHrIGimBwVZOIPoaOM7YQrEDy9SVCAw3jB0BE00d2on4XWi');
  const elements = stripe.elements();
  const cardElement = elements.create('card');
  cardElement.mount('#card-element');

  document.querySelector('#payment-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const {paymentMethod, error} = await stripe.createPaymentMethod({
      type: 'card',
      card: cardElement,
    });

    if (error) {
      console.error(error.message);
    } else {
      console.log('Payment Method 생성 성공:', paymentMethod);
      // 서버에 Payment Method ID 전송
      fetch('/api/save-payment-method', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({paymentMethodId: paymentMethod.id}),
      }).then(response => response.json())
      .then(data => {
        console.log(data);
      }).catch(err => {
        console.error(err);
      });
    }
  });
});
