app.factory('WishlistService', ['$resource',
    function ($resource){
        var url="/ebuonweekend-web";
        return $resource(url, {}, {
            //actions
            sendWish: {
                method: 'POST',
                url: url + "/addWishlist",
                //isArray: true,
                params: {
                    nick: '@nick',
                    idHouse: '@idHouse'
                },
                transformResponse: function (data) {
                    return angular.fromJson(data);
                }
            },
            //-----------------------------------------
            showMyWishlist: {
                method: 'POST',
                url: url + "/showMyWishlist",
                isArray: true,
                params: {
                    nick: '@nick'
                }/*,
                transformResponse: function (data) {
                    return angular.fromJson(data);
                }*/
            },
            //-----------------------------------------
            deleteFromWishlist: {
                method: 'POST',
                url: url + "/deleteWishlist",
                //isArray: true,
                params: {
                    nick: '@nick',
                    idHouse: '@idHouse'
                }/*,
                 transformResponse: function (data) {
                 return angular.fromJson(data);
                 }*/
            }
            //-----------------------------------------


        });
    }]);