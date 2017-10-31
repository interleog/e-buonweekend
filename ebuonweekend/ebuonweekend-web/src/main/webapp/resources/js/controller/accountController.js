/**
 * Created by lorenzogagliani on 09/02/17.
 */
app.controller('accountController', function($cookies, $resource, $scope, AccountService,UploaderService) {

    $scope.myInfo = {};
    $scope.myStatus = {};

    $scope.myInfo = AccountService.getUser(     {
            nick: $cookies.get('nick')
        },
        function(data){
            convertStatoUtenteToArray();
            console.log(data);
            console.log("success: Get User");
        }, function(){
            console.log("error: Get User");
        }
    );


    var convertStatoUtenteToArray = function(){
        var arr_status = $scope.myInfo.statoUtente.split(';');

        var i=0;
        var len = arr_status.length;

        while(i < len ){
            if ( angular.equals(arr_status[i], "sea") ){
                $scope.myStatus.sea = true;
            }
            if (angular.equals(arr_status[i], "mountain")){
                $scope.myStatus.mountain = true;
            }
            if (angular.equals(arr_status[i], "city")) {
                $scope.myStatus.city = true;
            }
            i++;
        }
    };

    $scope.uploadTrigger = function(){
        console.log("I'm here to change your image");
        angular.element('#uploadImage').trigger('click');
    }


    $scope.saveChanges = function(){
        console.log( angular.equals($scope.myStatus.sea, 'checked') );
        var status = "";
        var already = false;
        if ( $scope.myStatus.sea === true ){
            status = 'sea';
            already = true;
        }
        if ( $scope.myStatus.mountain === true  ){
            if (already){
                status += ";"
            }
            status += 'mountain';
            already = true;
        }
        if ( $scope.myStatus.city === true  ){
            if (already){
                status += ";"
            }
            status += 'city';
        }

    /*    UploaderService.uploadProfile({},$scope.form,function(data){
            console.log("Data:"+data.returnObject);
           // editAccount(data.returnObject);
            $scope.myInfo.profilePicture =data.returnObject;
        });*/

        AccountService.saveChangesProfile({
                nickName:   $cookies.get('nick'),
                pass:       $scope.myInfo.pass,
                firstName:  $scope.myInfo.firstName,
                lastName:   $scope.myInfo.lastName,
                telephone:  $scope.myInfo.telephone,
                profilePicture: $scope.myInfo.profilePicture,
                statoUtente:    status //$scope.myInfo.statoUtente
            },
            function(data){
                console.log(data);
                console.log("success: Save Changes User");
                swal({
                        title: "Profile edited successfully!",
                        text: "",
                        type: "success"
                    },
                    function () {
                        window.location.href = '#!/account';
                    });

            }, function(){
                swal("An error occurs!", "Try later" , "error");
                console.log("error: Save Changes User");
            }
        );
    };

    $scope.uploadImageTest = function(){
        UploaderService.uploadProfile({},$scope.form,function(data){
            console.log("Data dentro upload TEst:"+data.returnObject);
            // editAccount(data.returnObject);
            $scope.myInfo.profilePicture =data.returnObject;
        });
    };

});