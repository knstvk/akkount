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
            "click a.ok": "commitChanges",
            "click a.cancel": "cancelChanges"
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

        },

        deleteRow: function() {
            this.$el.remove();
        },

        commitChanges: function() {
            this.$el.empty();
            this.render();
        },

        cancelChanges: function() {
            this.$el.empty();
            this.render();
        }
    });
}());
