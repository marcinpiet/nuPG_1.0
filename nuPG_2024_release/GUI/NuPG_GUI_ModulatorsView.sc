NuPG_GUI_ModulatorsView {

	var <>window;
	var <>stack;
	var <>data;
	var <>modType, <>modFreq, <>modDepth;

	draw {|name, dimensions, synthesis, n = 1|

		var view, viewLayout;
		var paramLabel, paramNames, modTypeItems;
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
			.margins_([3, 3, 3, 3]);
		};
		//load gridLayouts into corresponding views
		n.collect{|i| view[i].layout_(viewLayout[i])};
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		data = n.collect{ 2.collect{} };
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		//place objects on view
		//table view editors
		paramNames = [
			"_modType",
			"_modFreq",
			"_modDepth"
		];

		modTypeItems = ["SinOsc", "LFSaw", "Lato", "Gendy", "HennonC"];

		modType = n.collect{};
		modFreq = n.collect{};
		modDepth = n.collect{};
		paramLabel = n.collect{ paramNames.size.collect{} };


		n.collect{|i|

			paramNames.size.collect{|k|
				paramLabel[i][k] = guiDefinitions.nuPGStaticText(paramNames[k], 20, 60)
			};

			modType[i] = guiDefinitions.nuPGMenu(modTypeItems, 0, 70);
			modFreq[i] = guiDefinitions.nuPGNumberBox(20, 30).action_{|num|};
			modDepth[i] = guiDefinitions.nuPGNumberBox(20, 30).action_{|num|};

		};


		n.collect{|i|
			paramNames.size.collect{|k|
				viewLayout[i].addSpanning(item: paramLabel[i][k], row: k, column: 0)
			};

			viewLayout[i].addSpanning(item: modType[i], row: 0, column: 1);
			viewLayout[i].addSpanning(item: modFreq[i], row: 1, column: 1);
			viewLayout[i].addSpanning(item: modDepth[i], row: 2, column: 1);
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