'use strict'
angular.module('piaApp').controller("PluginPermissionController", function ($scope) {

    parseZipFile($scope.ngDialogData);


    function parseZipFile(file) {
        var reader = new FileReader();

        reader.onload = (function() {
            return function(e) {
                try {
                    var zip = new JSZip(e.target.result);

                    $.each(zip.files, function (index, zipEntry) {
                        var name = zipEntry.name;
                        if(name.indexOf('permissions.json', name.length - 'permissions.json'.length) !== -1) {
                            parsePermissions(zipEntry);
                        }
                        if(name.indexOf('schedules.json', name.length - 'schedules.json'.length) !== -1) {
                            parseSchedules(zipEntry);
                        }
                    });
                } catch(e) {

                }
            }
        })(file);

        reader.readAsArrayBuffer(file);
    }

    function parsePermissions(entry) {
        $scope.application = JSON.parse(entry.asText());
    }

    function parseSchedules(entry) {
        $scope.application = JSON.parse(entry.asText());
    }

    $scope.file = $scope.ngDialogData;
    $scope.pluginname = 'This plugin';


});
