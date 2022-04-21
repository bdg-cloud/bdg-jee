//<![CDATA[ 
    function toggleListWest() {
    	//PF('widgetLayoutPage').toggle('west');
    	PF('widgetVarTabViewLeft').select(PF('widgetVarTabViewLeft').getLength()-1);
    }
    
    function activeLastTabCenter() {
    	PF('widgetVarTabView').select(PF('widgetVarTabView').getLength()-1);
    }
    
    function handleCompleteToggleList(xhr, status, args) {  
       // alert("expandedForce:" + args.expandedForce);  
        if(args.expandedForce=="true") {
        	PF('widgetLayoutPage').toggle('west');
        }
    }  
//]]>