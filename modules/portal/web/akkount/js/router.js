/**
 * Created by krivopustov on 26.02.14.
 */
(function() {
    "use strict";

    app.Router = Backbone.Router.extend({
        routes: {
            "login": "showLogin",
            "operations": "showOperations"
        },

        initialize: function(options) {
            this.operations = options.operations;
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
        }
    });
}());