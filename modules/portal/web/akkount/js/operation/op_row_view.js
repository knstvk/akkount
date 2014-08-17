/**
 * Created by krivopustov on 01.03.14.
 */
(function() {
    "use strict";

    app.OperationRowView = Backbone.View.extend({
        tagName: "tr",

        events: {
            "click a.edit": "editRow",
            "click a.confirm-delete": "confirmDelete",
            "click a.delete": "deleteRow",
            "click a.save": "save",
            "click a.cancel": "cancel"
        },

        initialize: function(options) {
            this.operation = options.operation;
            this.operations = options.operations;
            this.accounts = options.accounts;
            this.categories = options.categories;
        },

        render: function() {
            this.$el.html(_.template($("#operation-row-template").html(), this.operation.toJSON()));
            return this;
        },

        editRow: function() {
            var self = this;
            var opType, template, acc1, acc2, acc1Select, acc2Select, cat, catSelect;

            this.$el.empty();

            opType = this.operation.get("opType");

            if (opType == "E")
                template = $("#expense-edit-template").html();
            else if (opType == "I")
                template = $("#income-edit-template").html();
            else
                template = $("#transfer-edit-template").html();

            this.$el.html(_.template(template, this.operation.toJSON()));

            if (opType == "E" || opType == "T") {
                acc1Select = new app.AccountSelectView(this.accounts).render().el;
                $(acc1Select).addClass("acc1");
                acc1 = this.operation.get("acc1");
                acc1Select.value = acc1.id;
                this.$el.find("div.account1-container").append(acc1Select);

                this.$el.find("div.currency1").html(this.accounts.currencyCodeByAccId(acc1.id));
                acc1Select.onchange = function() {
                    self.$el.find("div.currency1").html(self.accounts.currencyCodeByAccId(this.value));
                };
            }
            if (opType == "I" ||  opType == "T") {
                acc2Select = new app.AccountSelectView(this.accounts).render().el;
                $(acc2Select).addClass("acc2");
                acc2 = this.operation.get("acc2");
                acc2Select.value = acc2.id;
                this.$el.find("div.account2-container").append(acc2Select);

                this.$el.find("div.currency2").html(this.accounts.currencyCodeByAccId(acc2.id));
                acc2Select.onchange = function() {
                    self.$el.find("div.currency2").html(self.accounts.currencyCodeByAccId(this.value));
                };
            }

            if (opType == "E" || opType == "I") {
                catSelect = new app.CategorySelectView(this.categories, opType).render().el;
                $(catSelect).addClass("category");
                cat = this.operation.get("category");
                if (cat) {
                    catSelect.value = cat.id;
                }
                this.$el.find("div.category-container").append(catSelect);
            }

            this.$el.find("input.opDate").datepicker({ dateFormat: "dd/mm/yy" });

            this.$el.find("input.calc").on("blur", function(event) {
                var amount2Field;
                var x = event.target.value;
                if (x.match(/([-+]?[0-9]*\.?[0-9]+[\-\+\*\/])+([-+]?[0-9]*\.?[0-9]+)/)) {
                    event.target.value = eval(x);
                }
                if (opType == "T") {
                    if (event.target.className.indexOf("amount1") > -1) {
                        amount2Field = self.$el.find("input.amount2");
                        if (amount2Field.val() == "") {
                            amount2Field.val(event.target.value);
                        }
                    }
                }
            });
        },

        confirmDelete: function() {
            this.$el.find(".delete-cell").html($("#operation-delete-template").html());
        },

        deleteRow: function() {
            this.operations.remove(this.operation);
            this.operation.destroy();
            this.$el.remove();
        },

        save: function() {
            var acc1Select, acc2Select, amount1Field, amount2Field, catSelect;

            var date = this.$el.find("input.opDate").val();
            app.lastUsedDate = date;

            this.operation.set({
                opDate: app.toServerDate(date),
                comments: this.$el.find("textarea.comments").val()
            });

            acc1Select = this.$el.find(".acc1");
            if (acc1Select.length) {
                this.operation.set("acc1", this.accounts.byId(acc1Select.val()).toJSON());
            }

            acc2Select = this.$el.find(".acc2");
            if (acc2Select.length) {
                this.operation.set("acc2", this.accounts.byId(acc2Select.val()).toJSON());
            }

            amount1Field = this.$el.find("input.amount1");
            if (amount1Field.length) {
                this.operation.set("amount1", amount1Field.val());
            }

            amount2Field = this.$el.find("input.amount2");
            if (amount2Field.length) {
                this.operation.set("amount2", amount2Field.val());
            }

            catSelect = this.$el.find(".category");
            if (catSelect.length) {
                this.operation.set("category", this.categories.byId(catSelect.val()));
            }

            this.operation.save();
            this.$el.empty();
            this.render();
        },

        cancel: function() {
            if (this.operation.get("version")) { // not new
                this.$el.empty();
                this.render();
            } else {
                this.$el.remove();
            }
        }
    });
}());
