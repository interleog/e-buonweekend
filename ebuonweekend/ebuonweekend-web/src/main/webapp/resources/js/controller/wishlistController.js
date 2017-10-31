
app.controller('wishlistController', function($scope, $cookies, $location, WishlistService, ShowHouseService, $route) {

    $scope.myWishlist = {};

    $scope.myWishlist = WishlistService.showMyWishlist({
            nick: $cookies.get('nick')
        },
        function(data){
            if(data.length === 0){
                //alert("Your wishlist is empty!");
            }
            else{
                //nothing
            }

        },function(error){
            alert("Error: show wishlist");
        }
    );

    $scope.showHouse = function(item){
        ShowHouseService.setItem(item);
        $location.path("/house");
    };

    $scope.deleteHouse = function (idHouse) {
        WishlistService.deleteFromWishlist({
            nick: $cookies.get('nick'),
            idHouse: idHouse
        },
        function (data) {
            if(data.returnObject){
                swal({
                        title: "House removed from your wishlist!",
                        text: "",
                        type: "success"
                    },
                    function () {
                        $route.reload();
                    });

            }
            else{
                alert("Error!");
            }

        })

    };


});
