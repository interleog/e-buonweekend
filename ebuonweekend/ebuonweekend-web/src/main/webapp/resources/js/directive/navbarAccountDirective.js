/**
 * Created by bonan on 09/02/2017.
 */
app.directive('navbarAccount',function($location,$cookies){
    return{
        restrict: 'E',
        templateUrl: '/ebuonweekend-web/resources/html/directive/navbar-directive.html',
        controller: function(){
            /* Function created for the dropdown list of the navbar: DON'T DELETE ME!
             this.setTab = function(currentTab){
             console.log("Ciao");
             $location.path("/account");
             NavbarService.setMenu(currentTab);
             };
             */
            this.isShown = function(){
                if($cookies.get('nick')){
                    return true;
                }
                else{
                    return false;
                }
            };
            this.logout = function(){
                $cookies.remove('nick');
                $cookies.remove('pass');
                $cookies.remove('myProfilePicture');

                swal({
                        title: "Bye Bye!",
                        text: "Don't forget to come back soon!",
                        imageUrl: "/ebuonweekend-web/resources/img/bye.png",
                        imageSize: "200x200",
                        animation: "slide-from-top"
                    },
                        function(){
                            window.location.href = '/ebuonweekend-web/';
                        }
                    );
            };
        },
        controllerAs: 'navCtrl'
    };
});