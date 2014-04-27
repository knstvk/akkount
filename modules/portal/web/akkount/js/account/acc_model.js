/**
 * Created by krivopustov on 27.04.2014.
 */
(function() {
    "use strict";

    app.AccountModel = Backbone.Model.extend();

    app.AccountsCollection = Backbone.Collection.extend({
        model: app.AccountModel,

        entityName: "akk$Account",
        jpqlQuery: "select a from akk$Account a order by a.name",
        maxResults: 100,
        view: "account-with-currency"
    });
}());