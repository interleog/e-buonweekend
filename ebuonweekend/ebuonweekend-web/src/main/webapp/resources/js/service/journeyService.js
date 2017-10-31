/**
 * Created by bonan on 11/02/2017.
 */
app.factory('JourneyService',['$resource', function($resource){
    var url="/ebuonweekend-web";
    return $resource(url, {}, {
        //getJourney è la vecchia query che prendeva i journey dalla tabella posta
        getJourney: {
            method: 'GET',
            url: url + "/searchMyJourneys",
            isArray: true,
            params: {
                user: '@nick'
            },
            transformResponse: function (data) {
                return angular.fromJson(data);
            }
        },
        //nuova query che prende i journey completati dalla tabella viaggi
        getCompletedJourney: {
            method: 'GET',
            url: url + "/getCompletedJourney",
            params: {
                user: '@user'
            }
        }
    });
}]);