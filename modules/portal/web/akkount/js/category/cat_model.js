(function(){
    "use strict";

    app.CategoryModel = Backbone.Model.extend({}, {
        entityName: "akk$Category"
    });

    app.CategoryCollection = Backbone.Collection.extend({
        model: app.CategoryModel,

        jpqlQuery: "select c from akk$Category c order by c.name",
        maxResults: 100,
        view: "_local",

        byId: function(id) {
            return  id == "" ?
                null :
                this.find(function (cat) {
                    return cat.id == id;
                }).toJSON();
        },

        byType: function(type) {
            return this
                .filter(function(cat) {
                    return cat.get("catType") == type;
                })
                .map(function(cat) {
                    return cat.toJSON();
                });
        }
    });

}());