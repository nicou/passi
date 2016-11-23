var editGroup = function(id) {
	$.get('/passi/groupInfo?groupID=' + id, function(data) {
		$('#editGroupID').val(data.groupID);
		$('#editGroupName').val(data.groupName);
		$('#editGroupKey').val(data.groupKey);
		$('#edit-group-form, #edit-group-tab').removeClass('hidden');
		//$('#edit-group-info').addClass('hidden');
		selectTab('edit');
	});
}

var selectTab = function(tab) {
	$('#navTabs a[href="#' + tab + '"]').tab('show');
}

var clearEditFields = function() {
	$('#editGroupID').val(0);
	$('#editGroupName').val('');
	$('#editGroupKey').val('');
	$('#edit-group-form, #edit-group-tab').addClass('hidden');
	//$('#edit-group-info').removeClass('hidden');
	selectTab('users');
}