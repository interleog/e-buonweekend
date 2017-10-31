/**
 * Created by bonan on 12/02/2017.
 */
app.factory('MyhousesService',['$resource', function($resource){
    var url="/ebuonweekend-web";
    return $resource(url, {}, {
        getHouses: {
            method: 'GET',
            url: url + "/searchHouseByUser",
            isArray: true,
            params: {
                user: '@user'
            },
            transformResponse: function (data) {
                return angular.fromJson(data);
            }
        },
        //-----------------------------------
        deleteHouse: {
            method: 'GET',
            url: url + "/deleteHouse",
            params: {
                id: '@id'
            }
        }
    });
}]);