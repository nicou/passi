var getGroupUsers = function(id) {
	$.get('/passi/groupInfoUsers?groupID=' + id, function(data) {
		var userRows = '';
		if (data.users.length === 0) {
			$('#group-users-table').addClass('hidden');
		} else {
			$('#group-users-table').removeClass('hidden');
		}
		for (var i = 0; i < data.users.length; i++) {
			userRows += renderUserRow(data.users[i], data.group.groupID);
		}
		$('#group-users-info').html(renderGroupInfo(data.group, data.users.length));;
		$('#group-users-tbody').html(userRows);
		selectTab('users');
	});
}

var renderUserRow = function(user, groupID) {
	var userFullname = user.firstname + ' ' + user.lastname;
	var deleteUserButton = '<button onclick="removeUserFromGroup(' + user.userID + ', ' + groupID + ', \'' + userFullname +'\'); this.blur();" type="button" class="btn btn-secondary" title="Poista opiskelija ryhmästä"><span class="glyphicon glyphicon-remove"></span></button>';
	return '<tr><td>' + userFullname + '</td><td class="text-center">' + deleteUserButton + '</td></tr>';
}

var removeUserFromGroup = function(userID, groupID, userFullname) {
	var confirmationMessage = 'Haluatko varmasti poistaa opiskelijan ' + userFullname + ' ryhmästä?';
	if (confirm(confirmationMessage)) {
		console.log('Poistetaan opiskelija ID ' + userID + ' (' + userFullname + ') ryhmästä ID ' + groupID);
	}
}

var renderGroupInfo = function(group, studentCount) {
	var groupName = group.groupName;
	if (studentCount === 0) {
		return '<strong>Ryhmä:</strong><br/>' + groupName + '<br/><br/>Ryhmässä ei ole vielä yhtään opiskelijaa.';
	}
	var studentWord = 'opiskelijaa';
	if (studentCount === 1) {
		studentWord = 'opiskelija';
	}
	return '<strong>Ryhmä:</strong><br/>' + groupName + '<br /><br />Ryhmässä on yhteensä <strong>' + studentCount + '</strong> ' + studentWord + '.';
}