<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body { font-family: Arial, sans-serif; line-height:  1.6; color: #333; }
        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
        .header { background: #10B981; color: white; padding: 30px; text-align: center; }
        .content { padding: 30px; background: #f9fafb; }
        .order-details { background: white; padding: 20px; margin: 20px 0; border-radius: 8px; }
        .item { padding: 10px; border-bottom: 1px solid #e5e7eb; }
        .total { font-size: 20px; font-weight: bold; color: #10B981; margin-top: 20px; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>✅ Order Confirmed!</h1>
    </div>
    <div class="content">
        <h2>Hi ${buyerName}!</h2>
        <p>Your order has been successfully placed and payment is being processed.</p>

        <div class="order-details">
            <h3>Order Details</h3>
            <p><strong>Order Number:</strong> ${orderNumber}</p>
            <p><strong>Order Date:</strong> ${orderDate}</p>

            <h4>Items: </h4>
            <#list items as item>
                <div class="item">
                    <strong>${item.productTitle}</strong><br>
                    Quantity:  ${item.quantity} × ₹${item.price} = ₹${item.subtotal}<br>
                    Seller: ${item.sellerName}
                </div>
            </#list>

            <div class="total">
                Total Amount: ₹${totalAmount}
            </div>
        </div>

        <h3>What Happens Next?</h3>
        <ol>
            <li>Seller will be notified about your order</li>
            <li>Seller will arrange a meeting location on campus</li>
            <li>You'll receive an OTP to verify the transaction</li>
            <li>Meet the seller, verify the product, and share the OTP</li>
            <li>Payment will be released to the seller</li>
        </ol>

        <p><strong>Note:</strong> Your payment is held securely until you verify the transaction with OTP.</p>

        <p>Track your order:  <a href="${orderUrl}">View Order Status</a></p>
    </div>
</div>
</body>
</html>