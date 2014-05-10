/**
 * Created by krivopustov on 26.02.14.
 */
(function() {
    "use strict";

    app.Router = Backbone.Router.extend({
        routes: {
            "login": "showLogin",
            "operations": "showOperations",
            "op/expense": "newExpense",
            "op/income": "newIncome",
            "op/transfer": "newTransfer"
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

        newExpense: function() {
            app.log("New expense");
            if (this.currentView instanceof app.OperationTableView) {
                var op = new app.OperationModel();
                op.set("opType", "E");
                op.set("acc1", this.currentView.accounts.first().toJSON());
                this.currentView.addNew(op);
            }
        },

        newIncome: function() {
            app.log("New income");
            if (this.currentView instanceof app.OperationTableView) {
                var op = new app.OperationModel();
                op.set("opType", "I");
                op.set("acc2", this.currentView.accounts.first().toJSON());
                this.currentView.addNew(op);
            }
        },

        newTransfer: function() {
            app.log("New transfer");
            if (this.currentView instanceof app.OperationTableView) {
                var op = new app.OperationModel();
                op.set("opType", "T");
                op.set("acc1", this.currentView.accounts.first().toJSON());
                op.set("acc2", this.currentView.accounts.first().toJSON());
                this.currentView.addNew(op);
            }
        }
    });
}());