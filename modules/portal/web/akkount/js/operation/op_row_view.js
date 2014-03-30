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
        },

        render: function() {
            this.$el.html(_.template($("#operation-row-template").html(), this.operation.toJSON()));
            return this;
        },

        editRow: function() {
            this.$el.empty();
            this.$el.html(_.template($("#operation-edit-template").html(), this.operation.toJSON()));
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
