app.factory("UploaderService", function($resource){
	
	return $resource ("", {},{
		
		uploadProfile: {method: "POST",
						url:"/ebuonweekend-web/uploadImage",
					  	transformRequest: formDataObject,
			headers: { 'Content-Type': undefined , enctype:'multipart/form-data'}
			
			},
        uploadHouse: {method: "POST",
            url:"/ebuonweekend-web/uploadHouseImage",
            transformRequest: formDataObject,
            headers: { 'Content-Type': undefined , enctype:'multipart/form-data'}

        }
	})

});

function formDataObject (data) {
    var fd = new FormData();
    angular.forEach(data, function(value, key) {
			fd.append(key, value);
    });

    console.log(fd);

    console.log(angular.fromJson(data));

    return fd;
}
