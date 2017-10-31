/**
 * Created by lorenzogagliani on 10/02/17.
 */

app.controller('homeController', function($location, SearchService){

    this.searchDest = function(city){
        SearchService.setCity(city);
        $location.path("/search");
    };

});