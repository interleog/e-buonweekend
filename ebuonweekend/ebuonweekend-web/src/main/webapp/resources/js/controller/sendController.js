/**
 * Created by lorenzogagliani on 10/02/17.
 */
app.controller('sendController', function($scope, $cookies, HandService, WishlistService){

    $scope.sendHand = function(receiver, idHouseRcv){
        HandService.sendHand({
                sender:      $cookies.get('nick'),
                receiver:    receiver,
                idHouseRcv: idHouseRcv
            },
            function(data){
                if (data.returnObject){
                    swal("Hand sent successfully!", "Wait for an answer! Check your notification to see the current state." , "success");
                    console.log("success: Send Hand");
                }else{
                    swal("Impossible to send hand!", "You already send a hand to this house." , "error");
                    console.log("error: Send Hand");
                }
            }, function(){
                swal("Impossible to send hand!", "" , "error");
            }
        );
    };

    $scope.addWish = function (idHouse) {
        console.log("ID casa: "+idHouse);

        WishlistService.sendWish(     {
                nick: $cookies.get('nick'),
                idHouse: idHouse
            },
            function(data){
                console.log(data);

                if(data.returnObject) {
                    swal(
                        "Hi " + $cookies.get('nick'),
                        "House added successfully to your wishlist!\nYou can find it in Account->My wishlist",
                        "success"

                    );
                }
                else
                    swal(
                        "Hi " + $cookies.get('nick'),
                        "Error while adding the house to your wishlist!\nMaybe you are adding an house already present in your wishlist",
                        "error"
                    );
            }
        );
    };

});