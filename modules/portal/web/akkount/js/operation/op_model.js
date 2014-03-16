/**
 * Created by krivopustov on 26.02.14.
 */
(function() {
    "use strict";

    app.OperationModel = Backbone.Model.extend({
       defaults: {
           id: "",
           date: "",
           comments: ""
       }
    });

    app.OperationsCollection = Backbone.Collection.extend({
        model: app.OperationModel,

        url: function() {
            return "api/query.json?s=" + app.session.id
                + "&e=akk$Operation&q=" + encodeURIComponent("select o from akk$Operation o order by o.opDate desc")
                + "&max=100"
                + "&view=operation-browse";
        }
    });
}());