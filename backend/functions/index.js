/*
 * Functions SDK : is required to work with firebase functions.
 * Admin SDK : is required to send Notification using functions.
 */

'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendFriendNotification = functions.database.ref('/NOTIFICATIONS/{user_id}/{notification_id}').onWrite(event => {

    const user_id = event.params.user_id;
    const notification_id = event.params.notification_id;

    console.log('We have a notification for : ', user_id);

    if (!event.data.val()) {

        console.log('A Notification has been deleted from the database : ', notification_id);

        return 0;
    }

    const fromUser = admin.database().ref(`/NOTIFICATIONS/${user_id}/${notification_id}`).once('value');

    return fromUser.then(fromUserResult => {

        const from_user_id = fromUserResult.val().from;

        console.log('You have new notification from  : ', from_user_id);

        const userQuery = admin.database().ref(`USERS/${from_user_id}/displayName`).once('value');
        const deviceToken = admin.database().ref(`/USERS/${user_id}/msgToken`).once('value');

        return Promise.all([userQuery, deviceToken]).then(result => {

            const userName = result[0].val();
            const token_id = result[1].val();

            const payload = {
                notification: {
                    title: "New Friend Request",
                    body: `${userName} has sent you request`,
                    icon: "default",
                    click_action: ".ui.Profile.ProfileActivity"
                },
                data: {
                    from_user_id : from_user_id
                }
            };

            return admin.messaging().sendToDevice(token_id, payload).then(response => {

                console.log('This was the notification Feature, Response: ' + response);

            });

        });

    });

});