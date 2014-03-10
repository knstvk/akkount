/**
 * Created by krivopustov on 26.02.14.
 */
(function() {
    "use strict";

    App.OperationModel = Backbone.Model.extend({
       defaults: {
           id: "",
           date: "",
           comments: ""
       }
    });

    App.OperationsCollection = Backbone.Collection.extend({
       model: App.OperationModel
    });
}());