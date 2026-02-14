<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #1f2937; margin: 0; padding: 0; }
        .container { max-width: 600px; margin: 0 auto; border: 1px solid #e5e7eb; }
        .header { background: #4F46E5; color: white; padding: 25px; text-align: center; }
        .content {
            padding: 30px;
            background-color: #ffffff;
            background-image: url(''); /* Add your background image URL here */
            background-size: cover;
            background-position: center;
        }
        .info-box {
            background: #f3f4f6;
            border-radius: 8px;
            padding: 20px;
            margin: 20px 0;
            border-left: 4px solid #4F46E5;
        }
        .info-item { margin-bottom: 10px; font-size: 14px; }
        .info-label { font-weight: bold; color: #4b5563; }
        .button {
            display: inline-block;
            padding: 12px 25px;
            background: #ef4444;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
        }
        .footer { text-align: center; padding: 20px; color: #9ca3af; font-size: 12px; }
        .warning-text { color: #dc2626; font-size: 13px; margin-top: 20px; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h2>Security Alert: New Login</h2>
    </div>

    <div class="content">
        <p>Hi <strong>${firstName}</strong>,</p>
        <p>This is a quick notification to let you know that a new login was detected for your Dormigo account.</p>

        <div class="info-box">
            <div class="info-item">
                <span class="info-label">📅 Date & Time:</span> ${loginTime}
            </div>
            <div class="info-item">
                <span class="info-label">💻 Device/Browser:</span> ${deviceInfo}
            </div>

            <div class="info-item">
                <span class="info-label">🌐 IP Address:</span> ${ipAddress}
            </div>
        </div>

        <p>If this was you, you can safely ignore this email. You’re all set to continue trading!</p>

        <hr style="border: 0; border-top: 1px solid #e5e7eb; margin: 25px 0;">

        <p class="warning-text"><strong>Didn't recognize this activity?</strong></p>
        <p>If you did not log in, your account security may be compromised. Please secure your account immediately by clicking the button below:</p>


    </div>

    <div class="footer">
        <p>&copy; 2025 Dormigo. Protecting your campus trades.</p>
        <p>This is an automated security notification. If you need help, contact support@dormigo.com</p>
    </div>
</div>
</body>
</html>