/**
 * Created by lorenzogagliani on 13/02/17.
 */
app.controller('newhouseController', function ($scope, $cookies, AccountService, UploaderService, EditHouseService) {

    $scope.myNewHouse = {};

    $scope.title = "";

    $scope.isEdit = false;
    $scope.isNewHouse = true;

    $scope.available = false;
    $scope.notAvailable = false;

    if (EditHouseService.getIsEdit() === 'yes'){
        $scope.isEdit = true;
        $scope.isNewHouse = false;
        $scope.title = "Edit House";
        EditHouseService.setIsEdit('no');
        $scope.myNewHouse = AccountService.getHouseById({
                id:   EditHouseService.getIdHouse()
            },
            function(data){
                console.log(data);
                console.log("success: Save Changes House");
            }, function(){
                console.log("error: Save modify House");
                swal({
                    title:"Something went wrong!",
                    text: "Please try again...",
                    type: "error"
                },
                    function () {
                        window.location.href = '#!/account/myhouses';
                    });
            }
        );
        $scope.available = ($scope.myNewHouse === "true");
        $scope.notAvailable = ($scope.myNewHouse === "false");
    }else{
        $scope.title = "Insert new house";
    }

    $scope.addNewHouse = function () {
        $scope.myNewHouse.availableDates = $('#textbox_dates').val();
        AccountService.uploadNewHouse({
                city:   $scope.myNewHouse.city,
                address:$scope.myNewHouse.address,
                zip:    $scope.myNewHouse.zip,
                mq:     $scope.myNewHouse.mq,
                path:   $scope.myNewHouse.pathimg,
                user:   $cookies.get('nick'),
                tags:   $scope.myNewHouse.tags,
                descr:  $scope.myNewHouse.descr,
                availableDates: $scope.myNewHouse.availableDates,
                isAvailable: $scope.available

            },
            function(data){
                console.log(data);
                console.log("success: Save Changes User");
                swal({
                        title: "House added successfully!",
                        text: "",
                        type: "success"
                    },
                    function () {
                        window.location.href = '#!/account/myhouses';
                    });
            }, function(){
                console.log("error: Save Changes User");
                swal({
                    title:"Something went wrong!",
                    text: "Please try again...",
                    type: "error"
                });
            }
        );
    };

    $scope.editHouse = function () {
        $scope.myNewHouse.availableDates = $('#textbox_dates').val();
        AccountService.editHouse({
                id:     $scope.myNewHouse.id,
                city:   $scope.myNewHouse.city,
                address:$scope.myNewHouse.address,
                zip:    $scope.myNewHouse.zip,
                mq:     $scope.myNewHouse.mq,
                pathimg:   $scope.myNewHouse.pathimg,
                tags:   $scope.myNewHouse.tags,
                descr:  $scope.myNewHouse.descr,
                availableDates: $scope.myNewHouse.availableDates,
                isAvailable: $scope.available
            },
            function(data){
                console.log(data);
                console.log("success: Edit House");
                swal({
                        title: "House edited successfully!",
                        text: "",
                        type: "success"
                    },
                    function () {
                        window.location.href = '#!/account/myhouses';
                    });
            }, function(){
                console.log("error: Edit House");
                swal({
                    title:"Something went wrong!",
                    text: "Please try again...",
                    type: "error"
                });
            }
        );
    };

    $scope.uploadImageTest2 = function(){
        UploaderService.uploadHouse({},$scope.form,function(data){
            console.log("Data dentro upload Test: " + data.returnObject);
            $scope.myNewHouse.pathimg = data.returnObject;
        });
    };

});