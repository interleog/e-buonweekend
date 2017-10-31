/**
 * Created by bonan on 11/02/2017.
 */
app.controller('journeyController', function($scope,$cookies,JourneyService,FeedbackService){
    $scope.myjourneyList = {};

    $scope.myjourneyList = JourneyService.getCompletedJourney({
            user: $cookies.get('nick')
        },
        function(data){
            console.log("Journey server call goes well");
            if (data.returnObject==null){
                $scope.myjourneyList = {};
                console.log("Journey list empty");
            }
            else{
                console.log("Something to show");
                $scope.myjourneyList = data.returnObject;
                console.log($scope.myjourneyList);
               }
        },
        function(error) {
            console.log(error);
            console.log("Something in the journey call goes wrong");
    });

    $scope.isSelected = function(index,value){
        console.log("indice: "+ index + " " +$scope.myjourneyList[index].feedback);
        console.log(value);
        console.log($scope.myjourneyList[index].feedback == value);
        return $scope.myjourneyList[index].feedback == value;
    };

    $scope.myfeeds = FeedbackService.pendingFeedbacks({
            user: $cookies.get('nick')
        },
        function(data){
            console.log("Feedback API goes well");
            if (data.length === 0){
                console.log("No feedback to be left");
            }
            else{
                console.log("Something to show");
                $scope.myfeeds = data;
                console.log($scope.myfeeds);
            }
        },
        function(error) {
            console.log(error);
            console.log("Something in the journey call goes wrong");
        });

});