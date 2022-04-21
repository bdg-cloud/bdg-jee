//<![CDATA[ 
	

    function toggleListWest() {
    	//PF('widgetLayoutPage').toggle('west');
    	PF('widgetVarTabViewLeft').select(PF('widgetVarTabViewLeft').getLength()-1);
    }
    
    function activeLastTabCenter() {
    	PF('widgetVarTabView').select(PF('widgetVarTabView').getLength()-1);
    }
    
    function activeTabCenter(laClasse) {
    	$('li.tab').each(function(index) { 
    	   if($(this).hasClass(laClasse)) {
    		   PF('widgetVarTabView').select(index);
    		   //console.log( "test ***** "+$(this).attr('tabindex')+"   "+index);
    	   }
    	});
    }

    	
	function toggleListeMenuGauche() {
		$('#left,#right').show();
		if(!$("#left").hasClass("Container50")) {
			$('#left').removeClass('Container10').addClass('Container50');
			$('#right').removeClass('Container90').addClass('Container50');
		}
		//PF('left').toggle();
	} 
	
	function toggleListeResize() {
        if($("#left").hasClass("Container50")) {
        	$('#left').removeClass('Container50').addClass('Container10');
 			$('#right').removeClass('Container50').addClass('Container90');
        } else {
        	$('#left').removeClass('Container10').addClass('Container50');
  			$('#right').removeClass('Container90').addClass('Container50');
        }
      
    } 
    
    
    $( document ).ready(function() {
    	/*
         * Primefaces Bug 5241 => La selection ne fonctionne pas avec les tables éditables
         * https://code.google.com/p/primefaces/issues/detail?id=5241
         */
        //Debut Primefaces Bug 5241
        console.log( "ready!" );
        var funcMakeCellEditorClickableAndOnContextMenuable = function() {
            $(this).closest(".ui-editable-column").trigger("click");
        };
        
        $(document).on("click", ".ui-cell-editor", function(e) { $.proxy(funcMakeCellEditorClickableAndOnContextMenuable, this)(); } );
        $(document).on("contextmenu", ".ui-cell-editor", function(e) { $.proxy(funcMakeCellEditorClickableAndOnContextMenuable, this)(); } );
        // Fin Primefaces Bug 5241
        
        $('#left,#right').hide();
        $(document).on("click", "a.liste_menu_gauche", function() { toggleListeMenuGauche(); } );
        $(document).on("click", "#horizontal_toggler", function() { toggleListeResize(); } );
        
    });
   
    
//]]>