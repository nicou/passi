$(document).ready(function() {
	
	/* keep scroll position fixed over page refresh */
	(function(b){window.onbeforeunload=function(a){window.name+=" ["+b(window).scrollTop().toString()+"["+b(window).scrollLeft().toString()};b.maintainscroll=function(){if(0<window.name.indexOf("[")){var a=window.name.split("[");window.name=b.trim(a[0]);window.scrollTo(parseInt(a[a.length-1]),parseInt(a[a.length-2]))}};b.maintainscroll()})(jQuery);	

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
		var multichoice = $('#saveFeedback-' + id).find('input[name="instructorRating"]:checked').val() > 0;
		if (textarea && multichoice){
			$('#saveFeedback-' + id).submit();
			$('.success-toast-' + id).delay(2000).fadeIn(1000).delay(2000).fadeOut(1000);
		} else {
			if ($('.error-toast-' + id).queue().length < 1) {
				$('.error-toast-' + id).fadeIn(1000).delay(3000).fadeOut(1000);
			}
		}
	});
	
	var scaleImage = function() {
		var $img = $('#zImage'),
	    maxWidth = $(window).width(),
	    maxHeight = $(window).height(),
	    imageWidth, imageHeight;
		
	//toggle width and height properties if image rotated	
		imageWidth = $img[0].width;
		imageHeight = $img[0].height;
		
		var ratio = [maxWidth / imageWidth, maxHeight / imageHeight ];
		ratio = Math.min(ratio[0], ratio[1]);
		
	//resize the image relative to the ratio
		$img.attr('width', imageWidth * ratio)
	    	.attr('height', imageHeight * ratio);

	//align the image vertically and horizontally
		if(imageRotateCounter % 2 !== 0) {
			$img.css({
				maxHeight: maxHeight * .5,
				maxWidth: maxWidth * .5
			});
		}
		else {
			$img.css({
				margin: 'auto',
				position: 'absolute',
				maxHeight: maxHeight * .75,
				maxWidth: maxWidth * .75,
				top: 0,
				bottom: 0,
				left: 0,
				right: 0
			});
		}
	}
	
	var imageRotateCounter = 0;
	
	//rotate image right 90 degrees, scale the image accordingly (swap height, and width properties)
	var angle = 0;
	var rotateImage = function(target) {
		angle += 90
		$(target).css('transform','rotate(' + angle + 'deg)');
		//let condition = $('#zImage').position().top;
			imageRotateCounter++;
			scaleImage();
	};
	
	//click event listener for rotating or hiding the image/lightbox
	 $('#lightbox').on('click', function(e) {
		 let target = $(e.target);
		 target.is('img') ? rotateImage(target) : $('#lightbox').hide();
		});
		
	 //for Displaying clicked image in lightbox
	 //get the clicked image, reset angle for image rotation, set image css according window, append and scale image, show lightbox
	$('.lightbox_trigger').on('click', function(e) {
		e.preventDefault();
		let image_href = $(this).attr("href");
		var image = '<img src="' + image_href + '" id="zImage"/>';
			angle = 0;
			$('#content').html(image);
			  $(image).on('load', function(){
				  scaleImage(false);
					if($('.glyphicon-repeat').length < 1){
						$('#content').append('<div id="rotateiconContainer"><p><span class="glyphicon glyphicon-repeat text-center" style="padding-right: 5px;"></span> Klikkaa kuvaa</p></div>');
					}	
				});
			  
	//Scale image each time the browser window is resized	  
	$(window).on('resize', function(){
			scaleImage();
	});
			  
			  /*
			   * if needed init in lightbox_trigger: //wWidth = $(window).width(), wHeight = $(window).height();
			 var  wWidth, wHeight, iWidth, iHeigth
			  if(imageWidth > wWidth || imageHeigth > wHeight){
				  while(imageWidth > wWidth || imageHeigth > wHeight ) {
					  	imageWidth *= 0,8;
					  	imageHeigth *= 0,8;
					  }
			  }
			*/
		
			$('#lightbox').show();
	});
});


/* OLD IMAGE SCALE / BACKUP
	 * 	var scaleImage = function(rotated) {
		var $img = $('#zImage'),
	    maxWidth = $(window).width(),
	    maxHeight = $(window).height(),
	    widthRatio = maxWidth / imageWidth,
	    heightRatio = maxHeight / imageHeight;
	//toggle width and height properties if image rotated	
		if(rotated) {
			var imageWidth = $img[0].height,
		    	imageHeight =  $img[0].width
		} else {
			var imageWidth = $img[0].width,
		    	imageHeight = $img[0].height
		}

	var ratio = widthRatio;
	console.log(maxWidth / imageWidth);
		if (widthRatio * imageHeight > maxHeight) {
			ratio = heightRatio;
		}
	
	//resize the image relative to the ratio
		$img.attr('width', imageWidth * ratio)
	    	.attr('height', imageHeight * ratio);

	//align the image vertically and horizontally
		$img.css({
			margin: 'auto',
			position: 'absolute',
			top: 0,
			bottom: 0,
			left: 0,
			right: 0
		});
	}	
 */