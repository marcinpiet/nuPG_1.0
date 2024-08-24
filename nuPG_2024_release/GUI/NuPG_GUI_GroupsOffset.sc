NuPG_GUI_GroupsOffset {

	var <>window;
	var <>stack;
	var <>modeButton;
	var <>tablesButton;
	var <>tables;
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
		//3 - > number of parameters of modulation unit
		modeButton = n.collect{};
		tablesButton = n.collect{};
		slider = n.collect{ 3.collect{} };
		numberDisplay = n.collect{ 3.collect{} };

		n.collect{|i|

			3.collect{|l|

				slider[i][l] = guiDefinitions.sliderView(width: 270, height: 20);
				slider[i][l].action_{|sl| };
				slider[i][l].mouseDownAction_{|sl| };
				slider[i][l].mouseUpAction_{|sl| };

				numberDisplay[i][l] = guiDefinitions.numberView(width: 25, height: 20);
				numberDisplay[i][l].action_{|num|};
			}

		};

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		//place objects on view
		n.collect{|i|
			//parameters' labels
			var names = [
				"_group_1_offset",
				"_group_2_offset",
				"_group_3_offset"
			];
			viewLayout[i].addSpanning(item: tablesButton[i], row: 0, column: 0);
			viewLayout[i].addSpanning(item: modeButton[i], row: 0, column: 1);
			3.collect{|l|
				//shift values to distribute objects on a view
				var shiftT = [1, 3, 5];
				var shiftS = [2, 4, 6];
				viewLayout[i].addSpanning(item: guiDefinitions.nuPGStaticText(names[l], 11, 150), row: shiftT[l], column: 0);
				viewLayout[i].addSpanning(item: slider[i][l], row: shiftS[l], column: 0, columnSpan: 4);
				viewLayout[i].addSpanning(item: numberDisplay[i][l], row: shiftS[l], column: 5);

			};
		};



		//load views into stacks
		n.collect{|i|
			stack.add(view[i])
		};

		//^window.front;
	}

	visible {|boolean|
		^window.visible = boolean
	}
}