(function() {
    app.CategorySelectView = Backbone.View.extend({
        tagName: "select",
        className: "form-control",

        initialize: function(categories, type) {
            this.categories = categories;
            this.type = type;
        },

        render: function() {
            this.$el.html(_.template($("#category-select-template").html(), {
                categories: this.categories.byType(this.type)
            }));
            return this;
        }
    });
}());