var getGroupUsers = function(id) {
	$.get('/passi/groupInfoUsers?groupID=' + id, function(data) {
		var memberRows = '';
		if (data.users.length === 0) {
			$('#group-users-table').addClass('hidden');
		} else {
			$('#group-users-table').removeClass('hidden');
		}
		for (var i = 0; i < data.users.length; i++) {
			memberRows += renderMemberRow(data.users[i], data.group.groupID);
		}
		$('#group-users-info').html(renderGroupInfo(data.group, data.users.length));;
		$('#group-users-tbody').html(memberRows);
		selectTab('users');
	});
}

var renderMemberRow = function(user, groupID) {
	var memberFullname = user.firstname + ' ' + user.lastname;
	var deleteMemberButton = '<button onclick="confirmMemberRemoval(' + user.userID + ', ' + groupID + ', \'' + memberFullname +'\'); this.blur();" type="button" class="btn btn-secondary" title="Poista opiskelija ryhmästä"><span class="glyphicon glyphicon-remove"></span></button>';
	return '<tr><td>' + memberFullname + '</td><td class="text-center">' + deleteMemberButton + '</td></tr>';
}

var confirmMemberRemoval = function(userID, groupID, memberFullname) {
	var confirmationMessage = 'Haluatko varmasti poistaa opiskelijan ' + memberFullname + ' ryhmästä?';
	if (confirm(confirmationMessage)) {
		deleteGroupMember(userID, groupID);
	}
}

var deleteGroupMember = function(userID, groupID) {
	console.log('Poistetaan opiskelija ID ' + userID + ' ryhmästä ID ' + groupID);
	var params = 'userID=' + userID + '&groupID=' + groupID;
	$.get('/passi/delGroupMember?' + params, function(data) {
		if (data.status === true) {
			console.log('Opiskelija poistettu onnistuneesti!');
			getGroupUsers(groupID);
		} else {
			console.log('Opiskelijan poistossa tapahtui virhe!');
		}
	});
}

var renderGroupInfo = function(group, studentCount) {
	var groupName = group.groupName;
	if (studentCount === 0) {
		return '<strong>Ryhmän nimi ja tunnus:</strong><br/>' + groupName + '<br/><br/>Ryhmässä ei ole vielä yhtään opiskelijaa.';
	}
	var studentWord = 'opiskelijaa';
	if (studentCount === 1) {
		studentWord = 'opiskelija';
	}
	return '<strong>Ryhmän nimi ja tunnus:</strong><br/>' + groupName + '<br /><br />Ryhmässä on yhteensä <strong>' + studentCount + '</strong> ' + studentWord + '.';
}