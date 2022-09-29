const mongoose = require("mongoose");

const bloodSchema = new mongoose.Schema({
  user: {
    type: String,
    required: true,
  },
  status: {
    type: Boolean,
    default: true,
  },
  created: {
    type: Date,
    default: Date.now(),
  },
  latitude: {
    type: Number,
    required: true,
  },
  longitude: {
    type: Number,
    required: true,
  },
  location: {
    type: String,
    required: true,
  },
  blood: {
    type: String,
    required: true,
  },
  quantity: {
    type: Number,
    required: true,
  },
  info: {
    type: String,
  },
  verified: {
    type: Boolean,
    default: false,
  },
  file: {
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

const Blood = mongoose.model("Blood", bloodSchema);
module.exports = Blood;
