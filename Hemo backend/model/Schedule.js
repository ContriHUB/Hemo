const mongoose = require("mongoose");

const scheduleSchema = new mongoose.Schema({
  user: {
    type: String,
    required: true,
  },
  close: {
    type: Boolean,
    default: false
  },
  status: {
    type: Boolean,
    default: false,
  },
  approval: {
    type: Boolean,
    default: false
  },
  pending: {
    type: Boolean,
    default: true
  },
  created: {
    type: Date,
    default: Date.now(),
  },
  date: {
    type: String,
    required: true,
  },
  time: {
    type: String,
    required: true,
  },
  bank: {
    type: String,
    required: true,
  },
  bank_id: {
    type: String,
    required: true,
  },
  name: {
    type: String
  },
  phone: {
    type: String
  }
});

const Schedule = mongoose.model("Schedule", scheduleSchema);
module.exports = Schedule;
