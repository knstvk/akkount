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
            var opType = this.operation.get("opType");

            if (opType == "E")
                template = $("#expense-edit-template").html();
            else if (opType == "I")
                template = $("#income-edit-template").html();
            else
                template = $("#transfer-edit-template").html();

            this.$el.html(_.template(template, this.operation.toJSON()));

            if (opType == "E" || opType == "T") {
                var acc1Select = new app.AccountSelectView(this.accounts).render().el;
                acc1Select.setAttribute("id", "acc1");
                acc1Select.value = this.operation.get("acc1").id;
                this.$el.find("div.account1-select").append(acc1Select);
            }
            if (opType == "I" ||  opType == "T") {
                var acc2Select = new app.AccountSelectView(this.accounts).render().el;
                acc2Select.setAttribute("id", "acc2");
                acc2Select.value = this.operation.get("acc2").id;
                this.$el.find("div.account2-select").append(acc2Select);
            }

            this.$el.find("#opDate").datepicker({ dateFormat: "dd/mm/yy" });

        },

        deleteRow: function() {
            this.$el.remove();
        },

        save: function() {
            this.operation.set({
                opDate: app.toServerDate(this.$el.find("#opDate").val()),
                comments: this.$el.find("#comments").val()
            });

            var acc1Select = this.$el.find("#acc1");
            if (acc1Select.length) {
                var acc1 = this.accounts.find(function(acc) {
                    return acc.id == acc1Select.val();
                });
                this.operation.set("acc1", acc1.toJSON());
            }

            var acc2Select = this.$el.find("#acc2");
            if (acc2Select.length) {
                var acc2 = this.accounts.find(function(acc) {
                    return acc.id == acc2Select.val();
                });
                this.operation.set("acc2", acc2.toJSON());
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
