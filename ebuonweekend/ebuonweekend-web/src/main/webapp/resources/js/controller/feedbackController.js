/**
 * Created by bonan on 12/02/2017.
 */
app.controller('feedbackController', function($scope,$route,$location,$cookies,JourneyService,FeedbackService,SearchService){
    $scope.myfeeds = {};
    $scope.myfeeds.feedback = 2;
    $scope.score = SearchService.getScore();
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

    $scope.isSelected = function(value){

        return $scope.myfeeds.feedback === value;
    };
    $scope.setScore = function(value){
        SearchService.myScore(value);
    };
    $scope.sendFeedback = function(receiver){
        FeedbackService.sendFeedback({
                sender: $cookies.get('nick'),
                houseOwner: receiver,
                feed: SearchService.getScore()
            },
            function(data){
                if (data.returnObject){
                    console.log("Feedback sent successfully.");
                    swal("Feedback sent successfully.","","success");
                    $route.reload();
                    //$location.path('/account/leavefeedback');
                }
                else{
                    console.log("Problem during feedback send.");
                }
            },
            function(){
                console.log("Nothing to show");
            });
    }
});