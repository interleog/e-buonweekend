/**
 * Created by lorenzogagliani on 13/02/17.
 */
app.factory('EditHouseService', function () {
    var isEdit;

    var idHouse;


    var setIsEdit = function (value) {
        isEdit = value;
    };
    var getIsEdit = function () {
        return isEdit;
    };

    var setIdHouse = function (value_id) {
        idHouse = value_id;
    };
    var getIdHouse = function () {
        return idHouse;
    };


    return {
        getIsEdit: getIsEdit,
        setIsEdit: setIsEdit,
        getIdHouse: getIdHouse,
        setIdHouse: setIdHouse

    }
});