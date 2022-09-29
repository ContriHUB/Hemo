const Ajv = require("ajv");
const addFormats = require("ajv-formats");
const ajv = new Ajv();
addFormats(ajv);

const registerValidation = (body) => {
  const schema = {
    type: "object",
    properties: {
      name: {
        type: "string",
        minLength: 4,
        maxLength: 255,
      },
      email: {
        type: "string",
        minLength: 6,
        maxLength: 255,
        format: "email",
      },
      password: {
        type: "string",
        minLength: 6,
        maxLength: 1024,
      },
      date: {
        type: "string",
        format: "date",
      },
    },
    required: ["name", "email", "password"],
  };
  const valid = ajv.validate(schema, body);
  var error = ajv.errors;
  if (!valid) {
    error = ajv.errors[0].message;
  }

  return {
    valid,
    error,
  };
};

const loginValidation = (body) => {
  const schema = {
    type: "object",
    properties: {
      email: {
        type: "string",
        minLength: 6,
        maxLength: 255,
        format: "email",
      },
      password: {
        type: "string",
        minLength: 6,
        maxLength: 1024,
      },
      token: {
        type: "string"
      }
    },
    required: ["email", "password", "token"],
  };
  const valid = ajv.validate(schema, body);
  var error = ajv.errors;
  if (!valid) {
    error = ajv.errors[0].message;
  }

  return {
    valid,
    error,
  };
};

const profileValidation = (body) => {
  const schema = {
    type: "object",
    properties: {
      dob: {
        type: "string",
      },
      location: {
        type: "string",
      },
      weight: {
        type: "integer",
      },
      gender: {
        type: "string",
      },
      blood: {
        type: "string",
      },
      phone: {
        type: "string",
      }
    },
    required: ["dob", "location", "weight", "gender", "blood", "phone"],
  };
  const valid = ajv.validate(schema, body);
  var error = ajv.errors;
  if (!valid) {
    console.log(ajv.errors);
    error = ajv.errors[0].message;
  }

  return {
    valid,
    error,
  };
};

const bloodValidation = (body) => {
  const schema = {
    type: "object",
    properties: {
      latitude: {
        type: "number",
      },
      longitude: {
        type: "number",
      },
      location: {
        type: "string",
      },
      blood: {
        type: "string",
      },
      quantity: {
        type: "integer",
      },
      user: {
        type: "string",
      },
      status: {
        type: "boolean",
      },
      info: {
        type: "string",
      },
      file: {
        type: "string",
      },
    },
    required: [
      "latitude",
      "longitude",
      "location",
      "quantity",
      "blood",
      "user",
      "file",
    ],
  };
  const valid = ajv.validate(schema, body);
  var error = ajv.errors;
  if (!valid) {
    error = ajv.errors[0].message;
  }

  return {
    valid,
    error,
  };
};

const scheduleValidation = (body) => {
  const schema = {
    type: "object",
    properties: {
      bank: {
        type: "string",
      },
      bank_id: {
        type: "string",
      },
      date: {
        type: "string",
      },
      user: {
        type: "string",
      },
      time: {
        type: "string",
      }
    },
    required: [
      "bank",
      "bank_id",
      "date",
      "user",
      "time",
    ],
  };
  const valid = ajv.validate(schema, body);
  var error = ajv.errors;
  if (!valid) {
    error = ajv.errors[0].message;
  }

  return {
    valid,
    error,
  };
};



module.exports.profileValidation = profileValidation;
module.exports.registerValidation = registerValidation;
module.exports.loginValidation = loginValidation;
module.exports.bloodValidation = bloodValidation;
module.exports.scheduleValidation = scheduleValidation;
