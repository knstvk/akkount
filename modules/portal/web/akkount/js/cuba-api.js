(function() {
	app.cubaAPI = {
		load: function(model, options) {

		},

		loadList: function(collection, options) {
            var url = "api/query.json?s=" + app.session.id
                + "&e=" + collection.model.entityName + "&q=" + encodeURIComponent(collection.jpqlQuery);
            if (collection.maxResults)
                url = url + "&max=" + collection.maxResults;
            if (collection.view)
                url = url + "&view=" + collection.view;

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
            this.update(mode, options);
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

		},

        /**
         * Parse server responce replacing refs to objects.
         *
         * @param resp  data from server, containing "ref" attributes to refer objects inserted previously in JSON
         * @returns {*} full valid object graph
         */
        parse: function (resp) {
            var visited = {}, result = [];

            function process(obj) {
                var ref, processedObj;

                ref = obj["ref"];
                if (ref) {
                    processedObj = visited[ref];
                    if (processedObj)
                        return processedObj;
                    else {
                        app.log("Error parsing collection: ref=" + ref + " not found in visited objects");
                        return obj;
                    }
                } else {
                    if (obj.id) {
                        if (visited[obj.id])
                            return obj;

                        visited[obj.id] = obj;
                    }
                    _.each(obj, function(val, prop) {
                        if (_.isArray(val)) {
                            _.each(val, function(item, index) {
                                if (_.isObject(item)) {
                                    val[index] = process(item);
                                }
                            });
                        } else if (_.isObject(val)) {
                            obj[prop] = process(val);
                        }
                    });
                    return obj;
                }
            }

            if (_.isArray(resp)) {
                _.each(resp, function(item) {
                    result.push(process(item));
                });
                return result;
            } else {
                return process(resp);
            }
        }
	};
}());