/**
 * Created by Pasquale on 10/02/2017.
 */

app.factory('ShowHouseService', function () {
    var item = {};
    var setItem = function (house) {
        item = house;
    };
    var getItem = function () {
        return item;
    };
    return {
        getItem: getItem,
        setItem: setItem
    }
});
