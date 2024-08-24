NuPG_GUI_Masking {

	var <>window;
	var <>stack;
	var <>probability, <>burtsRest, <>channel;
	var <>data;


	draw {|name, dimensions, buffer = 0, n = 1|
		var view, viewLayout, shapeFun;
		//get GUI defs
		var guiDefinitions = NuPG_GUI_Definitions;
		var dataCopyPaste = NuPG_Data_CopyPaste.new;

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
		probability = n.collect{};
		burtsRest = n.collect{ 2.collect{} };
		channel = n.collect{ 2.collect{} };

		n.collect{|i|
			//probability mask
			probability[i] = guiDefinitions.nuPGNumberBox(20, 30);
			probability[i].action_{};

			//burst masking
			2.collect{|l|
				burtsRest[i][l] = guiDefinitions.nuPGNumberBox(20, 30);
				burtsRest[i][l].action_{};
			};

			//channel
			2.collect{|l|
				channel[i][l] = guiDefinitions.nuPGNumberBox(20, 30);
				channel[i][l].action_{};
			};

		};

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		//place objects on view
		//table view editors
		n.collect{|i|
			viewLayout[i].addSpanning(item: guiDefinitions.nuPGStaticText("_probability", 20, 60), row: 0, column: 0);
			viewLayout[i].addSpanning(item: probability[i], row: 0, column: 1);

			viewLayout[i].addSpanning(item: guiDefinitions.nuPGStaticText("_burst", 20, 60), row: 1, column: 0);
			viewLayout[i].addSpanning(item: burtsRest[i][0], row: 1, column: 1);

			viewLayout[i].addSpanning(item: guiDefinitions.nuPGStaticText("_rest", 20, 60), row: 1, column: 2);
			viewLayout[i].addSpanning(item: burtsRest[i][1], row: 1, column: 3);

			viewLayout[i].addSpanning(item: guiDefinitions.nuPGStaticText("_channel", 20, 60), row: 2, column: 0);
			viewLayout[i].addSpanning(item: channel[i][0], row: 2, column: 1);
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