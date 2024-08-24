NuPG_GUI_Definitions {

	//colors scheme
	//ported from original PG
	*bAndKGreen { ^Color.new255(205, 250, 205) } 	// default gui colors; similar to old B&K hardware.
	*guiGrey { ^Color.new255(176, 176, 176) }
	*onYellow { ^Color.new255(250,250,144) }
	*lightYellow { ^Color.new255(139, 139, 122) }
	*bAndKGreenLight { ^Color.new255(230,255,230) } 		// Cloud Generator appriximation
	*darkGreen { ^Color.new255(43, 88, 43) }
	*white { ^Color.new255(255,255,255) }
	*recRed { ^Color.new255( 159, 17, 21 ) }
	*black { ^Color.new255(0,0,0) }
	//nuPG
	*magenta { ^Color.magenta() }
	*cyan { ^Color.new255(0, 139, 139) }
	*blueViollet { ^Color.new255(138, 43, 226) }
	*cornflowerBlue { ^Color.new255(100, 149, 237) }
	*orange { ^Color.new255(255, 165, 0)}
	*pink { ^Color.new255(255, 192, 203) }
	*snow { ^Color.new255(255, 250, 250)}

	//colors as array for adjustable number of instances
	//maximum 10
	*colorArray {
		var array =
		[this.bAndKGreen,
			this.guiGrey,
			this.lightYellow,
			this.magenta,
			this.blueViollet,
			this.cyan,
			this.orange,
			this.cornflowerBlue,
			this.pink,
			this.snow
		];
		^array;
	}


	*nuPGFont {|size = 10, italic = 0|
		var font;

		font = Font("Roboto Mono", size: size, italic: italic);

		^font
	}

	*nuPGView  {|color|
		var view =  CompositeView.new()
		.background_(color);
		^view
	}

	//pulsaret window dimensions
	*pulsaretViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 15,  //position from left of the screen
			top: Window.screenBounds.top + 300,  //position from bottom of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}
	//envelope window dimensions
	*envelopeViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 15,  //position from left of the screen
			top: Window.screenBounds.top + 15,  //position from bottom of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//envelope window dimensions
	*frequencyViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 5,  //position from left of the screen
			top: Window.screenBounds.top + 5,  //position from bottom of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//main window dimensionss
	*mainViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 320,  //position from left of the screen
			top: Window.screenBounds.top + 530,  //position from top of the screen
			width: 300,  //widt of the view
			height: 430 //height of the view
		);

		^dimensions;
	}

    //modulators window dimensionss
	*modulatorsViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1320,  //position from left of the screen
			top: Window.screenBounds.top + 130,  //position from top of the screen
			width: 320,  //widt of the view
			height: 130 //height of the view
		);

		^dimensions;
	}

	//groups offset window dimensionss
	*groupsOffsetViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1320,  //position from left of the screen
			top: Window.screenBounds.top + 290,  //position from top of the screen
			width: 320,  //widt of the view
			height: 130 //height of the view
		);

		^dimensions;
	}

	//masking window dimensionss
	*maskingViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 15,  //position from left of the screen
			top: Window.screenBounds.top + 530,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//fundamental frequency window dimensions
	*fundamentalViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 15,  //position from left of the screen
			top: Window.screenBounds.top + 760,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//formant frequency one window dimensions
	*formantOneViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 625,  //position from left of the screen
			top: Window.screenBounds.top + 760,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//formant frequency two window dimensions
	*formantTwoViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 930,  //position from left of the screen
			top: Window.screenBounds.top + 760,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//formant frequency three window dimensions
	*formantThreeViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1235,  //position from left of the screen
			top: Window.screenBounds.top + 760,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//envelope one window dimensions
	*envelopeOneViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 625,  //position from left of the screen
			top: Window.screenBounds.top + 530,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//envelope two window dimensions
	*envelopeTwoViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 930,  //position from left of the screen
			top: Window.screenBounds.top + 530,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//envelope three window dimensions
	*envelopeThreeViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1235,  //position from left of the screen
			top: Window.screenBounds.top + 530,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//pan one window dimensions
	*panOneViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 625,  //position from left of the screen
			top: Window.screenBounds.top + 300,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//pan two window dimensions
	*panTwoViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 930,  //position from left of the screen
			top: Window.screenBounds.top + 300,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//pan three window dimensions
	*panThreeViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1235,  //position from left of the screen
			top: Window.screenBounds.top + 300,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//amp one window dimensions
	*ampOneViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 625,  //position from left of the screen
			top: Window.screenBounds.top + 15,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//amp two window dimensions
	*ampTwoViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 930,  //position from left of the screen
			top: Window.screenBounds.top + 15,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//amp three window dimensions
	*ampThreeViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1235,  //position from left of the screen
			top: Window.screenBounds.top + 15,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//modulatiomAmount window dimensions
	*modulationAmountViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1335,  //position from left of the screen
			top: Window.screenBounds.top + 300,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//modulatiom ratio window dimensions
	*modulationRatioViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1335,  //position from left of the screen
			top: Window.screenBounds.top + 530,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//multiparameter modulatiom window dimensions
	*multiParameterModulationViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1335,  //position from left of the screen
			top: Window.screenBounds.top + 760,  //position from top of the screen
			width: 300,  //widt of the view
			height: 200 //height of the view
		);

		^dimensions;
	}

	//scrubber window dimensions
	*scrubberViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 15,  //position from left of the screen
			top: Window.screenBounds.top + 150,  //position from top of the screen
			width: 1000,  //widt of the view
			height: 30 //height of the view
		);

		^dimensions;
	}

	//control window dimensions
	*controlViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 320,  //position from left of the screen
			top: Window.screenBounds.top + 470,  //position from top of the screen
			width: 300,  //widt of the view
			height: 30 //height of the view
		);

		^dimensions;
	}

	//presets window dimensions
	*presetsViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 320,  //position from left of the screen
			top: Window.screenBounds.top + 300,  //position from top of the screen
			width: 300,  //widt of the view
			height: 140 //height of the view
		);

		^dimensions;
	}

	//extensions window dimensions
	*extensionsViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 320,  //position from left of the screen
			top: Window.screenBounds.top + 130,  //position from top of the screen
			width: 300,  //widt of the view
			height: 135 //height of the view
		);

		^dimensions;
	}

	//groups control window dimensions
	*groupsControlViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 625,  //position from left of the screen
			top: Window.screenBounds.top + 990,  //position from top of the screen
			width: 910,  //widt of the view
			height: 30 //height of the view
		);

		^dimensions;
	}

	//train control window dimensions
	*trainControlViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 15,  //position from left of the screen
			top: Window.screenBounds.top + 990,  //position from top of the screen
			width: 605,  //widt of the view
			height: 30 //height of the view
		);

		^dimensions;
	}

	//record window dimensions
	*recordViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 320,  //position from left of the screen
			top: Window.screenBounds.top + 15,  //position from top of the screen
			width: 300,  //widt of the view
			height: 30 //height of the view
		);

		^dimensions;
	}

	//record window dimensions
	*maskingControlDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1320,  //position from left of the screen
			top: Window.screenBounds.top + 450,  //position from top of the screen
			width: 200,  //widt of the view
			height: 100 //height of the view
		);

		^dimensions;
	}

	//large editor window dimensions
	*tableEditorViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 320,  //position from left of the screen
			top: Window.screenBounds.top + 280,  //position from top of the screen
			width: 910,  //widt of the view
			height: 650 //height of the view
		);

		^dimensions;
	}

	*fourierViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1320,  //position from left of the screen
			top: Window.screenBounds.top + 580,  //position from top of the screen
			width: 200,  //widt of the view
			height: 150 //height of the view
		);

		^dimensions;
	}

	//sieve window dimensionss
	*sieveViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1235,  //position from left of the screen
			top: Window.screenBounds.top + 280,  //position from top of the screen
			width: 300,  //widt of the view
			height: 325 //height of the view
		);

		^dimensions;
	}

	//matrix mod
	*modMatrixViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1235,  //position from left of the screen
			top: Window.screenBounds.top + 580,  //position from top of the screen
			width: 190,  //widt of the view
			height: 340 //height of the view
		);

		^dimensions;
	}

	//matrix mod
	*modulatorOneViewDimensions {

		var dimensions = Rect(
			left: Window.screenBounds.left + 1440,  //position from left of the screen
			top: Window.screenBounds.top + 860,  //position from top of the screen
			width: 140,  //widt of the view
			height: 75 //height of the view
		);

		^dimensions;
	}

	//table view definition
	*tableView {

		var table;

		table = MultiSliderView()
		.startIndex_(false)
		.valueThumbSize_(1)
		.drawLines_(true)
		.drawRects_(false)
		.editable_(true)
		.background_(this.white.alpha_(1.0))
		.strokeColor_(this.black)
		.elasticMode_(1)
		.setProperty(\showIndex, true);

		^table;
	}

	//slider view definition
	*sliderView {|width, height|

		var slider;

		slider = Slider()
		.background_(this.white.alpha_(1.0))
		.knobColor_(Color.gray(0.9))
		.orientation_(\horizontal)
		.fixedWidth_(width)
		.fixedHeight_(height);

		^slider
	}

	//slider view definition
	*numberView {|width, height|

		var numberBox;

		numberBox = NumberBox()
		.fixedWidth_(width)
		.fixedHeight_(height)
		.scroll_step_(0.01)
		.font_(this.nuPGFont(size: 9));

		^numberBox
	}



	*nuPGButton {|state, height, width|
		var button;

		button = Button()
		.font_(this.nuPGFont(size: 9))
		.states_(state)
		.fixedWidth_(width)
		.fixedHeight_(height);

		^button
	}

	*nuPGSlider {|height, width|
		var slider;

		slider = Slider()
		.background_(this.white)
		.knobColor_(this.guiGrey)
		.orientation_(\horizontal)
		.fixedWidth_(width)
		.fixedHeight_(height);

		^slider
	}

	*nuPGNumberBox {|height, width|
		var numberBox;
		numberBox = NumberBox()
		.fixedWidth_(width)
		.fixedHeight_(height)
		.font_(this.nuPGFont(size: 9));

		^numberBox
	}

	*nuPGStaticText {|string, height, width|
		var text;

		text = StaticText()
		.string_(string)
		.font_(this.nuPGFont())
		.fixedWidth_(width)
		.fixedHeight_(height)
		.background_(this.white);

		^text
	}

	*nuPGTextField {|string, height = 20, width = 50|
		var text;

		text = TextField()
		.string_(string)
		.fixedWidth_(width)
		.fixedHeight_(height)
		.font_(this.nuPGFont());

		^text
	}

	*nuPGMenu {|items, defState, width = 160|
		var menu;

		menu = PopUpMenu()
		.items_(items)
		.font_(this.nuPGFont())
		.fixedWidth_(width)
		.fixedHeight_(20)
		.valueAction_(defState);


		^menu
	}


}