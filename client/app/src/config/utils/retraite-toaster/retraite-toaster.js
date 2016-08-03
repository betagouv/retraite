'use strict';

angular.module('SgmapRetraiteConfig').service('RetraiteToaster', function (toaster) {
    this.popSuccess = function(message) {
        toaster.pop({
            type: "success",
            title: message,
            showCloseButton: true,
            toasterId: "success"
        });
    };

    this.popError = function(message, body) {
        toaster.pop({
            type: "error",
            title: message,
            body: body ? body : "",
            showCloseButton: true,
            toasterId: "error"
        });
    };

    this.popErrorWithTimeout = function(message, body) {
        toaster.pop({
            type: "error",
            title: message,
            body: body ? body : "",
            showCloseButton: true,
            toasterId: "errorTimedout"
        });
    };
});

