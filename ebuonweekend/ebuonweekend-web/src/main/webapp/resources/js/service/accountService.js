/**
 * Created by lorenzogagliani on 09/02/17.
 */
app.factory('AccountService', ['$resource',
    function ($resource){
        var url="/ebuonweekend-web";
        return $resource(url, {}, {
            //actions
            getUser: {
                method: 'POST',
                url: url + "/getUser",
                isArray: false,
                params: {
                    nick: '@nick'
                },
                transformResponse: function (data) {
                    return angular.fromJson(data);
                }
            },
            saveChangesProfile: {
                method: 'POST',
                url: url + "/editUser",
                isArray: false,
                params: {
                    nickName:   '@nickName',
                    pass:       '@pass',
                    firstName:  '@firstName',
                    lastName:   '@lastName',
                    telephone:  '@telephone',
                    //email:      '@email',
                    profilePicture: '@profilePicture',
                    statoUtente:  '@statoUtente'
                }
            },
            uploadNewHouse: {
                method: 'POST',
                url: url + "/newHouse",
                params: {
                    city:   '@city',
                    address:'@address',
                    zip:    '@zip',
                    mq:     '@mq',
                    path:   '@path',
                    user:   '@user',
                    tags:   '@tags',
                    descr:  '@descr',
                    availableDates: '@availableDates',
                    isAvailable:    '@isAvailable'
                }
            },
            getHouseById: {
                method: 'POST',
                url: url + "/getHouseById",
                params: {
                    id:   '@id'
                }
            },
            editHouse: {
                method: 'POST',
                url: url + "/editHouse",
                params: {
                    id:     '@id',
                    city:   '@city',
                    address:'@address',
                    zip:    '@zip',
                    mq:     '@mq',
                    pathimg:   '@pathimg',
                    tags:   '@tags',
                    descr:  '@descr',
                    availableDates: '@availableDates',
                    isAvailable:    '@isAvailable'
                }
            }

        });
    }]);
