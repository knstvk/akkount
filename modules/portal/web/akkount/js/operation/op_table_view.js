/**
 * Created by krivopustov on 26.02.14.
 */
(function() {
    "use strict";

    App.OperationTableView = Backbone.View.extend({
        initialize: function(options){
            this.operations = options.operations;
            this.operations.bind("reset", this.addAll, this);
        },

        render: function(){
            this.$el.html($('#operation-table-template').html());
            this.addAll();
            return this;
        },

        addAll: function () {
            this.$el.find('tbody').children().remove();
            _.each(this.operations.models, $.proxy(this, "addOne"));
        },

        addOne: function(operation){
            var view = new App.OperationRowView({operations: this.operations, operation: operation});
            this.$el.find("tbody").append(view.render().el);
        }
    });
}());