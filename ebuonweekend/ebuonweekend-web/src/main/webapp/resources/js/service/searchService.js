app.factory('SearchService', function () {
    var tipoRicerca;
    var user;

    var citta;
    var data;
    var location;
    var score;
    var setCity = function (city) {
        citta = city;
    };
    var getCity = function () {
        return citta;
    };

    var setData = function (date) {
        data = date;
    };
    var getData = function () {
        return data;
    };

    var setLocation = function (loc) {
        location = loc;
    };
    var getLocation = function () {
        return location;
    };

    var myScore = function (value){
        score = value;
        console.log(score);
    };
    var getScore = function (){
        return score;
    };

    var setTipoRicerca = function (tipo) {
        tipoRicerca = tipo;
    };
    var getTipoRicerca = function () {
        return tipoRicerca;
    };
    var setUser = function (u) {
        user = u;
    };
    var getUser = function () {
        return user;
    };

    return {
        getCity: getCity,
        setCity: setCity,
        setData: setData,
        getData: getData,
        setLocation: setLocation,
        getLocation: getLocation,
        myScore: myScore,
        getScore: getScore,
        getTipoRicerca: getTipoRicerca,
        setTipoRicerca: setTipoRicerca,
        getUser: getUser,
        setUser: setUser
    }
});