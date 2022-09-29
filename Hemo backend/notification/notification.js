var admin = require("firebase-admin");

var serviceAccount = require('./notificationPrivate.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

var options = {
  priority: "high",
  timeToLive: 60 * 60 * 24,
};

const send_notificaiton = (token, t, b) =>{

  var payload = {
    notification: {
      title: t,
      body: b,
    },
  };

  admin
  .messaging()
  .sendToDevice(token, payload, options)
  .then(function (response) {
    console.log("Successfull sent message to device", response);
  })
  .catch(function (error) {
    console.log("Error sending message to device: " + error);
  });
}

module.exports.send_notificaiton = send_notificaiton;
