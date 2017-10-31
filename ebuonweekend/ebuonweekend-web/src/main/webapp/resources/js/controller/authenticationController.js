app.controller('authenticationController', function($location,$route, $cookies,$window, UserService) {
    this.fieldSignIn = {};
    this.fieldsLogin = {};

    this.signup = function (){
        //var pwd = sha256.convertToSHA256(registerData.s_pass);
        UserService.signup({
                nick: this.fieldSignIn.s_nick,
                mail: this.fieldSignIn.s_email,
                pass: this.fieldSignIn.s_pass
            },
            function(mysuccess){
                if(mysuccess.returnObject != null) {
                    $cookies.put('nick', mysuccess.returnObject.nickName);
                    $cookies.put('pass', mysuccess.returnObject.password);
                    $cookies.put('myProfilePicture', mysuccess.returnObject.profilePicture);

                    console.log("Success: Signup");
                    this.fieldSignIn = {};
                    swal({
                            title: "Registration Completed Successfully!",
                            text: "Welcome " + mysuccess.returnObject.nickName + "!!!\nGo to your account and add your personal informations and your houses",
                            type: "success"
                        },
                        function () {
                            $window.location.href = "/ebuonweekend-web/";
                            //$window.location.reload();
                        }
                    );
                }
                else{
                    swal("Error!", "Something went wrong during registration.\nPlease check your fields and try again.", "error");

                }

            },
            function(myerror){
                console.log("Internal error: Signup");
            });
    };

    this.login = function(){
        //var pwd = sha256.convertToSHA256(this.fieldsLogin.pass);
        UserService.login({
                nick : this.fieldsLogin.nick,
                pass : this.fieldsLogin.pass
            },
            function(mysuccess){
                console.log(mysuccess);
                if(mysuccess.returnObject != null) {
                    $cookies.put('nick', mysuccess.returnObject.nickName);
                    $cookies.put('pass', mysuccess.returnObject.password);
                    $cookies.put('myProfilePicture', mysuccess.returnObject.profilePicture);
                    $("#login-modal").remove();

                    this.fieldsLogin = {};

                    swal({
                            title: "Hi " + mysuccess.returnObject.firstName,
                            text: "Welcome back to E-Buonweekend!\nAre you ready to travel?",
                            type: "success"
                        },
                        function () {
                            window.location.href = "/ebuonweekend-web/";
                        });
                }
                else{
                    swal("Error!", "Your username or password is incorrect.\nPlease check them and try again.", "error");
                }
            },
            function(myerror) {
                swal("Error!", "Your username or password is incorrect.\nPlease check them and try again.", "error");
            });
    };
});




