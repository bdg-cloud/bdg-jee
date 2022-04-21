function formatbarre ()
	{
	//this.cfg.axes.yaxis.tickOptions.formatString = "%'d";
	this.cfg.highlighter={show: true,tooltipAxes: 'y',useAxesFormatters: false,tooltipFormatString: "%.2f"+' €', sizeAdjust: 18};
//	this.cfg.highlighter={show: true,tooltipAxes: 'y',useAxesFormatters: false,tooltipFormatString: "%'d €", sizeAdjust: 18};
	$.jqplot.sprintf.decimalMark = '.';
	$.jqplot.sprintf.thousandsSeparator = ' ';
//	this.cfg.highlighter={show: true,tooltipAxes: 'y',useAxesFormatters: false,tooltipFormatString: '<table><tr><td>hi:</td><td>%s</td></tr><tr><td>low:</td><td>%s</td></tr> <tr><td>close:</td><td>%s</td></tr></table>', sizeAdjust: 15};
//	this.cfg.highlighter= {
//        show: true,
//        sizeAdjust: 8,
//        tooltipLocation: 'n',
//        tooltipAxes: 'piered',
//       formatString:'%s',
//        fadeTooltip: true,
//        tooltipFadeSpeed: "fast",
//        useAxesFormatters: false
//
//    }
	//this.cfg.grid = {             
		  //  background: 'transparent',
		  //   gridLineColor: '#303030',
		  //   drawBorder: false,
	//	};
	}

function formatbarreHoriz ()
{
//this.cfg.axes.yaxis.tickOptions.formatString = "%'d";
this.cfg.highlighter={show: true,tooltipAxes: 'x',useAxesFormatters: false,tooltipFormatString: "%.2f"+' €', sizeAdjust: 18};
//this.cfg.highlighter={show: true,tooltipAxes: 'x',useAxesFormatters: false,tooltipFormatString: "%'d €", sizeAdjust: 18};
$.jqplot.sprintf.decimalMark = '.';
$.jqplot.sprintf.thousandsSeparator = ' ';
//this.cfg.highlighter={show: true,tooltipAxes: 'y',useAxesFormatters: false,tooltipFormatString: '<table><tr><td>hi:</td><td>%s</td></tr><tr><td>low:</td><td>%s</td></tr> <tr><td>close:</td><td>%s</td></tr></table>', sizeAdjust: 15};
//this.cfg.highlighter= {
//    show: true,
//    sizeAdjust: 8,
//    tooltipLocation: 'n',
//    tooltipAxes: 'piered',
//   formatString:'%s',
//    fadeTooltip: true,
//    tooltipFadeSpeed: "fast",
//    useAxesFormatters: false
//
//}
//this.cfg.grid = {             
	  //  background: 'transparent',
	  //   gridLineColor: '#303030',
	  //   drawBorder: false,
//	};
}

function formatline ()
{
	this.cfg.highlighter={show: true,tooltipAxes: 'y',useAxesFormatters: false,tooltipFormatString: "%.2f"+' €', sizeAdjust: 18};
	$.jqplot.sprintf.decimalMark = '.';
	$.jqplot.sprintf.thousandsSeparator = ' ';
}

