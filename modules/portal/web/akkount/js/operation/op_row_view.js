/**
 * Created by krivopustov on 01.03.14.
 */
(function() {
    "use strict";

    app.OperationRowView = Backbone.View.extend({
        tagName: "tr",

        events: {
            "click a.edit": "editRow",
            "click a.delete": "deleteRow",
            "click a.save": "save",
            "click a.cancel": "cancel"
        },

        initialize: function(options) {
            this.operation = options.operation;
            this.operations = options.operations;
            this.accounts = options.accounts;
        },

        render: function() {
            this.$el.html(_.template($("#operation-row-template").html(), this.operation.toJSON()));
            return this;
        },

        editRow: function() {
            this.$el.empty();

            var template;
            if (this.operation.get("opType") == "E")
                template = $("#expense-edit-template").html();
            else if (this.operation.get("opType") == "I")
                template = $("#income-edit-template").html();
            else
                template = $("#transfer-edit-template").html();

            this.$el.html(_.template(template, this.operation.toJSON()));
            this.$el.find("#opDate").datepicker({ dateFormat: "dd/mm/yy" });

            var view = new app.AccountSelectView(this.accounts);
            var select = view.render().el;
            select.setAttribute("id", "acc1");
            select.value = this.operation.get("acc1").id;
            this.$el.find("div.account-select").append(select);
        },

        deleteRow: function() {
            this.$el.remove();
        },

        save: function() {
            this.operation.set({
                opDate: app.toServerDate(this.$el.find("#opDate").val()),
                comments: this.$el.find("#comments").val()
            });
            var acc1 = this.$el.find("#acc1");
            if (acc1) {
                var acc = this.accounts.find(function(acc) {
                    return acc.id == acc1.val();
                });
                this.operation.set("acc1", acc.toJSON());
            }
            this.operation.save();
            this.$el.empty();
            this.render();
        },

        cancel: function() {
            this.$el.empty();
            this.render();
        }
    });
}());
