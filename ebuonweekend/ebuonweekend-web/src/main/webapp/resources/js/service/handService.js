/**
 * Created by lorenzogagliani on 10/02/17.
 */
app.factory('HandService', ['$resource',
    function ($resource){
        var url="/ebuonweekend-web";
        return $resource(url, {}, {
            //actions
            sendHand: {
                method: 'POST',
                url: url+"/sendHand",
                params:{
                    sender: '@sender',
                    receiver: '@receiver',
                    idHouseRcv: '@idHouseRcv'
                }
            },
            //-------------------------------
            countHands: {
                method: 'POST',
                url: url+"/countHands",
                params: {
                    user: '@user'
                }
            },
            //------------------------------
            getHands: {
                method: 'POST',
                url: url+"/getNotifications",
                params: {
                    receiver: '@receiver'
                }
            }

            //-------------------------------
        });



    }]);