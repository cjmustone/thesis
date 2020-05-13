const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
const ref= admin.database().ref()



exports.onNewUser = functions.database.ref('messages/{pushId}/')
    .onCreate(event => {

        let uid = event.val().uid;
        
        console.log("evntVal: ", uid);
        return getUser(uid).then(token =>{
            let payload = {
                notification: {
                    title: 'ALERT',
                    body: 'DEAUTH DETECTED'
                }
            };
            return admin.messaging().sendToDevice(token,payload).then(()=>{
                event.ref.remove();
                console.log("event should beremoved");
                return 1;
            }).catch(error =>{
                console.log("error with sending to device");
        });
    });
});



//return admin.messaging().sendToDevice(token,payload)
function getUser(uid){
    console.log("getUser fired");
    let dbRef = admin.database().ref('/users');
    let defer = new Promise((resolve, reject) => {
        dbRef.once('value', (snap) => {
            var token = snap.child(uid).val().token 
            resolve(token);        
        }, (err) => {
            reject(err);
        });
    });
    return defer;
}

exports.newAlert = functions.database.ref('/users/{uuid}')
    .onCreate((snapshot, context) => {

        const auCont = context.params.uuid;
        //const auAlert = event.data.val();

        const snapVal = snapshot.val();
        //const alertVal = snapshot.child("alert").val();
        
        console.log("snapVal: ", snapVal);
        console.log("uuid: ", auCont);
        //console.log("alert: ", auAlert);

        const payload = {
            notification: {
                title: 'ALERT',
                body: 'DEAUTH DETECTED'
            }
        };

    //return admin.messaging().sendToDevice(device,payload)

});


exports.onNewInput = functions.database.ref('/users/{uuid}/alert/{alert}')
    .onCreate((snapshot, context) => {
        const auCont = context.params.uuid;
        const auAlert = context.params.alert;

        const snapVal = snapshot.val();
        const alertVal = snapshot.child("alert").val();
        //const tokenVal = context.params.uuid.child("token").val(); -> threw error
        const tokenVal = snapshot.child("token").val();

        console.log("snapVal: ", snapVal);
        console.log("uuid: ", auCont);
        console.log("auAlert: ", auAlert);
        console.log("alertVal: ", alertVal);
        console.log("tokenVal: ", tokenVal);




        const payload = {
            notification: {
                title: 'ALERT',
                body: 'DEAUTH DETECTED'
            }
        };

    //return admin.messaging().sendToDevice(device,payload)

});




exports.onToken = functions.database.ref('/users')
    .onCreate((snapshot, context) => {
      // Grab the current value of what was written to the Realtime Database.
      const token = snapshot.val();
      console.log('data: ', token);
      // You must return a Promise when performing asynchronous tasks inside a Functions such as
      // writing to the Firebase Realtime Database.
      // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
      return Promise;
    });

//
//    return admin.messaging().send(message)
//        .then(function(response){
//                console.log('notification sent success: ', response);
//        })
//        .catch(function(error){
//            console.log('notification failed: ', error);
//        });   
//});
