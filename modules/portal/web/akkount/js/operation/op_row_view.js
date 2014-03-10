/**
 * Created by krivopustov on 01.03.14.
 */
(function() {
    "use strict";

    App.OperationRowView = Backbone.View.extend({
        tagName: "tr",

        events: {
            "click a.delete": "deleteRow"
        },

        initialize: function(options) {
            this.operation = options.operation;
            this.operations = options.operations;
        },

        render: function() {
            this.$el.html(_.template($("#operation-row-template").html(), this.operation.toJSON()));
            return this;
        },

        deleteRow: function() {

        }
    });
}());
