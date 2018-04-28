
function insertParam(key, value) {
        key = escape(key); 
        value = escape(value);

        var kvp = document.location.search.substr(1).split('&');
        if (kvp == '') {
            document.location.search = '?' + key + '=' + value;
        }
        else {

            var i = kvp.length; var x; while (i--) {
                x = kvp[i].split('=');

                if (x[0] == key) {
                    x[1] = value;
                    kvp[i] = x.join('=');
                    break;
                }
            }

            if (i < 0) { kvp[kvp.length] = [key, value].join('='); }

            //this will reload the page, it's likely better to store this until finished
            document.location.search = kvp.join('&');
        }
 }

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,    
    function(m,key,value) {
      vars[key] = value;
    });
    return vars;
 }


/**
 * Submits a form by id in 5 seconds.
 * 
 * @param id the element id.
 */
function submitWithTimer (id) {
	console.log ("Starting submitWithTimer.");
	setTimeout(function(){ 
		$("#"+id).submit(); 
	}, 5000);
	console.log ("Starting submitWithTimer.");
}

/**
 * Hides an element by id in 5 seconds.
 * 
 * @param id the element id.
 */
function hideWithTimer (id) {
	console.log ("Starting hideWithTimer.");
	setTimeout(function(){ 
		$("#"+id).fadeOut('slow'); 
	}, 7000);
	console.log ("Finishing hideWithTimer.");
}

/**
 * Decodes an encoded URI.
 * 
 */
function urldecode (str) {
	return decodeURIComponent ((str+'').replace(/\+/g, '%20'));
}

/**
 * Hides the message container according with its container.
 * 
 * @param containerId the container id. 
 */
function hideMessageByContainer (containerId) {
	$("#"+containerId).fadeOut('slow');
}

/**
 * Hides the message container.
 */
function hideMessage () {
	$("#container-message").fadeOut('slow');
}

/**
 * Checks if a given field value is empty or not.
 * 
 * @param value the field value.
 */
function isEmpty (value) {
	var success = false;
	if (value == null || value.length == 0) {
		success = true;
	}
	return success;
}

/**
 * JQuery function that redirects to a specified url.
 * 
 * @param url the url.
 */
function redirectTo (url) {
	$(location).attr ('href', url);
}

/**
 * Checks if a given email is valid or not.
 * 
 * @param url the url.
 */
function isValidEmail (value) {
    var PATTERN =/^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
    if (PATTERN.test (value)){         
		return true;
    } else {
    	return false; 
    }
}

/**
 * Cleans the fields values given their ids.
 * 
 * @param idFieldArr the id field array.
 * 
 */
function cleanFields (idFieldArr) {
	for (var i = 0; i < idFieldArr.length; i++) {
		var id = idFieldArr[i];
		$("#"+id).val("");
	}
}

/**
 * Gets the url parameters from the URL.
 * 
 * @returns url get parameters
 */
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

/**
 * Disables a style sheet given its href.
 */
function disableStyleSheet (href) {
	var styleSheets = document.styleSheets;
	for (var i = 0; i < styleSheets.length; i++) {
	    if (styleSheets[i].href.indexOf (href) != -1) {
	        styleSheets[i].disabled = true;
	        console.log ('disabling styleSheet: ' + styleSheets[i].href);
	        break;
	    }
	}
}

/**
 * Checks or not all the table checkbox items.
 */
function changeCheckboxesStatus (prefixId) {
	var inputArr = document.getElementsByTagName ("input");
	var statusAll = document.getElementById ("chkSelectAll").checked;
	for (var i = 0; i < inputArr.length; i++) {
		var input = inputArr[i];
		if (input.id.indexOf( prefixId ) != -1) {
			input.checked = (statusAll) ? true : false;
		}
	}
}

/**
 * Gets the row index from a given string.
 * 
 * @param str the string.
 * @returns the index.
 */
function getRowIndexFromStr (str) {
	var PATTERN  = null;
	var rowIndex = null;
	var index    = null;
	PATTERN = "[0-9]";
	index = str.search (PATTERN);
	if (index != -1) {
		rowIndex = str.charAt (index);
	}	
	return rowIndex;
}

/**
 * Opens a url in a new tab.
 */
function openInNewTab (url, scrollToTarget) {
	//$('body').scrollTo (scrollToTarget);
	var win = window.open (url, '_blank');
	win.focus();
}

/**
 * Sets the window height to the given element.
 */
function setWindowHeightTo (id) {
	var newHeight = $(document).height();
	//more height of messages div
	newHeight = parseInt (newHeight) + 42;
	$('#'+id).css("height", newHeight);
	console.log ('setWindowHeightTo->newHeight: ' + newHeight);
}

/**
 * Configures the height of an element according with the window height.
 */
function configureHeight (divId) {
	var divHeight = $('#'+divId).css("height");
	var windowHeight = window.innerHeight;
	
	divHeight = parseInt (divHeight);
	windowHeight = parseInt (windowHeight) - 80;
	
	if (divHeight < windowHeight) {
		$('#'+divId).css ("height", windowHeight);
	}
	console.log ('divHeight: ' + divHeight);
	console.log ('windowHeight: ' + windowHeight);
}

/**
 * Resets the checkboxes with a specific id.
 */
function resetCheckboxes (id) {
	console.log ("Start resetCheckboxes, id->"+id);
	var inputArr = document.getElementsByTagName ('input');
	for (var i = 0; i < inputArr.length; i++) {
		var input = inputArr[i];
		if (input.id == id) {
			input.checked = false;							
		}
	}
}
