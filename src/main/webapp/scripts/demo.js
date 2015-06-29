
function demoController($scope, $http) {

    $http.get("/rest/member").success(function(data) {
            $scope.error = "OK"; 
            $scope.member = data;
        	alert("ok!");
    })
    .error(function(status) {
    	$scope.error = status;
    	if (status.status == 403) {
        	alert("Forbidden!");
            $scope.error = "403"; 
		}
    });
}
