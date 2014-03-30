(function() {
	app.cubaAPI = {
		load: function(model, options) {

		},

		loadList: function(model, options) {
            var url = "api/query.json?s=" + app.session.id
                + "&e=" + model.entityName + "&q=" + encodeURIComponent(model.jpqlQuery);
            if (model.maxResults)
                url = url + "&max=" + model.maxResults;
            if (model.view)
                url = url + "&view=" + model.view;

            $.ajax({
                url: url,
                type: "GET",
                success: function(json) {
                    app.log("Success: " + json);
                    options.success(json);
                },
                error: function(xhr, status) {
                    app.log("Error: " + status);
                    options.error(xhr, status);
                }
            });
        },
            
		create: function(model, options) {

		},

		update: function(model, options) {
            var json = {
                "commitInstances": [_.clone(model.attributes)]
            };
            var url = "api/commit?s=" + app.session.id;
            $.ajax({
                url: url,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(json),
                success: function(json, status, xhr) {
                    app.log("Success: " + status);
                    options.success(json);
                },
                error: function(xhr, status) {
                    app.log("Error: " + status);
                    options.error(xhr, status);
                }
            });

		},

		remove: function(model, options) {

		}
	};
}());