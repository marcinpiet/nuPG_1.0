NuPG_GUI_Main {

	var <>window;
	var <>stack;
	var <>slider;
	var <>numberDisplay;
	var <>data;

	draw {|name, dimensions, n = 1|
		var view, viewLayout, labels;
		//get GUI defs
		var guiDefinitions = NuPG_GUI_Definitions;


		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		//window
		window = Window(name, dimensions, resizable: false);
		window.userCanClose = false;
		window.view.background_(guiDefinitions.bAndKGreen);
		window.userCanClose = false;

		//load stackLayaut to display multiple instances on top of each other
		window.layout_(stack = StackLayout.new() );
		//Unlike other layouts, StackLayout can not contain another layout, but only subclasses of View
		//solution - load a CompositeView and use GridLayout as its layout
		//n = number of instances set a build time, default n = 1, we need at least one instance
		//maximum of instances is 10
		view = n.collect{|i| guiDefinitions.nuPGView(guiDefinitions.colorArray[i])};
		//generate corresponding number of gridLayouts to load in to CompositeView
		//Grid Laayout
		viewLayout = n.collect{|i|
			GridLayout.new()
			.hSpacing_(3)
			.vSpacing_(3)
			.spacing_(1)
			.margins_([5, 5, 5, 5]);

		};
		//load gridLayouts into corresponding views
		n.collect{|i| view[i].layout_(viewLayout[i])};
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		data = n.collect{};
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//define objects
		//generate empty placeholders for objects of size = n
		//11 - > number of parameters of main unit
		slider = n.collect{ 13.collect{} };
		numberDisplay = n.collect{ 13.collect{} };

		n.collect{|i|



			13.collect{|l|

				slider[i][l] = guiDefinitions.sliderView(width: 250, height: 20);
				slider[i][l].action_{|sl| };
				slider[i][l].mouseDownAction_{|sl| };
				slider[i][l].mouseUpAction_{|sl| };

				numberDisplay[i][l] = guiDefinitions.numberView(width: 25, height: 20);
				numberDisplay[i][l].action_{|num|};
			}

		};

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		//place objects on view
		//table view editors
		n.collect{|i|
			//parameters' labels
			var names = [
			"_fundamental frequency",
			"_formant frequency one",
			"_formant frequency two",
			"_formant frequency three",
			"_envelope dilation one",
			"_envelope dilation two",
			"_envelope dilation three",
			"_pan one",
			"_pan two",
			"_pan three",
			"_amplitude one",
			"_amplitude two",
			"_amplitude three"
		];

			13.collect{|l|
				//shift values to distribute objects on a view
				var shiftT = [0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24];
				var shiftS = [1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25];
				viewLayout[i].add(item: guiDefinitions.nuPGStaticText(names[l], 11, 150), row: shiftT[l], column: 0);
				viewLayout[i].add(item: slider[i][l], row: shiftS[l], column: 0);
				viewLayout[i].add(item: numberDisplay[i][l], row: shiftS[l], column: 1);

			};
		};



		//load views into stacks
		n.collect{|i|
			stack.add(view[i])
		};

		^window.front;
	}
}