var editGroup = function(id) {
	$.get('/passi/groupInfo?groupID=' + id, function(data) {
		$('#editGroupID').val(data.groupID);
		$('#editGroupName').val(data.groupName);
		$('#editGroupKey').val(data.groupKey);
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
	selectTab('add');
}