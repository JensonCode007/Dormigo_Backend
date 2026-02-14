<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
        .header { background: #4F46E5; color: white; padding: 30px; text-align: center; }
        .content { padding: 30px; background: #f9fafb; }
        .button { display: inline-block; padding: 12px 30px; background: #4F46E5; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }
        .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
    </style>
</head>
<body>
<div class="container" >
    <div class="header">
        <h1>🎉 Welcome to Dormigo! </h1>
    </div>
    <div class="content"  style="background-image: -moz-linear-gradient(#A2C2BE, #FFFFEB)">
        <h2>Hi ${firstName}!</h2>
        <p>Welcome to Dormigo - your campus marketplace for buying and selling college essentials!</p>

        <p>Your account has been successfully created with email: <strong>${email}</strong></p>

        <h3>What's Next?</h3>
        <ul>
            <li>📚 Browse products from fellow students</li>
            <li>💰 List your items for sale</li>
            <li>🤝 Connect with buyers/sellers on campus</li>
            <li>✅ Complete transactions safely with OTP verification</li>
        </ul>

        <a href="${appUrl}" class="button">Start Shopping</a>

        <p>If you have any questions, feel free to reply to this email! </p>

        <p>Happy trading! <br>The Dormigo Team</p>
    </div>
    <div class="footer">
        <p>&copy; 2025 Dormigo. All rights reserved.</p>
        <p>This is an automated email, please do not reply.</p>
    </div>
</div>
</body>
</html>