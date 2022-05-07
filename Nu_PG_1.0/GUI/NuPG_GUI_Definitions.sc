//nuPg GUI definitions
//GUI dimenisons, color schemes, font, GUI elements etc

NuPG_GUI_definitions {


	*resize {|resize = 1|

		^resize
	}

	*nuPgFont {|size = 10, italic = 1|
		var font;

		font = Font("Roboto Mono", size: size + this.resize, italic: italic);

		^font
	}

	*nuPGDimensions {
		var array;
		array = [
			//control
			[5, 730, 710, 170],
			///tables
			[5, 530, 560, 175],
			//main
			[315, 260, 400, 245],
			//modulators - MICRO
			[720, 95, 400, 805],
			//modulators - MESO
			[1125, 100, 400, 800],
			//masking
			[100, 280, 305, 100],
			//server
			[5, 140, 305, 90],
			//extensions
			[5, 345, 305, 160],
			//pulsaret shaper
			[45, 270, 205, 160],
			//pulsaret editor
			[255, 250, 900, 900],
			//envelope editor
			[555, 250, 900, 900],
			//micro seq editor 1
			[580, 475, 1200, 300],
			//micro seq editor 2
			[580, 375, 1200, 300],
			//micro seq editor 3
			[580, 275, 1200, 300],
			//micro seq editor 4
			[580, 175, 1200, 300],
			//micro seq editor 5
			[580, 75, 1200, 300],
			//meso seq editor 1
			[980, 575, 750, 250],
			//meso seq editor 2
			[980, 390, 750, 250],
			//meso seq editor 3
			[980, 205, 750, 250],
			//meso seq editor 4
			[980, 80, 750, 250],
			//meso seq editor 5
			[980, 10, 750, 250],
			//parameter linkage
			[45, 40, 410, 200],
			//rate modulator
			[45, 290, 220, 80],
			//perGrain transpose
			[145, 290, 480, 310],
			//wavelet
			[145, 290, 590, 110],
			//microSeq 1 Shaper
			[780, 575, 205, 160],
			//microSeq 2 Shaper
			[780, 390, 205, 160],
			//microSeq 3 Shaper
			[780, 205, 205, 160],
			//microSeq 4 Shaper
			[780, 80, 205, 160],
			//microSeq 5 Shaper
			[780, 10, 205, 160],
			//mesoSeq 1 Shaper
			[980, 575, 205, 160],
			//mesoSeq 2 Shaper
			[980, 390, 205, 160],
			//mesoSeq 3 Shaper
			[980, 205, 205, 160],
			//mesoSeq 4 Shaper
			[980, 80, 205, 160],
			//mesoSeq 5 Shaper
			[980, 10, 205, 160],
			//instrument defs
			[570, 605, 150, 100],
			//micro speed slider
			[1125, 500, 40, 400],
			//meso speed slider
			[1285, 880, 240, 40],
			//DTP
			[570, 180, 890, 475],
			//dtp - shaper 1
			[850, 700, 205, 160],
			//dtp - shaper 2
			[1060, 700, 205, 160],
			//frequency editor
			[870, 250, 900, 900],
			//modulator matrix
			[570, 405, 870, 870],
			//pulsaretFFT
			[45, 270, 605, 460],
			//mod 1
			[1045, 585, 290, 160],
			//mod 2
			[1045, 450, 290, 160],
			//mod 3
			[1045, 315, 290, 160],
			//mod 4
			[1045, 180, 290, 160],
			//sieveMask editor
			[410, 280, 200, 350],
			//ppMod Sliders
			[315, 90, 400, 145],
			//ppMod Select
			[],
			//SCRUBBER
			[720, 935, 400, 60],

		];

		^array
	}

	*nuPGView  {|colorScheme|
		var view =  CompositeView.new()
		.background_(this.nuPGBackground(colorScheme));
		/*.keyDownAction_({arg view,char,modifiers,unicode,keycode;
			[char,modifiers,unicode,keycode].postln});*/
		^view
	}

	*nuPGBackground {|choose = 1|

		var x = choose;

		^switch(x,
			0, {Color.gray(0.95)},
			1, {Color.new255(205, 250, 205)}, //original PG based on B&K hardware.
			2, {Color.gray(0.65)}
		)
	}

	*nuPGButton {|state, height, width|
		var button;

		button = Button()
		.font_(this.nuPgFont(size: 9))
		.states_(state)
		.minHeight_(height)
		.maxWidth_(width);

		^button
	}

	*nuPGKnob {|height, width|
		var knob;

		knob = Knob()
		.minHeight_(height)
		.maxWidth_(width);

		^knob
	}

	*nuPGText {|string, height = 20, width = 50|
		var text;

		text = StaticText()
		.string_(string)
		.font_(this.nuPgFont())
		.maxHeight_(height)
		.maxWidth_(width)
		.background_(Color.white);

		^text
	}

	*nuPGTextField {|string, height = 20, width = 50, font|
		var text;

		text = TextField()
		.string_(string)
		.maxHeight_(height)
		.maxWidth_(width)
		.font_(font);

		^text
	}

	*nuPGFont {|size = 10, italic = 1|
		var font;

		font = Font("Roboto Mono", size: size + this.resize, italic: italic);

		^font
	}


	*nuPGMenu {|items, defState|
		var menu;

		menu = PopUpMenu()
		.items_(items)
		.font_(this.nuPgFont())
		.valueAction_(defState);

		^menu
	}

	*nuPGSlider {|height, width, backgroundColor|
		var slider;

		slider = Slider()
		.background_(backgroundColor)
		.knobColor_(Color.gray(0.9))
		.orientation_(\horizontal)
		.minHeight_(height)
		.minWidth_(width);

		^slider
	}

	*nuPGMultislider {|choose = 1, index = 0|
		var multislider;
		var x = choose;

		multislider = MultiSliderView()
		.startIndex_(false)
		.valueThumbSize_(1)
		.drawLines_(true)
		.drawRects_(false)
		.editable_(true)
		.background_(
			switch(x,
				0, {Color.white},
				//1, {Color.new255(50,(index * 10 + 150), 200, 1)},
				1, {Color.gray(1, 0)},
				//2, {Color.new255(250, 100, 90, 100)}
				2, {Color.gray(1, 0)}
			)
		)
		.strokeColor_(Color.black)
		.elasticMode_(1)
		.setProperty(\showIndex, true);

		^multislider
	}


	*nuPGEnvView {|envData|
		var envView;

		envView = EnvelopeView()
		.grid_(Point(0.125, 0.5))
		.gridOn_(true)
		.drawLines_(true)
		.gridColor_(Color.red)
		.resize_(10)
		.step_(0.05)
		.thumbSize_(10)
		.keepHorizontalOrder_(true)
		.value_(envData);

		^envView
	}

	*nuPGNumberBox {|height, width|
		var numberBox;
		numberBox = NumberBox()
		.maxWidth_(width)
		.maxHeight_(height)
		.font_(this.nuPgFont(size: 9));

		^numberBox
	}

	*nuPGMeter {|height, width|
		var meter;

		meter = LevelIndicator()
		.maxHeight_(height)
		.maxWidth_(width)
		.numTicks_(5)
		.numMajorTicks_(3);

		^meter
	}
}