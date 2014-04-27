/**
 * Created by krivopustov on 27.04.2014.
 */
(function() {
   app.AccountSelectView = Backbone.View.extend({
       tagName: "select",
       className: "form-control",

       initialize: function(accounts) {
           this.accounts = accounts;
       },

       render: function() {
           this.$el.html(_.template($("#account-select-template").html(), { accounts: this.accounts.toJSON() }));
           return this;
       }
   });
}());