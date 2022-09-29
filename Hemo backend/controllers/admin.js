const mongoose = require("mongoose");
const createError = require("http-errors");
const Schedule = require("../model/schedule");
const Blood = require("../model/Blood");
const notif = require("../notification/notification");
const nodemailer = require("nodemailer");
const ScheduleTemplate = require("../template/schedule");
const User = require("../model/user");

//nodemailer transpoter
let transporter = nodemailer.createTransport({
  host: "smtp.gmail.com",
  service: "gmail",
  auth: {
    user: process.env.AUTH_EMAIL,
    pass: process.env.AUTH_PASS,
  },
});

// testing nodemailer
transporter.verify((error, success) => {
  if (error) {
    console.log(error);
  } else {
    console.log("Ready for messages");
    console.log(success);
  }
});

exports.get_all_schedule = async (req, res, next) => {
  try {
    const bank_id = req.query.bank_id;
    const pending = !!parseInt(req.query.pending);
    const schedules = await Schedule.find({ bank_id, pending, close: false});
    res.status(200).send(schedules);
  } catch (error) {
    next(error);
    return;
  }
};

exports.approve_schedule = async (req, res, next) => {
  try {
    const id = req.query.id;
    const approval = !!parseInt(req.query.approval);

    console.log(id, approval);

    const schedule = await Schedule.findOneAndUpdate(
      { _id: id },
      { approval, pending: false }
    );

    console.log(schedule);

    if (!schedule) {
      next(createError(404, "Schedule not found"));
      return;
    }

    const bank = schedule.bank;
    const date = schedule.date;
    const time = schedule.time;
    const user = schedule.user;

    const muser = await User.findOne({ _id: user });

    if (!muser) {
      next(createError(404, "User not found"));
      return;
    }

    const name = muser.name;
    const token = muser.token;
    const email = muser.email;

    console.log(email, name, bank, date, time, user);

    notif.send_notificaiton(
      token,
      "Appointment Scheduled",
      "Your blood can save someone's life"
    );
    sendVerificationEmail({ email, name, bank, date, time, user }, res);
  } catch (error) {
    next(error);
    return;
  }
};

exports.status_schedule = async (req, res, next) => {
  try {
    const id = req.query.id;
    const status = !!parseInt(req.query.status);
    await Schedule.updateOne({ _id: id }, { status, close: true });
    res.status(200).send({
      status: 200,
      message: "status updated",
    });
  } catch (error) {
    next(error);
    return;
  }
};

exports.get_all_requests = async (req, res, next) => {
  try {
    const location = req.query.location;
    const bloods = await Blood.find({ location, verified: false, status: true });
    res.status(200).send(bloods);
  } catch (error) {
    next(error);
    return;
  }
};

exports.verify_request = async (req, res, next) => {
  try {
    const _id = req.query.id;
    const verified = !!parseInt(req.query.verified);
    if (!verified) {
      await Blood.updateOne({_id}, { verified, status: false });
    } else {
      await Blood.updateOne({_id}, { verified });
    }
    res.status(200).send({
      status: 200,
      message: "verify updated",
    });
  } catch (error) {
    next(error);
    return;
  }
};

const sendVerificationEmail = (
  { email, name, bank, date, time, user },
  res,
  next
) => {
  //url to be used in the email
  var mailOptions = {
    from: process.env.AUTH_EMAIL,
    to: email,
    subject: "Email verification Hemo Dev334",
    html: ScheduleTemplate.exportEmail(name, user, date, time, bank),
  };
  transporter.sendMail(mailOptions, async function (error, response) {
    if (error) {
      console.log(error);
      //next(error);
      res.send({
        status: 404,
        message: "Error occured",
      });
      return;
    } else {
      res.status(200).send({
        status: 200,
        message: "approval updated",
      });
    }
  });
};
