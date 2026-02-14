<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; line-height: 1.6; color: #374151; margin: 0; padding: 0; }
        .container { max-width: 600px; margin: 0 auto; border: 1px solid #e5e7eb; }
        .header { background: #10B981; color: white; padding: 30px; text-align: center; }
        .content {
            padding: 30px;
            background-color: #ffffff;
            background-image: url('https://res.cloudinary.com/dywfubilx/image/upload/v1765381755/cld-sample-2.jpg');
            background-size: cover;
        }
        .receipt-card {
            background: #f9fafb;
            border: 1px dashed #d1d5db;
            border-radius: 8px;
            padding: 20px;
            margin: 20px 0;
        }
        .item-row { display: flex; justify-content: space-between; margin-bottom: 10px; border-bottom: 1px solid #f3f4f6; padding-bottom: 8px; }
        .total-row { display: flex; justify-content: space-between; font-weight: bold; font-size: 18px; margin-top: 15px; color: #111827; }
        .status-badge {
            display: inline-block;
            background: #d1fae5;
            color: #065f46;
            padding: 4px 12px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: bold;
            text-transform: uppercase;
        }
        .button {
            display: inline-block;
            padding: 12px 30px;
            background: #10B981;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
            margin: 20px 0;
        }
        .next-steps { background: #eff6ff; padding: 15px; border-radius: 8px; border: 1px solid #bfdbfe; font-size: 14px; }
        .footer { text-align: center; padding: 20px; color: #9ca3af; font-size: 12px; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <div style="font-size: 40px; margin-bottom: 10px;">💸</div>
        <h1>Payment Confirmed!</h1>
    </div>

    <div class="content">
        <h2>Great news, ${firstName}!</h2>
        <p>Your payment for <strong>${itemName}</strong> was successful. The seller has been notified and your item is reserved.</p>

        <div class="receipt-card">
            <div style="margin-bottom: 15px;">
                <span class="status-badge">Paid</span>
                <span style="float: right; color: #6b7280; font-size: 12px;">Order ID: #${orderId}</span>
            </div>

            <div class="item-row">
                <span>${itemName}</span>
                <span>$${itemPrice}</span>
            </div>
            <div class="item-row">
                <span>Platform Fee</span>
                <span>₹${fee!"0.00"}</span>
            </div>

            <div class="total-row">
                <span>Total Paid</span>
                <span>₹${totalAmount}</span>
            </div>
        </div>

        <div class="next-steps">
            <strong>🤝 What's Next?</strong>
            <p style="margin: 5px 0;">Reach out to the seller to coordinate a safe pickup location on campus. Don't forget to verify the item before completing the exchange!</p>
        </div>

        <div style="text-align: center;">
            <a href="${orderUrl}" class="button">View Order Details</a>
        </div>

        <p style="font-size: 13px; color: #6b7280;">A copy of this receipt has been saved to your account profile under 'Purchase History'.</p>
    </div>

    <div class="footer">
        <p>&copy; 2025 Dormigo Marketplace. Secure Campus Trading.</p>
        <p>If you encounter any issues with this transaction, please <a href="${supportUrl}" style="color: #4F46E5;">contact support</a>.</p>
    </div>
</div>
</body>
</html>