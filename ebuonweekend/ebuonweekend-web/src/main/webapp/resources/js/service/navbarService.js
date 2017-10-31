/**
 * Created by bonan on 09/02/2017.
 */
app.factory('NavbarService',function(){
    var actualTab = 1;
   var setMenu = function(currentTab){
       actualTab = currentTab;
   };
   var getMenu = function(){
       return actualTab;
   };
   return{
       getMenu: getMenu,
       setMenu: setMenu
   }
});