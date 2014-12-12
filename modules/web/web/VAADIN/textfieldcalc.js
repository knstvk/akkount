window.akkount_web_operation_CalcExtension = function() {
    var connectorId = this.getParentId();
    var input = $(this.getElement(connectorId));
    input.on("keypress", function(event) {
        if (event.which == 61) {
            event.preventDefault();
            var x = event.target.value;
            if (x.match(/([-+]?[0-9]*\.?[0-9]+[\-\+\*\/])+([-+]?[0-9]*\.?[0-9]+)/)) {
                event.target.value = eval(x);
            }
        }
    });
}