/**
 * Created by krivopustov on 09.03.14.
 */

var SESSION_ID_KEY = "akkount.session.id";
var SESSION_USER_NAME_KEY = "akkount.session.userName";

window.app = {
    debug: true,

    lastUsedDate: undefined,

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
    },

    newOpDate: function() {
        return this.toServerDate(this.lastUsedDate ? this.lastUsedDate : new Date());
    },

    guid: function() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
            return v.toString(16);
        });
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

Backbone.Model.prototype.initialize = function() {
    if (!this.get("id"))
        this.set("id", this.constructor.entityName + "-" + app.guid());
};

Backbone.Model.prototype.parse = function(resp) {
    return app.cubaAPI.parse(resp);
};

Backbone.Collection.prototype.parse = function(resp) {
    return app.cubaAPI.parse(resp);
};

$(document).ready(function() {
    new app.Router();
    app.initBalanceDialog();
    Backbone.history.start();
});
