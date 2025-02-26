document.addEventListener('DOMContentLoaded', function () {
  const stripe = Stripe('pk_test_51QvpsoPwOv85bxeOiuIstS44cDLwJCbTalFehOsOs1HokfI27M9jHrIGimBwVZOIPoaOM7YQrEDy9SVCAw3jB0BE00d2on4XWi');  // 여기에 Publishable Key를 입력하세요
  const elements = stripe.elements();
  const cardElement = elements.create('card');
  cardElement.mount('#card-element');

  document.querySelector('#setup-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const response = await fetch('/api/create-setup-intent', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const {client_secret} = await response.json();

    console.log(client_secret);

    const {setupIntent, error} = await stripe.confirmCardSetup(
        client_secret,
        {
          payment_method: {
            card: cardElement,
          }
        }
    );

    if (error) {
      console.error(error.message);
    } else {
      console.log('Setup Intent 성공:', setupIntent);
      // 서버에 Payment Method ID 전송
      fetch('/api/save-payment-method', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({paymentMethodId: setupIntent.payment_method}),
      }).then(response => response.json())
      .then(data => {
        console.log(data);
      }).catch(err => {
        console.error(err);
      });
    }
  });
});
