(function() {
	app.cubaAPI = {
		load: function(model, options) {

		},

		loadList: function(collection, options) {
            var url = "rest/v2/entities/" + collection.model.entityName;
            var params = "";
            if (collection.maxResults) {
                params = params == "" ? params + "?" : params + "&";
                params = params + "limit=" + collection.maxResults;
            }
            if (collection.view) {
                params = params == "" ? params + "?" : params + "&";
                params = params + "view=" + collection.view;
            }
            if (collection.sortOrder) {
                params = params == "" ? params + "?" : params + "&";
                params = params + "sort=" + collection.sortOrder;
            }
            url += params;

            $.ajax({
                url: url,
                type: "GET",
                headers: {"Authorization": "Bearer " + app.session.id},
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
            
		update: function(model, options) {
		    var update = model.attributes._entityName != undefined
		    var url = "rest/v2/entities/" + model.constructor.entityName;
		    if (update) {
		        url += "/" + model.attributes.id;
		    }
            var json = _.clone(model.attributes);
            $.ajax({
                url: url,
                type: update ? "PUT" : "POST",
                headers: {"Authorization": "Bearer " + app.session.id},
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
		    var url = "rest/v2/entities/" + model.constructor.entityName + "/" + model.attributes.id;
            $.ajax({
                url: url,
                type: "DELETE",
                headers: {"Authorization": "Bearer " + app.session.id},
                contentType: "application/json",
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