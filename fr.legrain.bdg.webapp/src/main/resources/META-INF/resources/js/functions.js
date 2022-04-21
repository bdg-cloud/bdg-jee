//<![CDATA[ 
var ongletActifTabViewLigneFabrication=0;
var differenceReglement=false;


//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
var oauthWindowHandle;

function oauthWindowGoogle(url) {
//	oauthWindowHandle = window.open(url,'mywindow', 'resizable=no,toolbar=no,scrollbars=yes,height=450,width=530,top=145,left=235');
	oauthWindowHandle.location = url;
	var timer = setInterval(function() { 
		if (oauthWindowHandle.closed) {
			clearInterval(timer);
			finGoogle();
		}
	}, 500);
}
function oauthWindowMicrosoft(url) {
//	oauthWindowHandle = window.open(url,'mywindow', 'resizable=no,toolbar=no,scrollbars=yes,height=450,width=530,top=145,left=235');
	oauthWindowHandle.location = url;
	var timer = setInterval(function() { 
		if (oauthWindowHandle.closed) {
			clearInterval(timer);
			finMicrosoft();
		}
	}, 500);
}
function oauthWindowStripeConnect(url) {
//	oauthWindowHandle = window.open(url,'mywindow', 'resizable=no,toolbar=no,scrollbars=yes,height=450,width=530,top=145,left=235');
	oauthWindowHandle.location = url;
	var timer = setInterval(function() { 
		if (oauthWindowHandle.closed) {
			clearInterval(timer);
			finStripeConnect();
		}
	}, 500);
}
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

//Utilisation du plugin JQuery bulleyes pour la gestion du scrolling, entré et sortie d'un element dans le viewport suite au scroll
//https://www.pixeltango.com/resources/web-development/jquery-bullseye-viewport-detection-events/
$.fn.lgrBullEye = function (typedoc) {

  setTimeout(function () { // setTimeout really not required, just wanted to make the effect more obvious
		$(typedoc+'.bullseye-btn-wizard-document')
		.bind('enterviewport', { td: typedoc }, function(event) {
			btnWizardFixed(event.data.td);
		 })
		.bind('leaveviewport', { td: typedoc }, function(event) {
			btnWizardFloat(event.data.td);
		 })
		.bullseye();
	       // .addClass('outline');
  }, 500);
};

//fonction pour savoir si un element est visible dans le viewport à l'instant t
$.fn.isInViewport = function() {
    var elementTop = $(this).offset().top;
    var elementBottom = elementTop + $(this).outerHeight();

    var viewportTop = $(window).scrollTop();
    var viewportBottom = viewportTop + $(window).height();

    return elementBottom > viewportTop && elementTop < viewportBottom;
};

//Fonction pour gérer l'affichage 'flottant' des boutons back et next des wizard des documents
function initialisePositionBoutonWizard(typedoc) {
	$().lgrBullEye(typedoc);
    
	if ($(typedoc+'.btn-wizard-document-tous').isInViewport()) {
		btnWizardFixed(typedoc);
    } else {
    	btnWizardFloat(typedoc);
    }
}
//Barre de bouton en bas du wizard (fixe sous le wizard car visible)
function btnWizardFixed(typedoc) {  
	 $(typedoc+".btn-wizard-document-left").removeClass('btn-wizard-float');
     $(typedoc+".btn-wizard-document-right").removeClass('btn-wizard-float-right');
     
     $(typedoc+".btn-wizard-document-left").addClass('btn-wizard-fixed');
     $(typedoc+".btn-wizard-document-tous").addClass('btn-wizard-fixed');
}
//Barre de bouton en bas de l'écran (flottant car bas du wizard invisible)
function btnWizardFloat(typedoc) {  
	$(typedoc+".btn-wizard-document-left").addClass('btn-wizard-float');
	$(typedoc+".btn-wizard-document-right").addClass('btn-wizard-float-right');
	
	$(typedoc+".btn-wizard-document-left").removeClass('btn-wizard-fixed');
	$(typedoc+".btn-wizard-document-tous").removeClass('btn-wizard-fixed');
}



//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
//http://soahowto.blogspot.fr/2014/02/display-busy-mouse-cursor-with.html
//http://stackoverflow.com/questions/15737801/change-mouse-cursor-to-busy-mode-when-primefaces-ajax-request-is-in-progress
	var handle = {}  
	
	function on_start() {  
	    handle = setTimeout(function() {  
	        $('html').addClass('progress')  
	    }, 0)  
	}  
	  
	function on_complete() {  
	    clearTimeout(handle)  
	    $('html').removeClass('progress')  
	}  
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    function toggleListWest() {
    	//Selection du dernier onglet du composant : widgetVarTabViewLeft
    	//plus utilise, remplacer par le binding des differents tabView, selection faite par le serveur
    	PF('widgetVarTabViewLeft').select(PF('widgetVarTabViewLeft').getLength()-1);
    }
    
//    function activeTabCenter(laClasse) {
//    	$('li.tab').each(function(index) { 
//    	   if($(this).hasClass(laClasse)) {
//    		   PF('widgetVarTabView').select(index);
//    		   //console.log( "test ***** "+$(this).attr('tabindex')+"   "+index);
//    	   }
//    	});
//    }
    
    function activeTabCenter(laClasse, tabView) {
    	if(tabView != null ){

    		var id = PF(tabView).id;
        	
        	$('[id="' + id + '"] > ul > li').each(function(index) { 
        	   if($(this).hasClass(laClasse)) {
        		   PF(tabView).select(index);
        		   //console.log( "test ***** "+$(this).attr('tabindex')+"   "+index);
        	   }
        	});
    		
    	}else {
    		$('> ul > li.tab').each(function(index) { 
 	     	   if($(this).hasClass(laClasse)) {
 	     		   PF('widgetVarTabView').select(index);
 	     		   //console.log( "test ***** "+$(this).attr('tabindex')+"   "+index);
 	     	   }
 	     	});
    	}
    	
    	
    	
    	
    }
    
    function activeLastTabCenter() {
    	PF('widgetVarTabView').select(PF('widgetVarTabView').getLength()-1);
    }
    
    function activeLastTabTracabilite() {
    	PF('wvTabViewTracabilite').select(PF('wvTabViewTracabilite').getLength()-1);
    }
    
    function activeNextTab(tabView) {
    	if(tabView.getActiveIndex()+1 < tabView.getLength()) {
    		tabView.select(tabView.getActiveIndex()+1);
    	}
    }
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
    
    function recupTabCourant(tabView) {
    	ongletActifTabViewLigne=tabView.getActiveIndex();
//    	 alert(ongletActifTabViewLigne);
    	 
    }
    
    function impressionDirect(checkBox,url) {
    	if(checkBox!=null && checkBox.input.is(':checked')){
    		window.open(url);
    	}else{
    		//alert('faux');
    	}
    }
    
    function clickBouttonSuivantCheckBox(checkBox,classeBoutton) {
    	if(checkBox!=null && checkBox.input.is(':checked')){
    		$(classeBoutton).click()
    	}else{
    		//alert('faux');
    	}
    }
    
    function selectTabCourant(tabView) {
    	if(ongletActifTabViewLigne!=null)  	
    		tabView.select(ongletActifTabViewLigne);
//    	 alert(ongletActifTabViewLigne);
    }
    
    
    function activePreviousTab(tabView) {
    	if(tabView.getActiveIndex()-1 >= 0) {
    		tabView.select(tabView.getActiveIndex()-1);
    	}
    }
    
    function handleCompleteToggleList(xhr, status, args) {  
       // alert("expandedForce:" + args.expandedForce);  
        if(args.expandedForce=="true") {
        	PF('widgetLayoutPage').toggle('west');
        }
    } 
    
    function afficheListeDocsManquantExport(xhr, status, args) {  
        // alert("expandedForce:" + args.expandedForce);  
    	return args.afficheListeDocManquant; 
     } 
    
    function reglementDifferentNetTTC(xhr, status, args) {
//    	alert(args.differenceReglementResteARegle);
   	 if(args.differenceReglementResteARegle )  
            return true;
   	 else
   		 return false;
   }
    

    function scrollToTop() {
    	//window.scrollTo(0,0);
    	//var elmnt = document.getElementById("top-bar"); /*nouveau theme primefaces mirage */
    	var elmnt = document.getElementById("layout-topbar-wrapper");
    	var elmnt = $(".layout-topbar-wrapper");
//    	elmnt.scrollLeft = 50;
    	elmnt.scrollTop = 0;
    }
    
    function handleCompleteValidationError(xhr, status, args) {  
    	 if(args.validationFailed )  
             return true;
    	 else
    		 return false;
    }
    
    function fermeRowExpension(idDataTable){
        var i = $('#'+idDataTable+' .ui-row-toggler.ui-icon-circle-triangle-s').length;
        if(i == 1){return;}
        $('#'+idDataTable+' .ui-row-toggler.ui-icon-circle-triangle-s').trigger('click');
    }

//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
    function doGetCaretPosition (oField) {
    	oField=document.getElementById(oField);
    	// Initialize
    	var iCaretPos = 0;

    	// IE Support
    	if (document.selection) {

    		// Set focus on the element
    		oField.focus ();

    		// To get cursor position, get empty selection range
    		var oSel = document.selection.createRange ();

    		// Move selection start to 0 position
    		oSel.moveStart ('character', -oField.value.length);

    		// The caret position is selection length
    		iCaretPos = oSel.text.length;
    	}

    	// Firefox support
//  	else if (oField.selectionStart || oField.selectionStart == '0')
    	iCaretPos = oField.selectionStart;
//  	alert(iCaretPos);
    	// Return results

    	command1([{name:'param1', value:iCaretPos}]);
    }
    
    function setCaretPositionIsa(elem, caretPos) {
    	elem=document.getElementById(elem);
    	if(elem) {
    		 elem.focus();

             if(caretPos > 0) {
                 if(elem.setSelectionRange) {
                	 elem.setSelectionRange(caretPos, caretPos);
                 } 
                 else if (elem.createTextRange) {
                   var range = elem.createTextRange();
                   range.collapse(true);
                   range.moveEnd('character', 1);
                   range.moveStart('character', 1);
                   range.select();
                 }
             }
         }
    }
    
    function setCaretPosition(elem, caretPos) {
        //var elem = document.getElementById(elemId);

        if(elem != null) {
            if(elem.createTextRange) {
                var range = elem.createTextRange();
                range.move('character', caretPos);
                range.select();
            }
            else {
                if(elem.selectionStart) {
                    elem.focus();
                    elem.setSelectionRange(caretPos, caretPos);
                }
                else
                    elem.focus();
            }
        }
    }
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
    function activeScan(jqueryElement) {
    
	    //$(document).scannerDetection({
    	$(jqueryElement).scannerDetection({
	    //$('.zone-scan-codebarre').scannerDetection({
	    	timeBeforeScanTest: 200, // wait for the next character for upto 200ms
	    	//startChar: [120], // Prefix character for the cabled scanner (OPL6845R)
	    	//endChar: [13], // be sure the scan is complete if key 13 (enter) is detected
	    	avgTimeByChar: 40, // it's not a barcode if a character takes longer than 40ms
	    	//ignoreIfFocusOn: $('input:not(.zone-scan-codebarre)'),
	    	//onComplete: function(barcode){ alert("aa : "+barcode); } // main callback function	
	    	onComplete: function(barcode){ 
	    		if($('.ui-datatable.myclass:visible').length > 0) {
	    			scanCodeBarreLigneBonLivraison();
	    		} //remote command $('.btn-cb-bonliv').click();}
	    		if($('.ui-datatable.myclass2:visible').length > 0) {
	    			scanCodeBarreLigneBonReception();
	    		} 
	    		if($('.ui-datatable.liste-ligne:visible').length > 0) {
	    			scanCodeBarreCaisseTactile();
	    		}
	    		//$('.btn-cb-bonreception').click();}
	    	} // main callback function	
	    	//onComplete: vcb()} // main callback function	
	    });
    
    }
    
    function desactiveScan(jqueryElement) {
    	$(jqueryElement).scannerDetection(false);
    }
    /*
    function autreLigneModifiable(maClasse) {
    	$('.'+maClasse+' .ui-cell-editor-input .btn-cb').removeClass('btn-cb-click');
    }
    
    function autreLigneModifiable(maClasse) {
    	$('.'+maClasse+' .ui-cell-editor-input .btn-cb').addClass('btn-cb-click');
    }
    */
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////  
    function autreLigneModifiable(maClasse) {
    	$('.'+maClasse+' .ui-row-editor a.ui-row-editor-pencil').each(function(){$(this).css('visibility','visible')});
    	$('.'+maClasse+' .icon-delete').each(function(){$(this).css('visibility','visible')});
    }
    
    function autreLigneNonModifiable(maClasse) {
    	$('.'+maClasse+' .ui-row-editor a.ui-row-editor-pencil').each(function(){$(this).css('visibility','hidden')});
    	$('.'+maClasse+' .icon-delete').each(function(){$(this).css('visibility','hidden')});
    	$('.'+maClasse+' .ui-cell-editor-input .focus-defaut-ligne').focus();
    }
    
    function editionDerniereLigne(maClasse) {
    	$('.'+maClasse+' .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});
    }
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////   
    function geocode() {
    	var adresse = document.getElementById('form:tabView:idTabViewTiers:idLigne1').value+' '+
			document.getElementById('form:tabView:idTabViewTiers:idLigne2').value+' '
			document.getElementById('form:tabView:idTabViewTiers:idLigne3').value+' '
			document.getElementById('form:tabView:idTabViewTiers:idCodePostal').value+' '
			document.getElementById('form:tabView:idTabViewTiers:idVille').value+' '
			document.getElementById('form:tabView:idTabViewTiers:idPays').value;
    	PF('pmap').geocode(adresse);
    }
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
    function ligneSeperationExpend() {
    	//alert('a');
//    	$('tr.ui-widget-content.ui-datatable-selectable').each(
//        		function(){
//        			if($(this).next('tr').next('tr').hasClass('zzz')) {
////        				alert('a')
//        				$(this).next('tr').next('tr').remove();
//        			}
//        		}
//        	);
    	
    	//nettoyage
    	$('.zzz').remove();
    			
    	//espace sous l'expand
    	$('tr:visible.ui-expanded-row-content.ui-widget-content').each(
    			// :visible car une fois créé, quand les expands sont replié ils ne sont pas supprimés, mais leur display=none
    		function(){
    			ligneSuivante = $(this).next('tr');
    			if(!ligneSuivante.hasClass('zzz')){
    				$('<tr class="zzz" style="border:none; color:white"><td>.</td></tr>').insertAfter( $(this) );
    			}
    		}
    	);
    	
    	//espace au dessus de la ligne dépliée si la ligne directement au dessus n'est pas dépliée
    	$('tr:visible.ui-widget-content.ui-datatable-selectable.ui-expanded-row').each(
    		function(){
    			lignePrecedente = $(this).prev('tr');
    			if(!lignePrecedente.hasClass('zzz') && !lignePrecedente.hasClass('ui-expanded-row')){
    				$('<tr class="zzz" style="border:none; color:white"><td>.</td></tr>').insertBefore( $(this) );
    			}
    		}
    	);
    }
    
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////    
    //enregistrer les erreurs javascript coté client pour pouvoir y acceder avec BDG pour pouvoir consulter les erreur JS des clients
    $( document ).ready(function() {
//	    window.onerror = function (msg, url, noLigne, noColonne, erreur) {
//	        var chaine = msg.toLowerCase();
//	        var souschaine = "script error";
//	        if (chaine.indexOf(souschaine) > -1){
//	            alert('Script Error : voir la Console du Navigateur pour les Détails');
//	        } else {
//	            var message = [
//	                'Message : ' + msg,
//	                'URL : ' + url,
//	                'Ligne : ' + noLigne,
//	                'Colonne : ' + noColonne,
//	                'Objet Error : ' + JSON.stringify(erreur)
//	            ].join(' - ');
//	
//	            alert(message);
//	        }
//	
//	        return false;
//	    };
    });
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////   
    
    /*
     * Primefaces Bug 5241 => La selection ne fonctionne pas avec les tables éditables
     * https://code.google.com/p/primefaces/issues/detail?id=5241
     * !!!!!!!!!! .ui-cell-editor-exclu-click-selection cette classe sert pour les écrans avec des cellEditor (pas RowEditor) sur lesquels on ne doit
     * pas appliquer la fonction "funcMakeCellEditorClickableAndOnContextMenuable" !!!!!!!!!!!!
     */
    //Debut Primefaces Bug 5241
    $( document ).ready(function() {
        console.log( "ready!" );
        var funcMakeCellEditorClickableAndOnContextMenuable = function() {
            $(this).closest(".ui-editable-column").not(".ui-cell-editor-exclu-click-selection").trigger("click");
            
        };
           
        
         
        $(document).on("click", ".ui-cell-editor", function(e) { $.proxy(funcMakeCellEditorClickableAndOnContextMenuable, this)(); } );
        $(document).on("contextmenu", ".ui-cell-editor", function(e) { $.proxy(funcMakeCellEditorClickableAndOnContextMenuable, this)(); } );
    });
    
    /*
     * La selection ne fonctionne pas avec les tables éditables
     * normalement il n'y a pas de sélection pendant l'édition, le changement de sélection peu se faire avec ENTER, NUMPAD_ENTER ou SPACE
     * pour pouvoir utiliser l'espace en édition (dans les champ) texte, il faut surcharger "bindSelectionKeyEvents" pour enlever "SPACE"
     * Sinon l'espace est intercepté pour déclancher une sélection au lieu d'un caractère espace.
     */
    PrimeFaces.widget.DataTable.prototype.bindSelectionKeyEvents = function() {
		var a = this;
		this
				.getFocusableTbody()
				.on(
						"focus",
						function(b) {
							if (!a.mousedownOnRow) {
								a.focusedRow = a.tbody
										.children(
												"tr.ui-widget-content.ui-datatable-selectable")
										.eq(0);
								a.highlightFocusedRow();
								if (a.cfg.scrollable) {
									PrimeFaces.scrollInView(
											a.scrollBody, a.focusedRow)
								}
							}
						})
				.on("blur", function() {
					if (a.focusedRow) {
						a.unhighlightFocusedRow();
						a.focusedRow = null
					}
				})
				.on(
						"keydown",
						function(f) {
							var d = $.ui.keyCode, b = f.which;
							if (a.focusedRow) {
								switch (b) {
								case d.UP:
									var g = a.focusedRow
											.prev("tr.ui-widget-content.ui-datatable-selectable");
									if (g.length) {
										a.unhighlightFocusedRow();
										a.focusedRow = g;
										a.highlightFocusedRow();
										if (a.cfg.scrollable) {
											PrimeFaces.scrollInView(
													a.scrollBody,
													a.focusedRow)
										}
									}
									f.preventDefault();
									break;
								case d.DOWN:
									var c = a.focusedRow
											.next("tr.ui-widget-content.ui-datatable-selectable");
									if (c.length) {
										a.unhighlightFocusedRow();
										a.focusedRow = c;
										a.highlightFocusedRow();
										if (a.cfg.scrollable) {
											PrimeFaces.scrollInView(
													a.scrollBody,
													a.focusedRow)
										}
									}
									f.preventDefault();
									break;
								case d.ENTER:
								case d.NUMPAD_ENTER:
								//case d.SPACE:
									f.target = a.focusedRow.children()
											.eq(0).get(0);
									a
											.onRowClick(f, a.focusedRow
													.get(0));
									f.preventDefault();
									break;
								default:
									break
								}
							}
						})
	};
    // Fin Primefaces Bug 5241
	
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
    
    //http://stackoverflow.com/questions/26472265/scroll-to-the-selected-tab-if-the-selected-tab-is-hidden-in-ptabview-on-intial
    PrimeFaces.widget.TabView.prototype.initScrolling = function() {
    	if (typeof this.lastTab.position() !== 'undefined') {
	    	if(this.jq.is(':visible')) {
	    		var overflown = (this.lastTab.position().left - this.firstTab.position().left) >                                               
	    		this.navscroller.innerWidth();
	    		if(overflown) {
	    			this.navscroller.css('padding-left', '18px');
	    			this.navcrollerLeft.show();
	    			this.navcrollerRight.show();
	
	    			activeTab = this.navContainer.children('.ui-tabs-selected');
	    			viewportWidth = this.navscroller.innerWidth();
	    			activeTabPosition = activeTab.position().left + parseInt(activeTab.innerWidth());
	
	    			if(activeTabPosition > viewportWidth) {
	    				var scrollStep = activeTabPosition - viewportWidth;
	    				scrollStep = Math.ceil(scrollStep/100) * -100;        
	    				this.scroll(scrollStep);
	    			} else {
	    				this.restoreScrollState();
	    			}                 
	    		}   
	    		return true;
	    	} else {
	    		return false;
	    	}
    	} else {
    		return true;
    	}
    	
    };
    
    
    /*
    //http://stackoverflow.com/questions/1009808/enter-key-press-behaves-like-a-tab-in-javascript
      
	// Map [Enter] key to work like the [Tab] key
	// Daniel P. Clark 2014
	
	// Catch the keydown for the entire document
    $(document).keydown(function(e) {

    	// Set self as the current item in focus
    	var self = $(':focus'),
    	// Set the form by the current item in focus
    	form = self.parents('form:eq(0)'),
    	focusable;

    	// Array of Indexable/Tab-able items
    	focusable = form.find('input,a,select,button,textarea,div[contenteditable=true]').filter(':visible');

    	function enterKey(){
    		if (e.which === 13 && !self.is('textarea,div[contenteditable=true]')) { // [Enter] key

    			// If not a regular hyperlink/button/textarea
    			if ($.inArray(self, focusable) && (!self.is('a,button'))){
    				// Then prevent the default [Enter] key behaviour from submitting the form
    				e.preventDefault();
    			} // Otherwise follow the link/button as by design, or put new line in textarea

    			// Focus on the next item (either previous or next depending on shift)
    			focusable.eq(focusable.index(self) + (e.shiftKey ? -1 : 1)).focus();

    			return false;
    		}
    	}
    	// We need to capture the [Shift] key and check the [Enter] key either way.
    	if (e.shiftKey) { enterKey() } else { enterKey() }
    });
    */

//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
    
    // Fonction qui permet de prendre en compte la classe css pour les menuitem que l'on veut mettre à droite dans les
    // zone de tableau de bord
    $(".menuitem-droit").parent().addClass("menuitem-droit");
	
//]]>