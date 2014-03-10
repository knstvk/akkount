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

                    $("#login-container").fadeOut();
                    window.location.hash = "operations";
                },
                error: function(xhr, status) {
                    console.log(xhr, status);
                    alert("Error!");
                }

            });
        }
    });
}());