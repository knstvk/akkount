/**
 * Created by krivopustov on 26.02.14.
 */
(function() {
    "use strict";

    app.OperationTableView = Backbone.View.extend({
        initialize: function(options){
            this.operations = options.operations;
            this.accounts = new app.AccountsCollection();
            this.operations.bind("reset", this.addAll, this);
        },

        render: function() {
            var self = this;
            this.operations.fetch({
                success: function() {
                    self.$el.html(_.template($('#operation-table-template').html(), app.session));
                    self.addAll();
                    self.accounts.fetch();
                },
                error: function(collection, response, options) {
                    app.log("Error loading operations: " + response.status);
                    if (response.status == 401) {
                        window.location.hash = "#login";
                    }
                }
            });
            return this;
        },

        addAll: function () {
            this.$el.find('tbody').children().remove();
            _.each(this.operations.models, $.proxy(this, "addOne"));
        },

        addOne: function(operation){
            var view = new app.OperationRowView({
                operations: this.operations,
                operation: operation,
                accounts: this.accounts
            });
            this.$el.find("tbody").append(view.render().el);
        },

        addNew: function() {
            var view = new app.OperationRowView({
                operations: this.operations,
                operation: new app.OperationModel(),
                accounts: this.accounts
            });
            this.$el.find("tbody").prepend(view.render().el);
            view.editRow();
        }
    });
}());