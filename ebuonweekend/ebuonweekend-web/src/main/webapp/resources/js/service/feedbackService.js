/**
 * Created by bonan on 12/02/2017.
 */
app.factory('FeedbackService',['$resource', function($resource){
    var url="/ebuonweekend-web";
    return $resource(url, {}, {
        sendFeedback: {
            method: 'POST',
            url: url + "/addFeed",
            params: {
                sender: '@sender',
                houseOwner: '@houseOwner',
                feed: '@feed'
            },
            transformResponse: function (data) {
                return angular.fromJson(data);
            }
        },
        pendingFeedbacks:{
            method: 'POST',
            url: url + "/pendingFeedbacks",
            isArray: true,
            params: {
                user: '@user',
            },
            transformResponse: function (data) {
                return angular.fromJson(data);
            }
        }
    });
}]);