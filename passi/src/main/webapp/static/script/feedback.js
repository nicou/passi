var currentlyWorking = false;

// Fix console for IE
if (typeof console == 'undefined') {
	this.console = { log: function() {} };
}

function submitFeedback() {
	// Don't run if request is pending
	if (currentlyWorking) return;
	setWorking(true);
	
	// Initialize values
	var waypoints = [];
	var answersheetFeedbackText = document.getElementById('instructor-comment').value;
	var feedbackComplete = document.querySelectorAll('.feedback-complete')[0].checked;
	var answersheetId = document.querySelectorAll('.answersheet-id')[0].value;
	
	// Get waypoint feedback values
	var forms = document.querySelectorAll('.waypointform');
	for (var i = 0; i < forms.length; i++) {
		var inputs = forms[i].getElementsByTagName('input');
		var textarea = forms[i].getElementsByTagName('textarea')[0].value;
		var waypointId = null;
		var instructorRating = null;
		for (var j = 0; j < inputs.length; j++) {
			if (inputs[j].name === 'answerWaypointID') {
				waypointId = inputs[j].value;
			} else if (inputs[j].name === 'instructorRating' && inputs[j].checked) {
				instructorRating = inputs[j].value;
			}
		}
		// Push waypoint feedback to array
		waypoints.push({ 'waypointID': waypointId, 'instructorRating': instructorRating === null ? 0 : instructorRating, 'instructorComment': textarea });
	}
	
	// Create object containing all feedback
	var worksheetFeedback = { 
			'answersheetId': answersheetId,
			'feedbackComplete': feedbackComplete,
			'feedback': answersheetFeedbackText,
			'waypoints': waypoints
	};
	saveWorksheetFeedback(worksheetFeedback);
}

// Save feedback
function saveWorksheetFeedback(feedback) {
	var answersheet = JSON.stringify(generateAnswersheet(feedback));
	var csrf = getCsrfToken();
	$.ajax({
		url: '/passi/saveFeedback',
		method: 'POST',
		data: answersheet,
		contentType: 'application/json; charset=utf-8',
		headers: { "X-CSRF-TOKEN": csrf.token },
		error: function(response) {
			console.log('Error saving feedback', response);
			$('.error-toast').fadeIn(200).delay(1100).fadeOut(200);
		},
		success: function(response) {
			// Reload page if feedback is saved succesfully
			console.log('Feedback saved succesfully', response);
			$('.success-toast').fadeIn(200).delay(1100).fadeOut(200);
			var location = window.location.href.substring(0, window.location.href.lastIndexOf('/')) + '/feedbackok';
			window.setTimeout(function() { window.location.href = location }, 1500);
		}
	}).always(function() {
		setWorking(false);
	});
}

function generateAnswersheet(worksheetFeedback) {
	var waypoints = [];
	for (var i = 0; i < worksheetFeedback.waypoints.length; i++) {
		waypoints.push(generateAnswerpoint(worksheetFeedback.waypoints[i]));
	}
	return { 'answerID': worksheetFeedback.answersheetId, 'answerPlanning': '', 'answerInstructorComment': worksheetFeedback.feedback, 'answerTimestamp': 0, 'worksheetID': 0, 'groupID': 0, 'userID': 0, 'waypoints': waypoints, 'feedbackComplete': worksheetFeedback.feedbackComplete };
}

function generateAnswerpoint(waypoint) {
	return { 'answerWaypointID': waypoint.waypointID, 'answerWaypointText': '', 'answerWaypointInstructorComment': waypoint.instructorComment, 'answerWaypointInstructorRating': waypoint.instructorRating, 'answerWaypointImageURL': '', 'answerID': 0, 'waypointID': 0, 'optionID': 0, 'optionText': '' };
}

function getCsrfToken() {
	return { 'token': document.getElementById('csrf-token').value, 'header': document.getElementById('csrf-header').value };
}

function setWorking(working) {
	currentlyWorking = working;
	if (working) {
		$('#feedback-btn').addClass('disabled');
		$('#feedback-btn').html('<span class=\'glyphicon glyphicon-repeat spinner\'></span> Tallenna');
	} else {
		$('#feedback-btn').removeClass('disabled');
		$('#feedback-btn').html('Tallenna');
	}
}