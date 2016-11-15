$(document).ready(function() {
	
	/* keep scroll position fixed over page refresh */
	(function(b){window.onbeforeunload=function(a){window.name+=" ["+b(window).scrollTop().toString()+"["+b(window).scrollLeft().toString()};b.maintainscroll=function(){if(0<window.name.indexOf("[")){var a=window.name.split("[");window.name=b.trim(a[0]);window.scrollTo(parseInt(a[a.length-1]),parseInt(a[a.length-2]))}};b.maintainscroll()})(jQuery);

/* WAYPOINT SECTION */	
	
	/* indicate selected ball
	   send the ball value to right/current answer (select by id) */

	//display selected ball in the GUI
	//removes all selected classes only from the current answer, check which button was clicked and add selected classes
	$('.custom-ball').on('click', function (){
		let balls = $(this).closest("ul").children();
		$(balls).removeClass('button-green-selected button-yellow-selected button-red-selected');
		
		switch($( this ).attr('class').split(' ')[1]){
			case 'button-green':
				$(this).addClass('button-green-selected').addClass('teacher-multichoice-selected');
				break;
			case 'button-yellow':
				$(this).addClass('button-yellow-selected').addClass('teacher-multichoice-selected');
				break;
			case 'button-red':
				$(this).addClass('button-red-selected').addClass('teacher-multichoice-selected');
				break;
			}
		var currentAnswerId = $(this).closest("div").prop("id");
		var currentBallValue = $(this).val();
		$('#' + currentAnswerId).val(currentBallValue);
	});
	
	/* WORKING CUSTOMBALL SELECTION DISPLAYER FUNCTION - OLD SOLUTION
	 $('.custom-ball').on('click', function (){
		let balls = $(this).closest("ul").children();
		$(balls).removeClass('button-green-selected button-yellow-selected button-red-selected');
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
	 */
	

	// change the text in the submit button when clicking
	$('.assessment-button').on('click', function (){
		var isCollapsed = $(this).hasClass('visible');
		if(isCollapsed) {
			$(this).removeClass('visible');
			$(this).html("Arviointi");
		} else {
			$(this).addClass('visible');
			$(this).html('Sulje');
		}
	});

	// check teacher's feedback values in textarea and multiselect, can only submit when has input otherwise send error
	$('.palaute').on('click', function (e){
		e.preventDefault();
		var id = $(this).val();
		var textarea = $(".teacher-assessment-text-" + id).val().trim().length > 0;
		var multichoice = $('#multichoices-'+ id).children().is('.teacher-multichoice-selected');
		if (textarea && multichoice){
			$('#saveFeedback-' + id).submit();
			console.log(id);
			$('.success-toast-' + id).delay(2000).fadeIn(1000).delay(2000).fadeOut(1000);
		} else {
			$('.error-toast-' + id).fadeIn(1000).delay(3000).fadeOut(1000);
		}
	});

	//rotate image right 90 degrees, image and rotateButton should be inside a container div in order for the selector to work
	var angle = 0;
	var rotateImage = function(target) {
		angle += 90
		$(target).css('transform','rotate(' + angle + 'deg)');
	};
	
	 $('#lightbox').on('click', function(e) {
		 let target = $(e.target);
		 target.is('img') ? rotateImage(target) : $('#lightbox').hide();
		});
		
	 //for Displaying clicked image in lightbox
	 //get the clicked image, reset angle for image rotation, set image css according window, append image and text, show lightbox
	$('.lightbox_trigger').on('click', function(e) {
		e.preventDefault();
		let image_href = $(this).attr("href");
		let image = '<img src="' + image_href + '"/>';
			angle = 0;
				$( this ).css('max-width', '' + (window.innerWidth - 50) + 'px').css('max-height', '' + (window.innerHeigt - 50) + 'px');
					$('#content').html(image);
					$('#content').append('<p style="margin-top: 50px; color: white">Klikkaa kääntääksesi kuvaa</p>');
					$('#lightbox').show();
		
		/*	for creating #lightbox dynamically
		if ($('#lightbox').length > 0) {
			} else {
			var lightbox = 
			'<div id="lightbox">' +
				'<p>Click to close</p>' +
				'<div id="content">' + 
					'<img src="' + image_href +'" />' +
				'</div>' +	
			'</div>';
			$('body').append(lightbox);
		} 
		*/
	});
});