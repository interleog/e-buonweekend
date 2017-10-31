/**
 * Created by Pasquale on 10/02/2017.
 */
app.controller('houseController', function($scope, $resource,$location,ShowHouseService, AccountService) {
    console.log(ShowHouseService.getItem());
    $scope.house = ShowHouseService.getItem();

    $scope.host = AccountService.getUser(     {
            nick: $scope.house.utente
        },
        function(data){
            console.log("success: Get Host");
        }, function(){
            console.log("error: Get Host");
        }
    );

    $scope.isSelected = function(value){
        return $scope.host.feedValue == value;
    }
});