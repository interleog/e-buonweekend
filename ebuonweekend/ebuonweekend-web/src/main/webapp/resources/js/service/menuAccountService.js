/**
 * Created by lorenzogagliani on 11/02/17.
 */
app.factory('MenuAccountService', function(){
   var actualTab = 1;
   var getTab = function(){
       return actualTab;
   };
   var setTab = function(currentTab){
        actualTab = currentTab;
   };
   return {
       setTab: setTab,
       getTab: getTab
   }
});