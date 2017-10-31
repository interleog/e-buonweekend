app.factory('UserService', ['$resource',
    function ($resource){
      var url="/ebuonweekend-web";
      return $resource(url, {}, {
      //actions
          login: {
                method: 'POST',
                url: url+"/logon",
                isArray: false,
                params:{
                    nick: '@nick',
                    pass: '@pass'
                },
                transformResponse: function (data) {
                  return angular.fromJson(data);
                }
          },
          signup: {
              method: 'POST',
              url: url+"/register",
              isArray: false,
              params:{
                  nick: '@nick',
                  mail: '@mail',
                  pass: '@pass'
              },
              transformResponse: function (data) {
                  return angular.fromJson(data);
              }
          }
          //login: {method: 'GET', url: "/logon/:nick/:pass", params:{nick: '@nick', pass: '@pass'}}
      });
}]);