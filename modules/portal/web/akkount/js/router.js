/**
 * Created by krivopustov on 26.02.14.
 */
(function() {
    window.App = window.App || {};

    App.Router = Backbone.Router.extend({
        routes: {
            "login": "showLogin",
            "operations": "showOperations"
        },

        initialize: function(options) {
            this.operations = options.operations;
            this.showLogin();
        },

        showLogin: function() {
            this.currentView = new App.LoginView();
            $('#main').html(this.currentView.render().el);

        },

        showOperations: function() {
            if (App.session.id) {
                this.currentView = new App.OperationTableView({operations: this.operations});
                $('#main').html(this.currentView.render().el);
            } else {
                window.location.hash = "login";
            }
        }
    });
}());