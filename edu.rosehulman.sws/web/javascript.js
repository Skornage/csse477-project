var generateGameTemplate = function(game) {
	var numChars = game["current-word"].length;
	var html = "<li data-id='"+ game["id"] +"' data-chars='" + numChars + "'>\
			<button class='btn btn-danger delete-game'><b>x</b></button>\
			<button class='btn btn-primary edit-game'>Edit</button>\
			<button class='btn btn-success join-game'>Join Game</button>\
			<div class='row game-name'>" + game["name"] + "</div>\
			<div class='row game-author'>Created by <span class='game-author-name'>" + game["posted-by"] + "</span></div>\
		</li>";
	return html;
}

var populateGames = function() {
	$("#list-container").html("");
	api.get().done(function(games) {
		for (var i = 0; i < games.length; i++) {
			var html = generateGameTemplate(games[i]);
			$("#list-container").append(html);
		}
	});
}

var setImage = function(strikes) {
	var picContainer = $("#hangman-pic-container");
	picContainer.html("");
	picContainer.append("<img src='hangman" + strikes +".bmp' />");
}

var updateWordTable = function(guess, word) {
	var guessesTable = $("#guesses-table");
	guessesTable.html("");
	var word = word.split(":");
	var strikes = word[2];
	var phrase = word[0];
	var word = word[0].split("");
	if (phrase == "you won!") {
		// do nothing
	}
	else if (phrase == "you lose!") {
		setImage(6);
	}
	else if(word.indexOf(guess) === -1)
	{
	  setImage(strikes);
	}
	for (var i = 0; i < word.length; i++) {
		var guess = "<span class='char-block'>" + word[i] + "</span>";
		guessesTable.append(guess);
	}
}

$(document).ready(function() {

	populateGames();

	$("#modal-create-game-add").on("click", function() {
		var name = $("#modal-game-name");
		var postedBy = $("#modal-author-name");
		var word = $("#modal-game-word");
		var data = {
			name : name.val(),
			postedBy : postedBy.val(),
			word : word.val()
		};
		api.put(data).done(function(response) {
			populateGames();
			name.val("");
			postedBy.val("");
			word.val("");
			$("#create-game-modal-container").modal("hide");
		});
	});

	$("#modal-create-game-edit").on("click", function() {
			var name = $("#modal-game-name");
			var postedBy = $("#modal-author-name");
			var word = $("#modal-game-word");
			var id = $(this).closest("#create-game-modal-container").data("id");
			var data = {
				id : id,
				name : name.val(),
				postedBy : postedBy.val(),
				word : word.val()
			};
			api.postGame(data).done(function(response) {
				populateGames();
				name.val("");
				postedBy.val("");
				word.val("");
				$("#create-game-modal-container").modal("hide");
			});
		});

	$("#create-game").on("click", function() {
		var modal = $("#create-game-modal-container");
		modal.modal("show");
		modal.find("#modal-create-game-edit").addClass("hide");
		modal.find("#modal-create-game-add").removeClass("hide");
	});

	$("#list-container").on("click", ".delete-game", function() {
		var li = $(this).parent();
		var id = li.data("id");
		api.deleteGame(id).done(function() {
			$("#list-container").find("[data-id='" + id + "']").remove();
		});
	});

	$("#list-container").on("click", ".edit-game", function() {

		var modal = $("#create-game-modal-container");
		var li = $(this).parent();
		var id = li.data("id");

		var name = $("#modal-game-name");
		name.val(li.find(".game-name").html());
		var postedBy = $("#modal-author-name");
		postedBy.val(li.find(".game-author-name").html());
		modal.data("id", id);
		modal.find("#modal-create-game-add").addClass("hide");
		modal.find("#modal-create-game-edit").removeClass("hide");
		modal.modal("show");
	});

	$("#list-container").on("click", ".join-game", function() {

		var li = $(this).closest("li");

		var game = {
			id : li.data("id"),
			name : li.find(".game-name").html(),
			postedBy : li.find(".game-author-name").html(),
			numberOfChars : li.data("chars")
		};

		api.get("play/" + li.data("id")).done(function(serverAddress) {

			$("#lobby-container").addClass("hide");

			var gamePage = $("#game-container");

			var guess = "<span class='char-block'>*</span>";
			var guessesTable = $("#guesses-table");

			for (var i = 0; i < game.numberOfChars; i++) {
				guessesTable.append(guess);
			}

			gamePage.find("#game-title").text(game["name"]);
			gamePage.find("#game-author").text(game["postedBy"]);

			gamePage.find("#character-input-submit").on("click", function() {
				var characterInput = $("#character-input");
				if (characterInput.val()) {
					var data = {
						id : li.data("id"),
						character : characterInput.val()
					}
					api.postGuess(serverAddress, data).done(function(result) {
						var character = characterInput.val();
						updateWordTable(character, result);
						characterInput.val("");
					});
				}
			});

			gamePage.removeClass("hide");
		});
	});

	setInterval(function() { populateGames() }, (5 * 1000));
});