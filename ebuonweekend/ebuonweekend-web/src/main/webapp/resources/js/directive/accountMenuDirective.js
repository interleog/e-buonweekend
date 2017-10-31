/**
 * Created by bonan on 07/02/2017.
 */
app.directive('accountMenu', function($location){
   return{
       restrict: 'E',
       templateUrl: '/ebuonweekend-web/resources/html/directive/account-menu-directive.html',
       /*scope: {
           tab: '@'
       },*/
       controller: function(){
           /*this.setTab = function(currentTab){
             NavbarService.setMenu(currentTab);
           };
           this.isSelected = function(currentTab){
               return NavbarService.getMenu() === currentTab;
           };*/
           this.go = function(path){
               $location.path(path);
           };
       },
       controllerAs: 'accountCtrl'
   };
});