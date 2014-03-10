/**
 * Created by krivopustov on 09.03.14.
 */

$(document).ready(function() {

    window.App = window.App || {};

    App.session = {};

    var operations = new App.OperationsCollection();
    var router = new App.Router({operations: operations});

    Backbone.history.start();

    operations.reset([
        {
            "id": "7BB288A2-5B7B-4266-A770-7929221FE12E",
            "date": "2014-01-01",
            "comments": "op1"
        },
        {
            "id": "9A12A229-1A18-46E9-A16C-03C256284AF2",
            "date": "2014-01-02",
            "comments": "op2"
        }
    ]);
});
