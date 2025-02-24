const functions = require("firebase-functions");
const nodemailer = require("nodemailer");
const PDFDocument = require("pdfkit");

const gmailUsername = "merchandisingverse@gmail.com";
const gmailAppToken = "pcoc yuid apwj zusz";

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

// Function to generate PDF as a Buffer
const createPdf = ({ paymentHolder, id, cardNumber, cvv, email, totalCost, items }) => {
  return new Promise((resolve, reject) => {
    const doc = new PDFDocument();
    let buffers = [];

    doc.on("data", buffers.push.bind(buffers));
    doc.on("end", () => {
      const pdfBuffer = Buffer.concat(buffers);
      resolve(pdfBuffer);
    });
    doc.on("error", reject);

    doc.fontSize(16).text("Purchase Receipt", { align: "center" }).moveDown();
    doc.fontSize(12).text(`Payment Holder: ${paymentHolder}`);
    doc.text(`ID: ${id}`);
    doc.text(`Card Number: ${cardNumber}`);
    doc.text(`CVV: ${cvv}`);
    doc.text(`Email: ${email}`).moveDown();
    doc.fontSize(12).text("Purchased Items:").moveDown();

    // Add each item to the receipt
    Object.entries(items).forEach(([item, quantity]) => {
      doc.text(`${item}: ${quantity}`);
    });

    doc.moveDown();
    doc.fontSize(14).text(`Total Cost: $${totalCost.toFixed(2)}`);

    doc.end();
  });
};


// Cloud Function to send email with attached PDF receipt
exports.sendReceipt = functions.https.onCall(async (data, context) => {
  const { userEmail, paymentHolder, id, cardNumber, cvv, totalCost, items } = data.data;
  console.log(`Received data to send receipt. Data: ${JSON.stringify(data.data)}`);

  try {
    const pdfBuffer = await createPdf({ paymentHolder, id, cardNumber, cvv, email: userEmail, totalCost, items });

    const mailOptions = {
      to: userEmail,
      from: gmailUsername,
      subject: "Your Purchase Receipt",
      text: `Thank you for your purchase! Your total cost was $${totalCost.toFixed(2)}. Please find your receipt attached.`,
      attachments: [
        {
          filename: "receipt.pdf",
          content: pdfBuffer,
          contentType: "application/pdf",
        },
      ],
    };

    await transporter.sendMail(mailOptions);
    return { success: true, message: "Email sent successfully with PDF attachment" };
  } catch (error) {
    console.error("Error sending email with receipt:", error);
    return { success: false, message: error.toString() };
  }
});