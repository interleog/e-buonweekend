/**
 * Created by lorenzogagliani on 11/02/17.
 */

app.controller('chatController', function($scope, $cookies, $filter, $location, ChatService, SearchService) {

    $scope.myProfilePicture = $cookies.get('myProfilePicture');
    $scope.picture = "";
    $scope.users = {};
    $scope.conversation = {};
    $scope.nick_rcv = "";
    $scope.message = "";
    $scope.newMessage = {};

    $scope.buttonSend = false;
    $scope.buttonHandshake = false;

    $scope.houses = {};
    $scope.isOneDate = false;
    $scope.showDismissButton = true;

    $scope.users = ChatService.getSenders({
            receiver: $cookies.get('nick')
        },
        function(data){
            if(data.length === 0){

            }
            else{

            }

        },function(error){
            //alert("Error: Get Senders, try again");
        }
    );




    $scope.showConversation = function (rcv) {

        $scope.buttonSend = false;
        $scope.buttonHandshake = false;


        $scope.picture = rcv.profilePicture;
        $scope.nick_rcv = rcv.nickName;

        $scope.conversation = ChatService.getConversation({
                sender: $cookies.get('nick'),
                receiver: rcv.nickName
            },
            function(data){
                for (var i = 0; i < $scope.conversation.length; i++){
                    if ($scope.conversation[i].isHand === "2"){
                        $scope.buttonSend = true;
                        $scope.buttonHandshake = true;
                    }
                }

            }
        );
    };

    $scope.isMyMessage = function(sender){
        return ($cookies.get('nick') === sender)
    };


    $scope.sendHandshake = function (nick_receiver) {
        ChatService.sendHandshake({
                sender: $cookies.get('nick'),
                receiver: nick_receiver
            },
            function(data){
                if(data.returnObject){

                }else {

                }
            }
        );
    };

    $scope.sendPrivateMessage = function (nick_receiver) {
        ChatService.sendPrivateMessage({
                sender:     $cookies.get('nick'),
                receiver:   nick_receiver,
                message:    $scope.message
            },
            function(data){
                if(data.returnObject){
                    var data_tmp = new Date();
                    data_tmp = $filter('date')(data_tmp, "dd/MM/yyyy");
                    data_tmp = data_tmp.split('/');
                    $scope.newMessage = {   'message' : $scope.message,
                                            'sender' :  $cookies.get('nick'),
                                            'dataMessage':  {
                                                dayOfMonth: data_tmp[0],
                                                monthValue: data_tmp[1],
                                                year:       data_tmp[2]
                                            }
                                        };
                    $scope.conversation.push($scope.newMessage);
                    $scope.message = "";
                }else {

                }
            }
        );

    };

    $scope.searchHouseUser = function (user) {
        SearchService.setTipoRicerca('user');
        SearchService.setUser(user);
        $location.path('/search');
    };


    $scope.selectDays = function(rcv){

        var isSuspended = false;
        var journeySuspended;

        //CONTROLLO SE L'ALTRO UTENTE HA GIà SELEZIONATO LA CASA, CIOè SE CI SONO JOURNEY "IN SOSPESO"
        ChatService.getSuspendedJourneys({
            user: rcv,
            houseOwner: $cookies.get('nick')
        },
        function (data) {
            if(data.returnObject != null){
                journeySuspended = data.returnObject;
                isSuspended = true;
            }

            if(!isSuspended) {
                console.log("caso in cui l'altro non ha scelto ancora la data");
                //CASO IN CUI L'ALTRO UTENTE NON HA ANCORA SCELTO LA DATA
                ChatService.getHandedHouses({
                        sender: $cookies.get('nick'),
                        receiver: rcv
                    },
                    function (data) {
                        if (data.returnObject != null) {
                            $scope.houses = data.returnObject;

                            for (var i = 0; i < $scope.houses.length; i++)
                                $scope.houses[i].availableDates = $scope.houses[i].availableDates.split(';');
                        }
                    });
            }
            else{
                console.log("caso in cui l'altro ha scelto già la data");
                //CASO IN CUI L'ALTRO UTENTE HA SCELTO LA DATA E DEVO SOLO CONFERMARE O RIFIUTARE
                ChatService.getHandedHouses({
                        sender: $cookies.get('nick'),
                        receiver: rcv
                    },
                    function (data) {
                        if (data.returnObject != null) {
                            $scope.houses = data.returnObject;

                            for (var i = 0; i < $scope.houses.length; i++)
                                $scope.houses[i].availableDates = journeySuspended.journeyDate;  //COME UNICA DATA POSSIBILE METTO QUELLA SCELTA DALL'ALTRO UTENTE

                            $scope.isOneDate = true;
                        }
                    });
            }
        });


    };

    $scope.submitFormSelectHouseDays = function (houseOwner, idHouse, data) {

        if (data != undefined) {
            //console.log("House owner: " + houseOwner + "\nidHouse: " + idHouse + "\nData: " + data);

            ChatService.insertJourney({
                user: $cookies.get('nick'),
                houseOwner: houseOwner,
                idHouse: idHouse,
                journeyDate: data
            },
            function (data) {
                if(data.returnObject){
                    swal("Handshake done!", "You've correctly sent to "+houseOwner+" the handshake!", "success");
                    $scope.showDismissButton = false;
                }
                else{
                    swal("Error!", "Handshake already done!", "error");
                }

            });
        }
    };

    $scope.deleteJourney = function (user) {

        ChatService.deleteSuspendedJourney({
            user: user,
            houseOwner: $cookies.get('nick')
        },
        function (data) {
            if(data.returnObject){
                swal("Handshake correctly rejected!", "", "success");
            }
        })
    };

});
