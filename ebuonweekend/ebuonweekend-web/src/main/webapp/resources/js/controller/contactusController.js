/**
 * Created by lorenzogagliani on 12/02/17.
 */
app.controller('contactusController', function($scope, ContactusService){
    $scope.message = {};
    $scope.sendMessageToAdmin = function(){
        ContactusService.sendMessageToAdmin({
                sender: $scope.message.sender,
                object: $scope.message.object,
                text:   $scope.message.text
            },
            function (data) {
                $scope.message = {};
                swal("Message sent successfully!", "", "success");
            },
            function (error) {
                swal("Something went wrong!", "Please try again...", "error");
            });
    };
});