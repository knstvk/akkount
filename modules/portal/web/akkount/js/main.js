/**
 * Created by krivopustov on 09.03.14.
 */

var SESSION_ID_KEY = "akkount.session.id";
var SESSION_USER_NAME_KEY = "akkount.session.userName";

window.app = {
    debug: true,

    log: function(obj) {
        if (this.debug) {
            console.log(obj);
        }
    },

    session: {
        id: localStorage.getItem(SESSION_ID_KEY),
        login: localStorage.getItem(SESSION_USER_NAME_KEY)
    },

    setSession: function(id, login) {
        this.session.id = id;
        this.session.login = login;
        localStorage.setItem(SESSION_ID_KEY, this.session.id);
        localStorage.setItem(SESSION_USER_NAME_KEY, this.session.login);

        this.log(this.session);
    }
};

//Backbone.sync = function(method, model, options) {
//    options || (options = {});
//
//    switch (method) {
//        case 'create':
//            break;
//
//        case 'update':
//            break;
//
//        case 'delete':
//            break;
//
//        case 'read':
//            //app.log(method)
//            $.ajax({
//                url: model.url(),
//                type: "GET",
//                success: function(json) {
//                    app.log("Success: " + json);
//                    options.success(json);
//                },
//                error: function(xhr, status) {
//                    app.log("Error: " + status);
//                    options.error(xhr, status);
//                }
//            });
//            break;
//    }
//};


$(document).ready(function() {
    var operations = new app.OperationsCollection();
    var router = new app.Router({operations: operations});

    Backbone.history.start();

//    operations.reset([
//        {
//            "id": "7BB288A2-5B7B-4266-A770-7929221FE12E",
//            "date": "2014-01-01",
//            "comments": "op1"
//        },
//        {
//            "id": "9A12A229-1A18-46E9-A16C-03C256284AF2",
//            "date": "2014-01-02",
//            "comments": "op2"
//        }
//    ]);
});
