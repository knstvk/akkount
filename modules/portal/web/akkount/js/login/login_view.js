(function() {
    "use strict";

    app.LoginView = Backbone.View.extend({

        initialize: function() {
            _.bindAll(this, "render", "loginClick");
        },

        events: {
            "click #login-button":  "loginClick"
        },

        render: function() {
            this.$el.html($("#login-template").html());
            var userName = window.localStorage.getItem(SESSION_USER_NAME_KEY);
            if (userName) {
                this.$el.find("#remember-me").attr("checked", true);
                this.$el.find("#login-name").val(userName);
                this.$el.find("#password").attr("autofocus", true);
            } else {
                this.$el.find("#login-name").attr("autofocus", true);
            }
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
                    app.setSession(text, $("#login-name").val());
                    if ($("#remember-me").is(":checked")) {
                        window.localStorage.setItem(SESSION_USER_NAME_KEY, $("#login-name").val());
                    } else {
                        window.localStorage.removeItem(SESSION_USER_NAME_KEY);
                    }
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