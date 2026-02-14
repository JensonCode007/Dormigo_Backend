<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
        .header { background: #F59E0B; color: white; padding: 30px; text-align: center; }
        .content { padding: 30px; background: #f9fafb; }
        .otp-box { background: #FEF3C7; border: 2px solid #F59E0B; padding: 30px; text-align: center; margin: 30px 0; border-radius: 10px; }
        .otp-code { font-size: 36px; font-weight: bold; letter-spacing: 8px; color: #D97706; }
        .warning { background: #FEE2E2; border-left: 4px solid #EF4444; padding: 15px; margin: 20px 0; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>🔐 Your OTP Code</h1>
    </div>
    <div class="content">
        <h2>Hi ${buyerName}!</h2>
        <p>The seller has arranged a meeting for your order <strong>${orderNumber}</strong>.</p>

        <div class="otp-box">
            <p>Your One-Time Password: </p>
            <div class="otp-code">${otpCode}</div>
            <p style="color: #D97706; margin-top: 15px;">Valid for 30 minutes</p>
        </div>

        <h3>Meeting Details: </h3>
        <p><strong>Location:</strong> ${meetingLocation}</p>
        <p><strong>Time:</strong> ${meetingTime}</p>
        <p><strong>Notes:</strong> ${meetingNotes}</p>

        <div class="warning">
            <strong>⚠️ Important:</strong>
            <ul style="margin:  10px 0;">
                <li>Only share this OTP with the seller AFTER verifying the product</li>
                <li>Once OTP is verified, payment will be released</li>
                <li>Do not share OTP via message - only in person</li>
            </ul>
        </div>

        <p>If you didn't request this OTP or have concerns, please contact us immediately.</p>
    </div>
</div>
</body>
</html>