/**
 * Created by lorenzogagliani on 14/02/17.
 */
function activeMultiDatepicker(){
    var today = new Date();
     $('#textbox_dates').multiDatesPicker({
         maxPicks: 8,
         dateFormat: "yy-mm-dd",
         beforeShowDay: function(date) {
             if(   date.getDay() == 6  && date > today  ){
                 return [true];
             } else {
                 return [false];
             }
         }
     });
}

function activeDatepicker(){
    var today = new Date();
    $("#search_by_dates").click(function() {
        $(this).datepicker({
            dateFormat: "yy-mm-dd",
            beforeShowDay: function(date) {
                if(   date.getDay() == 6  && date > today  ){
                    return [true];
                } else {
                    return [false];
                }
            }
        }).datepicker( "show" )
    });
}
