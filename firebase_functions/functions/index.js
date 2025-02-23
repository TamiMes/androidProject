const functions = require("firebase-functions");
const nodemailer = require("nodemailer");

const gmailUsername = "merchandisingverse@gmail.com"
const gmailAppToken = "pcoc yuid apwj zusz";
const PDFDocument = require("pdfkit");


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
// Function to generate PDF for the receipt
const createPdf = (data) => {
  const doc = new PDFDocument();
  const stream = new Readable();
  stream._read = () => {}; // required for Readable stream

  // Set up PDF document
  doc.fontSize(12).text("Purchase Information\n\n");

  doc.text(`Payment Holder: ${data.paymentHolder}`);
  doc.text(`ID: ${data.id}`);
  doc.text(`Card Number: ${data.cardNumber}`);
  doc.text(`CVV: ${data.cvv}`);
  doc.text(`Email: ${data.email}`);

  // Push PDF data to the stream
  doc.pipe(stream);
  doc.end();
  
  return stream;
};

// Cloud Function to send email with attached PDF receipt
exports.sendReceipt = functions.https.onCall(async (data, context) => {
  const { userEmail, paymentHolder, id, cardNumber, cvv, email } = data;

  const mailOptions = {
    to: userEmail,
    from: gmailUsername,
    subject: "Your Purchase Receipt",
    text: `Thank you for your purchase! Please find attached the receipt with your details.`,
    attachments: [
      {
        filename: 'receipt.pdf',
        content: createPdf({ paymentHolder, id, cardNumber, cvv, email }),
        contentType: 'application/pdf',
      },
    ],
  };

  try {
    await transporter.sendReceipt(mailOptions);
    return { success: true, message: "Email sent successfully with PDF attachment" };
  } catch (error) {
    return { success: false, message: error.toString() };
  }
});