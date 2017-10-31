var app = angular.module('eBuonweekend', ['ngRoute', 'ngResource', 'ngCookies', 'doowb.angular-pusher']);

var baseUrl = "/ebuonweekend-web/resources/html/pages/";

// configure our routes
app.config(function($routeProvider, $locationProvider) {
    $routeProvider
        .when('/house', {
            templateUrl : baseUrl + 'house.html',
            controller  : 'houseController'
        })
        .when('/about', {
            templateUrl : baseUrl + 'about.html',
            controller  : 'aboutController'
        })
        .when('/account', {
            templateUrl : baseUrl + 'account.html',
            controller: 'accountController'
        })
        .when('/account/myhouses', {
            templateUrl : baseUrl + 'myhouses.html',
            controller: 'myhousesController'
        })
        .when('/account/newhouse', {
            templateUrl : baseUrl + 'newhouse.html',
            controller: 'newhouseController'
        })
        .when('/account/myjourneys', {
            templateUrl : baseUrl + 'myjourneys.html',
            controller: 'journeyController'
        })

        .when('/account/mywishlist', {
            templateUrl : baseUrl + 'mywishlist.html',
            controller: 'wishlistController'
        })
        .when('/account/leavefeedback', {
            templateUrl : baseUrl + 'leavefeedback.html',
            controller: 'feedbackController'
        })
        .when('/account/chat', {
            templateUrl : baseUrl + 'chat.html',
            controller: 'chatController'
        })
        .when('/contactus', {
            templateUrl : baseUrl + 'contactus.html',
            controller  : 'contactusController'
        })
        .when('/contactus/faq', {
            templateUrl : baseUrl + 'faq.html',
            controller  : 'faqController'
        })
        .when('/home', {
            templateUrl : baseUrl + 'home.html'
        })
        .when('/', {
            templateUrl : baseUrl + 'home.html'
        })
        .when('/inbox', {
            templateUrl : baseUrl + 'inbox.html',
            controller  : 'inboxController'
        })
        .when('/signup', {
            templateUrl : baseUrl + 'signup.html'
        })
        .when('/search', {
            templateUrl : baseUrl + 'search.html',
            controller  : 'searchController'
        })
        .when('/terms', {
            templateUrl : baseUrl + 'terms.html',
            controller  : 'termsController'
        })
        .otherwise('/', {
            templateUrl : baseUrl + 'home.html'
        });

});

app.controller("mainController", function ($scope) {

    $scope.reloadHome = function () {
        window.location.href = '/ebuonweekend-web/';
    };

});

app.controller("navbarController", function ($scope, $cookies, $location, $route, $window,SearchService, HandService,Pusher) {

    var vm = this;

    $scope.handRequest = 0;

    $scope.hands = [];
    var activated = false;
    console.log(activated);

    //$scope.handRequest = HandService.getHandRequest();

    Pusher.subscribe('handNumber_'+$cookies.get('nick'), 'newNotification', function (notification) {
        // an item was updated. find it in our list and update it.
        //$scope.hands.push(notification);
        //$scope.handRequest = $scope.hands.length;
        $scope.hands = HandService.getHands({
                receiver: $cookies.get('nick')
            },
            function(data){
                if(data.returnObject != null){
                    $scope.hands = data.returnObject;
                    $scope.handRequest = $scope.hands.length;
                }
                else{
                    $scope.handRequest = 0;
                    console.log("Error while counting hand request");
                }
            },
            function(error){
                $scope.handRequest = 0;
                console.log("error: Hands not gotcha ");
            }
        );
    });

    Pusher.subscribe('handNumber_'+$cookies.get('nick'), 'removeNotification', function (idNotifica) {
        // an item was updated. find it in our list and update it.
        console.log("SONO DENTRO REMOVE-NOTIFICATION");
        console.log(idNotifica);

        /*for(var j=0; j < $scope.hands.length; j++){
            for(var i=0; i<idNotifica.length; i++){
                if($scope.hands[j].id == idNotifica[i]) {
                    $scope.hands.splice(j, 1);
                }
            }
        }*/
        $scope.hands = HandService.getHands({
                receiver: $cookies.get('nick')
            },
            function(data){
                if(data.returnObject != null){
                    $scope.hands = data.returnObject;
                    $scope.handRequest = $scope.hands.length;
                }
                else{
                    $scope.handRequest = 0;
                    console.log("Error while counting hand request");
                }
            },
            function(error){
                $scope.handRequest = 0;
                console.log("error: Hands not gotcha ");
            }
        );
    });

   /* var handRequest2 = HandService.countHands({
            user: $cookies.get('nick')
        },
        function(data){
            if(data.returnObject != null){
                handRequest2 = data.returnObject;
                console.log("handed");
            }
            else{
                console.log("Error while counting hand request");
            }

            $scope.handRequest = handRequest2;

        },
        function(error){
            console.log("error: HandRequest");
        }
    );
*/
    var handsTemp= HandService.getHands({
            receiver: $cookies.get('nick')
        },
        function(data){
            if(data.returnObject != null){
                handsTemp = data.returnObject;
                $scope.hands = handsTemp;
                $scope.handRequest = handsTemp.length;
                console.log("hands gotcha!  size"+ handsTemp.length + " hands:"+handsTemp);
            }
            else{
                $scope.handRequest = 0;
                console.log("Error while counting hand request");
            }



        },
        function(error){
            $scope.handRequest = 0;
            console.log("error: Hands not gotcha ");
        }
    );

    $scope.toChat = function () {
        $location.path("account/chat");
    };



    /*$scope.reloadHands = function() {
        activated = true;
        var handRequest2 = HandService.countHands({
                user: $cookies.get('nick')
            },
            function(data){
                if(data.returnObject != null){
                    handRequest2 = data.returnObject;
                    console.log("handed");
                }
                else{
                    console.log("Error while counting hand request");
                }

                $scope.handRequest = handRequest2;
                setTimeout($scope.reloadHands, 30000);
            },
            function(error){
                console.log("error: HandRequest");
                setTimeout($scope.reloadHands, 30000);
            }
        );
    };*/

    vm.saveFormData = function(){
        //console.log("saveFormData: " + $scope.city_param);
        $scope.date_param = $('#search_by_dates').val();

        SearchService.setCity($scope.city_param);
        SearchService.setData($scope.date_param);
        SearchService.setLocation($scope.location_param);
        $location.path("/search");
        $route.reload();
    };

  /*  function doAsync(functio, callBack) {
        if (typeof functio === function) setTimeout(function() { functio(); callBack();},1);
    } */

 /* if(!activated)
     $scope.reloadHands();
*/


});

app.config(['PusherServiceProvider',
    function(PusherServiceProvider) {
        PusherServiceProvider
            .setToken('59110edd1bd1ebce97dd')
            .setOptions({
                cluster: 'eu',
                encrypted: true
            });
    }
]);

app.filter('range', function() {
    return function(input, total) {
        total = parseInt(total);

        for (var i=0; i<total; i++) {
            input.push(i);
        }

        return input;
    };
});

app.controller("footerController", function ($scope, $cookies) {

    $scope.isLogged = function(){
        if ($cookies.get('nick') == undefined){
            return false;
        }else{
            return true;
        }
        //alert($cookies.get('nick'));
    }

});