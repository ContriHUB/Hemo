const router = require('express').Router();
const verify = require('../middleware/verify-token')

const AdminController = require('../controllers/admin')

//get all schedule of a particular blood bank
router.get('/schedule', verify, AdminController.get_all_schedule)

//blood donated or not
router.patch('/schedule/status', verify, AdminController.status_schedule)

//whether blood bank has approvced or not
router.patch('/schedule/approval', verify, AdminController.approve_schedule)

//get all request of a particular location
router.get('/request', verify, AdminController.get_all_requests)

//verify blood request
router.patch('/request/verify', verify, AdminController.verify_request)

module.exports=router