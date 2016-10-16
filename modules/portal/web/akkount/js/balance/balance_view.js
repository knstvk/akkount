(function() {
    "use strict";

    app.initBalanceDialog = function() {
        $("#balance-dialog").on("shown.bs.modal", function (e) {
            var view = new app.BalanceView();
            $(this).find(".modal-body").html(view.render().el);
        });
        $("#balance-dialog").on("hidden.bs.modal", function (e) {
            var view = new app.BalanceView();
            $(this).find(".modal-body").empty();
        });
    };

    app.BalanceView = Backbone.View.extend({
        tagName: "div",

        render: function() {
            var self = this;
            $.ajax({
                url: "rest/v2/services/akk_PortalService/getBalance",
                headers: {"Authorization": "Bearer " + app.session.id},
                type: "GET",
                success: function(json) {
                    self.$el.html(_.template($("#balance-template").html(), { balance: json }));
                },
                error: function(xhr, status) {
                    app.log("Error getting balance: " + status);
                }
            });
            return this;
        }
    });
}());
