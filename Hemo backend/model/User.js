const mongoose= require('mongoose')

const userSchema = new mongoose.Schema({
    name:{
        type: String,
        required: true,
        min: 6,
        max: 255
    },
    email: {
        type: String,
        required: true,
        min: 6,
        max: 255
    },
    password: {
        type: String,
        required: true,
        max: 1024,
        min: 6
    }, 
    date: {
        type: Date,
        default: Date.now()
    },
    verified: {
        type: Boolean,
        default: false
    },
    location: {
        type: String
    },
    dob:{
        type: String
    },
    last:{
        type: Date
    },
    blood:{
        type: String,
        enum: ['AB+', 'AB-','A+','A-','B+','B-','O+', 'O-']
    },
    gender: {
        type: String,
        enum: ['Male', 'Female', 'Other']
    },
    weight: {
        type: Number,
        min: 45
    },
    token: {
        type: String,
    },
    phone: {
        type: String
    },
    request: {
        type: Boolean,
        default: false
    },
    schedule: {
        type: Boolean,
        default: false
    }

})

const User= mongoose.model('User', userSchema);
module.exports=User;