$(document).ready(function() {
	
	/* keep scroll position fixed over page refresh */
	(function(b){window.onbeforeunload=function(a){window.name+=" ["+b(window).scrollTop().toString()+"["+b(window).scrollLeft().toString()};b.maintainscroll=function(){if(0<window.name.indexOf("[")){var a=window.name.split("[");window.name=b.trim(a[0]);window.scrollTo(parseInt(a[a.length-1]),parseInt(a[a.length-2]))}};b.maintainscroll()})(jQuery);

/* WAYPOINT SECTION */	
	
	/* indicate selected ball
	   send the ball value to right/current answer (select by id) */
	$('.custom-ball').on('click', function (){
		$('.custom-ball').removeClass('button-green-selected button-yellow-selected button-red-selected');
		if ($(this).hasClass('button-green')) {
			$(this).addClass('button-green-selected');
			$(this).addClass('teacher-multichoice-selected');
		} else if ($(this).hasClass('button-yellow')) {
			$(this).addClass('button-yellow-selected');
			$(this).addClass('teacher-multichoice-selected');
		} else if ($(this).hasClass('button-red')) {
			$(this).addClass('button-red-selected');
			$(this).addClass('teacher-multichoice-selected');
		}
		var currentAnswerId = $(this).closest("div").prop("id");
		var currentBallValue = $(this).val();
		$('#' + currentAnswerId).val(currentBallValue);
	});

	// change the text in the submit button when clicking
	$('.assesment-button').on('click', function (){
		var isCollapsed = $(this).hasClass('visible');
		if(isCollapsed) {
			$(this).removeClass('visible');
			$(this).html("Arvioi");
		} else {
			$(this).addClass('visible');
			$(this).html('Piilota');
		}
	});

	// check teacher's feedback values in textarea and multiselect, can only submit when has input otherwise send error
	$('.palaute').on('click', function (e){
		e.preventDefault();
		var id = $(this).val();
		var textarea = $(".teacher-assesment-text-" + id).val().trim().length > 0;
		var multichoice = $('#multichoices-'+ id).children().is('.teacher-multichoice-selected');
		if (textarea && multichoice){
			console.log("joo");
			$('#saveFeedback-' + id).submit();
			$('.success-toast-' + id).delay(2000).fadeIn(1000).delay(2000).fadeOut(1000);
		} else {
			console.log("ei");
			$('.error-toast-' + id).fadeIn(1000).delay(3000).fadeOut(1000);
		}
	});

	/* rotate image right 90 degrees, image and rotateButton should be inside a container div in order for the selector to work
	var angle = 0;
	 $('.rotateButton').on('click', function() {
		angle += 90
		var elem = $(this).prev();
			elem.css('transform','rotate(' + angle + 'deg)');
	});
	*/
	
});