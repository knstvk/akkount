(function() {
    "use strict";

    App.LoginView = Backbone.View.extend({

        initialize: function() {
            _.bindAll(this, "render", "loginClick");
        },

        events: {
            "click #login-button":  "loginClick"
        },

        render: function() {
            this.$el.html($("#login-template").html());
            return this;
        },

        loginClick: function(e) {
            var self = this;
            e.preventDefault();

            $.ajax({
                async: false,
                url: "api/login",
                data: {
                    u: $("#login-name").val(),
                    p: $("#password").val(),
                    l: "en"
                },
                success: function(text) {
                    App.session.id = text;
                    App.session.login = $("#login-name").val();
                    console.log(App.session);

                    window.location.hash = "operations";
                },
                error: function(xhr, status) {
                    console.log(xhr, status);
                    if (xhr.status == 401) {
                        self.showAlert("Invalid user name or password");
                    } else {
                        self.showAlert("Error " + xhr.status + " " + xhr.statusCode);
                    }
                }

            });
        },

        showAlert: function(msg) {
            var loginAlertsDiv = $("#login-alerts");
            loginAlertsDiv.empty();
            loginAlertsDiv.append("<div class='alert alert-warning'>" + msg + "</div>");
        }

    });
}());