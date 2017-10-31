/**
 * Created by lorenzogagliani on 12/02/17.
 */
app.factory('ContactusService', ['$resource',
    function ($resource){
        var url="/ebuonweekend-web";
        return $resource(url, {}, {
            //actions
            sendMessageToAdmin: {
                method: 'POST',
                url: url + "/newMail",
                isArray: false,
                params: {
                    sender: '@sender',
                    object: '@object',
                    text: '@text'
                }
            }

        });
}]);