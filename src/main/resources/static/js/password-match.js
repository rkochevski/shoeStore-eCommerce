function checkPasswordMatch() {
	var password = $("#txtNewPassword").val();
	var confirmPassword = $("#txtConfirmPassword").val();
	if (password == "" && confirmPassword == "") {
		$("#updateUserInfoButton").attr("disabled", false);
	} else {
		if (password != confirmPassword) {
			$("#updateUserInfoButton").attr("disabled", true);				
			$("#txtConfirmPassword").addClass("is-invalid");
		} else {
			$("#updateUserInfoButton").attr("disabled", false);
			$("#txtConfirmPassword").removeClass("is-invalid");
		}
	}		
}

$(document).ready(function(){
	$("#txtConfirmPassword").keyup(checkPasswordMatch);
	$("#txtNewPassword").keyup(checkPasswordMatch);
	$('#updateUserInfoButton').click(function() {
		if ($('#txtNewPassword').val() == '') {   
			$('#profileForm').submit()    		
		} else {
			$('#passwordModal').modal('show');
		}
	 });
});