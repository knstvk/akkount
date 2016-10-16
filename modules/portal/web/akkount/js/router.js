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
            if (!this.checkReferences())
                return;
            var self = this;
            $.ajax({
                url: "rest/v2/services/akk_PortalService/getLastAccount?opType=opExpenseAccount",
                headers: {"Authorization": "Bearer " + app.session.id},
                success: function(json) {
                    var op = new app.OperationModel({opDate: app.newOpDate()});
                    op.set("opType", "EXPENSE");
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
            if (!this.checkReferences())
                return;
            if (this.currentView instanceof app.OperationTableView) {
                var self = this;
                $.ajax({
                    url: "rest/v2/services/akk_PortalService/getLastAccount?opType=opIncomeAccount",
                    headers: {"Authorization": "Bearer " + app.session.id},
                    success: function(json) {
                        var op = new app.OperationModel({opDate: app.newOpDate()});
                        op.set("opType", "INCOME");
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
            if (!this.checkReferences())
                return;
            if (this.currentView instanceof app.OperationTableView) {
                var self = this;
                $.when(
                    $.ajax({
                        url: "rest/v2/services/akk_PortalService/getLastAccount?opType=opTransferExpenseAccount",
                        headers: {"Authorization": "Bearer " + app.session.id}
                    }),
                    $.ajax({
                        url: "rest/v2/services/akk_PortalService/getLastAccount?opType=opTransferIncomeAccount",
                        headers: {"Authorization": "Bearer " + app.session.id}
                    }))
                    .done(function(res1, res2) {
                        var op = new app.OperationModel({opDate: app.newOpDate()});
                        op.set("opType", "TRANSFER");
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
            else if (!this.currentView.accounts.isEmpty())
                op.set(attr, this.currentView.accounts.first().toJSON());
        },

        checkReferences: function() {
            if (this.currentView.accounts.isEmpty() || this.currentView.categories.isEmpty()) {
                var opAlertsDiv = $("#op-alerts");
                opAlertsDiv.empty();
                opAlertsDiv.append("<div class='alert alert-warning'>Fill in accounts and categories first</div>");
                return false;
            } else
                return true;
        }

    });
}());