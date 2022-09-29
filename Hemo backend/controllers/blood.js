const mongoose = require('mongoose')
const createError = require('http-errors');
const Blood = require('../model/Blood')
const {bloodValidation, scheduleValidation} = require("../validation");
const Schedule = require('../model/schedule');
const User = require('../model/user')



exports.blood_all_req = async (req, res, next)=>{
    try{
        var location=req.query.location
        var blood = req.query.blood

        if(blood==="A+"){
            const mblood = await Blood.find({location, blood: {$in: ['A+','AB+']}, verified: true, status: true})
            res.status(200).send(mblood);
        }else if (blood==="A-"){
            const mblood = await Blood.find({location, blood: {$in: ['A-','A+','AB+','AB-']}, verified: true, status: true})
            res.status(200).send(mblood);
        }else if(blood==="B+"){
            const mblood = await Blood.find({location, blood: {$in: ['B+','AB+']}, verified: true, status: true})
            res.status(200).send(mblood);
        }else if(blood==="B-"){
            const mblood = await Blood.find({location, blood: {$in: ['B-','B+','AB+', 'AB-']}, verified: true, status: true})
            res.status(200).send(mblood);
        }else if(blood==="O+"){
            const mblood = await Blood.find({location, blood: {$in: ['O+','A+','AB+']}, verified: true, status: true})
            res.status(200).send(mblood);
        }else if(blood==="O-"){
            const mblood = await Blood.find({location, verified: true, status: true})
            res.status(200).send(mblood);
        }else if(blood==="AB+"){
            const mblood = await Blood.find({location, blood: {$in: ['AB+']}, verified: true, status: true})
            res.status(200).send(mblood);
        }else if(blood==="AB-"){
            const mblood = await Blood.find({location, blood: {$in: ['AB+','AB-']}, verified: true, status: true})
            res.status(200).send(mblood);
        }else{
            next(createError(400, "Undefined blood type"))
        }
    }catch(error){
        next(error)
        return
    }
}

exports.blood_one_req = async (req, res, next)=>{
    try{
        const {id} = req.params
        const blood = await Blood.findOne({_id: id});
        if(blood){
            res.status(200).send(blood)
        }else{
            throw createError(404, 'Not found')
        }
    }catch(error){
        next(error)
        return;
    }
}

exports.blood_save_req = async (req, res, next)=>{
    const {valid, error} = bloodValidation(req.body)

    if(!valid){
        next(createError(400, error))
        return
    }
    try{
        const user_id = req.body.user;

        const user = await User.findOne({_id: user_id})

        if(!user){
            next(createError(404, "User not found"))
            return;
        }

        if(user.request){
            next(createError(450, "Request already open"))
            return;
        }

        const blood = new Blood(req.body);
        blood.name = user.name;
        blood.phone=user.phone;
        user.request = true;

        await user.save();
        await blood.save();

        res.status(200).send({
            status: 200,
            message: 'Saved'
        })
    }catch(error){
        next(error)
        return
    }
}

exports.blood_schedule = async (req, res, next)=>{

    console.log("schedule called")

    const {valid, error} = scheduleValidation(req.body)
    if(!valid){
        next(createError(400, error))
        return;
    }

    try{

        const user_id = req.body.user;

        const muser = await User.findOne({_id: user_id})

        if(!muser){
            next(createError(404, "User not found"))
            return;
        }

        if(muser.schedule){
            next(createError(450, "Schedule already open"))
            return;
        }

        const schedule = new Schedule(req.body)
        schedule.name=muser.name;
        schedule.phone=muser.phone;

        muser.schedule=true;
        await muser.save();
        await schedule.save();

        
        res.status(200).send({
          status: 200,
          message: "approval updated"
        })
        

    }catch(error){
        next(error)
        return
    }

}

exports.remove_request = async (req, res, next)=>{

    try{
        const request_id = req.query.request_id;
        const user_id = req.query.user_id;

        const user = await User.findOneAndUpdate({_id: user_id}, {request: false})
        if(!user){
            next(createError(404, "User not found"))
            return;
        }

        const request = await Blood.findOneAndUpdate({_id: request_id}, {status: false})

        if(!request){
            next(createError(404, "Request not found"))
            return;
        }

        res.status(200).send({
            status: 200,
            message: "Removed successfully"
        })

    }catch(error){
        next(error)
    }

}

exports.remove_schedule = async (req, res, next)=>{

    try{
        const schedule_id = req.query.schedule_id;
        const user_id = req.query.user_id;

        const user = await User.findOneAndUpdate({_id: user_id}, {schedule: false})
        if(!user){
            next(createError(404, "User not found"))
            return;
        }

        const schedule = await Schedule.findOneAndUpdate({_id: schedule_id}, {close: true})

        if(!schedule){
            next(createError(404, "Schedule not found"))
            return;
        }

        res.status(200).send({
            status: 200,
            message: "Removed successfully"
        })

    }catch(error){
        next(error)
    }

}


exports.show_user_schedule = async (req, res, next)=>{
    try{
        const user_id=req.query.user_id;
        const schedule = await Schedule.findOne({user: user_id, close: false})
        if(schedule){
            res.send(schedule);
        }else{
            next(createError(404, "No schedule found"))
        }

    }catch(error){
        next(error)
    }
}

exports.show_user_request = async (req, res, next)=>{
    try{
        const user_id=req.query.user_id;
        const request = await Blood.findOne({user: user_id, status: true})

        if(request){
            res.send(request);
        }else{
            next(createError(404, "No request found"))
        }


    }catch(error){
        next(error)
    }
}