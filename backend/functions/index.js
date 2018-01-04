'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/NOTIFICATIONS/{user_id}/{notification_id}').onWrite(event => {

    const user_id = event.params.user_id;
    const notification_id = event.params.notification_id;

    console.log('We have a notification for : ', user_id);

    // if (!event.data.val()) {
    //
    //     return console.log('A Notification has been deleted from the database : ', notification_id);
    //
    // }


    return notification_id;

});