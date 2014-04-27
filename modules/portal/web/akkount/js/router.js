/**
 * Created by krivopustov on 26.02.14.
 */
(function() {
    "use strict";

    app.Router = Backbone.Router.extend({
        routes: {
            "login": "showLogin",
            "operations": "showOperations",
            "op/new": "newOperation"
        },

        initialize: function() {
            this.operations = new app.OperationsCollection();
            this.showOperations();
        },

        showLogin: function() {
            app.session = {};
            this.currentView = new app.LoginView();
            $('#main').html(this.currentView.render().el);

        },

        showOperations: function() {
            if (app.session.id) {
                this.currentView = new app.OperationTableView({operations: this.operations});
                $('#main').html(this.currentView.render().el);
            } else {
                window.location.hash = "login";
            }
        },

        newOperation: function() {
            app.log("New operation");
            if (this.currentView instanceof app.OperationTableView) {
                this.currentView.addNew();
            }
        }
    });
}());