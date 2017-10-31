app.controller('searchController', function($scope, $resource,$location, SearchService,PagerService,ShowHouseService) {
    $scope.houses =[];
    $scope.pageSize = 8;

    $scope.pager = {};
    $scope.setPage = function(page) {
        if (page < 1 || page > $scope.pager.totalPages) {
            return;
        }

        // get pager object from service
        $scope.pager = PagerService.GetPager($scope.houses.length, page,$scope.pageSize);

        // get current page of items
        $scope.items = $scope.houses.slice($scope.pager.startIndex, $scope.pager.endIndex + 1);


    };

    $scope.changePageSize = function(pageSize){
        if(pageSize!=8 && pageSize!=16 && pageSize!=$scope.houses.length)
            pageSize = 8;

            $scope.pageSize = pageSize;
            $scope.pager = PagerService.GetPager($scope.houses.length, 1, pageSize);
            $scope.items = $scope.houses.slice($scope.pager.startIndex, $scope.pager.endIndex + 1);

    };


    $scope.showHouse = function(item){
        ShowHouseService.setItem(item);
        $location.path("/house");
    };
    $scope.getCity = function(){
      return $scope.currentCity;
    };

    var url = '/ebuonweekend-web';
    var Resource = $resource(url,
        {},
        {
            //actions
            searchByCity: {
                method: 'GET',
                url: url + "/search",
                isArray: true,
                params: {
                    city: '@city',
                    location: '@location',
                    date: '@date'
                }
            },
            searchHouseByUser: {
                method: 'GET',
                url: url + "/searchHouseByUser",
                isArray: true,
                params: {
                    user: '@user'
                }
            }
        }
    );

    if (SearchService.getTipoRicerca() === 'user'){
        Resource.searchHouseByUser({
                user: SearchService.getUser()
            },
            function (data) {
                $scope.houses = data;
                $scope.setPage(1);
                console.log("success: città trovate");
            }, function () {
                console.log("error");
            }
        );

    }else {
        Resource.searchByCity({
                city: SearchService.getCity(),
                location: SearchService.getLocation(),
                date: SearchService.getData()
            },
            function (data) {
                $scope.houses = data;
                $scope.setPage(1);
                //console.log("success: città trovate");
                console.log("Citta: "+SearchService.getCity());
                console.log("Location: "+SearchService.getLocation());
                console.log("Data: "+SearchService.getData());
                if( SearchService.getCity() === undefined) {
                    $scope.currentCity = "All location";
                }else{
                    $scope.currentCity = $scope.houses[0].city;
                }


            }, function () {
                console.log("error");
            }
        );
    }
});
