/**
 * Created by krivopustov on 09.03.14.
 */

var SESSION_ID_KEY = "akkount.session.id";
var SESSION_USER_NAME_KEY = "akkount.session.userName";

window.app = {
    debug: true,

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
    },

    log: function(obj) {
        if (this.debug) {
            console.log(obj);
        }
    },

    toDisplayDate: function (date) {
        if (_.isDate(date)) {
            return date.toISOString().slice(0, 10).split("-").reverse().join("/");
        } else {
            var parts = date.split("-");
            if (parts.length == 3) {
                return parts.reverse().join("/");
            } else if (date.indexOf("/") == 2) {
                return date;
            } else {
                this.log("Invalid date: " + date);
                return date;
            }
        }
    },

    toServerDate: function (date) {
        if (_.isDate(date)) {
            return date.toISOString().slice(0, 10);
        } else {
            var parts = date.split("/");
            if (parts.length == 3) {
                return parts.reverse().join("-");
            } else if (date.indexOf("-") == 4) {
                return date;
            } else {
                this.log("Invalid date: " + date);
                return date;
            }
        }
    }
};

Backbone.sync = function(method, model, options) {
    options || (options = {});

    switch (method) {
        case 'create':
            app.cubaAPI.create(model, options); 
            break;

        case 'update':
            app.cubaAPI.update(model, options); 
            break;

        case 'delete':
            app.cubaAPI.remove(model, options); 
            break;

        case 'read':
            if (model.id)
                app.cubaAPI.load(model, options); 
            else
                app.cubaAPI.loadList(model, options);
            break;
    }
};

$(document).ready(function() {
    var router = new app.Router();

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
