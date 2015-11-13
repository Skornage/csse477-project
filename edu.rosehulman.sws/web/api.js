var api = function() {
	
	var get = function(id) {
		var gameId = "";
		if (id) gameId = id;
		var defer = $.Deferred();
		var url = "http://localhost:8080/games/" + gameId;
		generateAjaxRequestNoBody(url, "GET").done(function(response) {
			defer.resolve(response);
		});
		return defer.promise();
	};

	var postGame = function(data) {
		var defer = $.Deferred();
		var url = "http://localhost:8080/games/update/" + data.id;
		generateAjaxPostGame(url, "POST", data).done(function(response) {
			defer.resolve(response);
		});
		return defer.promise();
	}

	var postGuess = function(serverAddress, data) {
		var defer = $.Deferred();
		var url = "http://" + serverAddress + "/hangman/game/" + data.id;
		generateAjaxPostGuess(url, "POST", data).done(function(response) {
			defer.resolve(response);
		});
		return defer.promise();
	}

	var put = function(data) {
		var defer = $.Deferred();
		var url = "http://localhost:8080/games/create";
		generateAjaxRequestBody(url, "PUT", data).done(function(response) {
			defer.resolve(response);
		});
		return defer.promise();
	}

	var deleteGame = function(id) {
		var defer = $.Deferred();
		var url = "http://localhost:8080/games/delete/" + id;
		generateAjaxDeleteGame(url, "DELETE", id).done(function(response) {
			defer.resolve(response);
		});
		return defer.promise();
	}

	var generateAjaxRequestNoBody = function(url, method) {
		var defer = $.Deferred();
		var method = method.toUpperCase();
		$.ajax({
		    url : url,
		    type : method,
		    dataType : "json",
		    success : function(data) {
		        defer.resolve(data);
		    },
		    error : function(request,error)
		    {
		    	console.log(request);
		    	console.log(error);
		    	if (error == "parsererror") {
		    		defer.resolve(request.responseText);
		    	}
		    }
		});
		return defer.promise();
	};

	var generateAjaxRequestBody = function(url, method, data) {
		var defer = $.Deferred();
		var method = method.toUpperCase();
		$.ajax({
		    url : url,
		    beforeSend: function (request)
            {
            	request.setRequestHeader("name", data.name);
                request.setRequestHeader("posted-by", data.postedBy);
                request.setRequestHeader("word", data.word);
            },
		    type : method,
		    success : function(data) {
		        defer.resolve(data);
		    },
		    error : function(request,error)
		    {
		    	console.log(request);
		    	console.log(error);
		    }
		});
		return defer.promise();
	};

	var generateAjaxDeleteGame = function(url, method, data) {
		var defer = $.Deferred();
		var method = method.toUpperCase();
		$.ajax({
		    url : url,
		    type : method,
		    beforeSend: function (request)
            {
            	request.setRequestHeader("id", data);
            },
		    success : function(data) {
		        defer.resolve(data);
		    },
		    error : function(request,error)
		    {
		    	console.log(request);
		    	console.log(error);
		    }
		});
		return defer.promise();
	};

	var generateAjaxPostGame = function(url, method, data) {
		var defer = $.Deferred();
		var method = method.toUpperCase();
		$.ajax({
		    url : url,
		    type : method,
		    beforeSend: function (request)
            {
            	request.setRequestHeader("id", data.id);
            	request.setRequestHeader("name", data.name);
                request.setRequestHeader("posted-by", data.postedBy);
                request.setRequestHeader("word", data.word);
            },
		    success : function(data) {
		        defer.resolve(data);
		    },
		    error : function(request,error)
		    {
		    	console.log(request);
		    	console.log(error);
		    }
		});
		return defer.promise();
	};

	var generateAjaxPostGuess = function(url, method, data) {
		var defer = $.Deferred();
		var method = method.toUpperCase();
		$.ajax({
		    url : url,
		    type : method,
		    data : data.character,
		    success : function(data) {
		        defer.resolve(data);
		    },
		    error : function(request,error)
		    {
		    	console.log(request);
		    	console.log(error);
		    }
		});
		return defer.promise();
	};

	var generatePreflightAjax = function(url, method, data) {
		var defer = $.Deferred();
		var method = method.toUpperCase();
		$.ajax({
		    url : url,
		    headers: {'preflightTest': 'test'},
		    type : method,
		    success : function(data) {
		        defer.resolve(data);
		    },
		    error : function(request,error)
		    {
		    	console.log(request);
		    	console.log(error);
		    }
		});
		return defer.promise();
	};

	return {
		get : get,
		postGame : postGame,
		postGuess : postGuess,
		put : put,
		deleteGame : deleteGame
	}
}();