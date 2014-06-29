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
            var self = this;
            $.ajax({
                url: "api/last-account?s=" + app.session.id + "&t=opExpenseAccount",
                success: function(json) {
                    var op = new app.OperationModel({opDate: app.newOpDate()});
                    op.set("opType", "E");
                    self.setAcc(json, op, "acc1");
                    self.currentView.addNew(op);
                },
                error: function(xhr, status) {
                    app.log("Error getting last account: " + status);
                }
            });
        },

        newIncome: function() {
            app.log("New income");
            if (this.currentView instanceof app.OperationTableView) {
                var self = this;
                $.ajax({
                    url: "api/last-account?s=" + app.session.id + "&t=opIncomeAccount",
                    success: function(json) {
                        var op = new app.OperationModel({opDate: app.newOpDate()});
                        op.set("opType", "I");
                        self.setAcc(json, op, "acc2");
                        self.currentView.addNew(op);
                    },
                    error: function(xhr, status) {
                        app.log("Error getting last account: " + status);
                    }
                });
            }
        },

        newTransfer: function() {
            app.log("New transfer");
            if (this.currentView instanceof app.OperationTableView) {
                var self = this;
                $.when(
                    $.get("api/last-account?s=" + app.session.id + "&t=opTransferExpenseAccount"),
                    $.get("api/last-account?s=" + app.session.id + "&t=opTransferIncomeAccount"))
                    .done(function(res1, res2) {
                        var op = new app.OperationModel({opDate: app.newOpDate()});
                        op.set("opType", "T");
                        self.setAcc(res1[0], op, "acc1");
                        self.setAcc(res2[0], op, "acc2");
                        self.currentView.addNew(op);
                    })
                    .fail(function(xhr, status) {
                        app.log("Error getting last account: " + status);
                    });
            }
        },

        setAcc: function (acc, op, attr) {
            var accModel;
            if (acc.id)
                accModel = this.currentView.accounts.byId(app.AccountModel.entityName + "-" + acc.id);
            if (accModel)
                op.set(attr, accModel.toJSON());
            else
                op.set(attr, this.currentView.accounts.first().toJSON());
        }

    });
}());