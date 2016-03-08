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
                        if(name.indexOf('manifest.json', name.length - 'manifest.json'.length) !== -1) {
                            parseManifest(zipEntry);
                        }
                    });
                } catch(e) {

                }
            }
        })(file);

        reader.readAsArrayBuffer(file);
    }

    function parseManifest(entry) {
        $scope.application = JSON.parse(entry.asText());
    }

    $scope.file = $scope.ngDialogData;
    $scope.pluginname = 'This plugin';


});
