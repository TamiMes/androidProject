const functions = require("firebase-functions");
const nodemailer = require("nodemailer");

const gmailUsername = "merchandisingverse@gmail.com"
const gmailAppToken = "pcoc yuid apwj zusz"

const transporter = nodemailer.createTransport({
  service: "gmail",
  auth: {
    user: gmailUsername,
    pass: gmailAppToken,
  },
});

// Cloud Function to send email
exports.sendEmail = functions.https.onCall(async (data, context) => {
  const { from, subject, text } = data.data;

  const mailOptions = {
    to: gmailUsername,
    from: from,
    subject: subject,
    text: text,
  };

  try {
    await transporter.sendMail(mailOptions);
    return { success: true, message: "Email sent successfully" };
  } catch (error) {
    return { success: false, message: error.toString() };
  }
});
