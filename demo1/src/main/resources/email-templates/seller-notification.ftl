<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
        .header { background: #8B5CF6; color: white; padding: 30px; text-align: center; }
        .content { padding: 30px; background: #f9fafb; }
        .highlight { background: #FEF3C7; padding: 20px; border-left: 4px solid #F59E0B; margin: 20px 0; }
        .button { display: inline-block; padding: 12px 30px; background: #8B5CF6; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>🔔 New Order Received!</h1>
    </div>
    <div class="content">
        <h2>Hi ${sellerName}!</h2>
        <p>Great news! You have a new order.</p>

        <div class="highlight">
            <h3>Order Details</h3>
            <p><strong>Order Number:</strong> ${orderNumber}</p>
            <p><strong>Buyer:</strong> ${buyerName}</p>
            <p><strong>Items:</strong></p>
            <ul>
                <#list items as item>
                    <li>${item.productTitle} - Qty: ${item.quantity} - ₹${item.subtotal}</li>
                </#list>
            </ul>
            <p style="font-size: 18px; font-weight: bold; color: #8B5CF6;">Total: ₹${totalAmount}</p>
        </div>

        <h3>Next Steps:</h3>
        <ol>
            <li>Contact the buyer to arrange a meeting location</li>
            <li>Set the meeting details in your dashboard</li>
            <li>Meet the buyer at the scheduled time</li>
            <li>Get the OTP from the buyer and verify the handover</li>
            <li>Payment will be released to you after verification 💰</li>
        </ol>

        <a href="${orderUrl}" class="button">View Order Details</a>

        <p><strong>Note:</strong> Payment is held in escrow until buyer confirms receiving the product.</p>
    </div>
</div>
</body>
</html>