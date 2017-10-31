/**
 * Created by bonan on 12/02/2017.
 */
app.controller('myhousesController',function($scope,$cookies, $location, MyhousesService, EditHouseService, ShowHouseService, $window){
    $scope.myhouses = {};
    $scope.myhouses = MyhousesService.getHouses({
            user: $cookies.get('nick')
        },
        function(data){
            $scope.myhouses = data;
            console.log($scope.myhouses);
        },
        function(error){
            console.log("Something goes wrong during the download");
            console.log(error);
        }
    );

    $scope.goAddHouse = function () {
        $location.path('/account/newhouse');
    };

    $scope.editHouse = function (id) {
        EditHouseService.setIsEdit('yes');
        EditHouseService.setIdHouse(id);
        $location.path('/account/newhouse');
    };
    $scope.showHouse = function(item){
        ShowHouseService.setItem(item);
        $location.path("/house");
    };

    $scope.delete = function(idHouse){
        swal(  {
                title: "Are you sure?",
                text: "Your house will be definitely deleted from our database.",
                type: "warning",
                showCancelButton: true,
                cancelButtonText: "No!",
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete it!",
                closeOnConfirm: false
            },
            function (isConfirm) {
                if (isConfirm) {
                    MyhousesService.deleteHouse({
                            id: idHouse
                        },
                        function (data) {
                            if(data.returnObject){
                                swal({
                                    title: "Deleted!",
                                    text: "Your house has been deleted.",
                                    type: "success"
                                },
                                function(){
                                    $window.location.reload();
                                });
                            }
                            else{
                                swal("Error!", "Something went wrong!", "warning");
                                //alert("Error!");
                            }
                        });
                }
            }
        );

    };
});