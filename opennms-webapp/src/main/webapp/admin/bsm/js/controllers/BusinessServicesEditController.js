(function () {
    'use strict';

    angular.module('businessServices')
        .controller('BusinessServicesEditController', ['$scope', '$location', '$window', '$log', '$filter', '$routeParams', 'BusinessServices', function ($scope, $location, $window, $log, $filter, $routeParams, BusinessServices) {
            $log.debug('BusinessServicesEditController initializing...');
            $log.debug('params:' + $routeParams.id);
            $scope.bsToEdit = BusinessServices.get({id: $routeParams.id});
            $log.debug("bsToEdit: " + $scope.bsToEdit);
            $log.debug("bsToEdit id and name: " + $scope.bsToEdit.id + " " + $scope.bsToEdit.name);

            $scope.bsUpdate = function () {
                $log.debug("update bs");
                $log.debug("update bs name: " + $scope.bsToEdit.name);
                if ($scope.bsToEdit.name !== "") {
                    $scope.updateBS = new BusinessServices({
                        id: $scope.bsToEdit.id,
                        name: $scope.bsToEdit.name
                    }).$update().
                        then(function () {
                            $log.debug("update of bs complete");
                            $window.location.href = "admin/bsm/index.jsp";
                        })
                        .catch(function (req) {
                            $log.debug("update of bs failed: " + req);
                        });
                }
            };

            $scope.bsCancel = function () {
                $log.debug("cancel bs");
                $window.location.href = "admin/bsm/index.jsp";
            };

        }]);
}());