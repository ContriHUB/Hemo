const router = require('express').Router();
const verify = require('../middleware/verify-token')

const BloodController = require('../controllers/blood')


router.get('/req', verify, BloodController.blood_all_req)

router.post('/req', verify, BloodController.blood_save_req)

router.get('/req/:id', verify, BloodController.blood_one_req)

router.post('/schedule', verify, BloodController.blood_schedule)

router.patch('/remove/schedule', verify, BloodController.remove_schedule)

router.patch('/remove/request', verify, BloodController.remove_request)

router.get('/user/req', verify, BloodController.show_user_request)

router.get('/user/schedule', verify, BloodController.show_user_schedule)

module.exports=router