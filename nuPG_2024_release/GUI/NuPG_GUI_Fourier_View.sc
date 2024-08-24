NuPG_GUI_Fourier_View {

	var <>window;
	var <>stack;
	var <>table;
	var <>data;
	var <>setBuffer;


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
		setBuffer = n.collect{};
		shapeFun = {|in| Signal.sineFill(2048, in).normalize};
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//define objects
		//generate empty placeholders for objects of size = n
		table = n.collect{};

		n.collect{|i|
			table[i] = guiDefinitions.tableView;
			table[i].reference_(0);
			table[i].drawRects_(true);
			table[i].drawLines_(false);
			table[i].editable_(true);
			table[i].isFilled_(true);
			table[i].indexThumbSize_(12);
			table[i].fillColor_(guiDefinitions.onYellow);
			table[i].background_(guiDefinitions.bAndKGreenLight);
			table[i].elasticMode_(1);
			table[i].size = 16;
			table[i].action_{|ms|
				var copy;
				copy = shapeFun.value(ms.value);
				data[i].value = copy.linlin(-1, 1, -1, 1);
			};
			table[i].mouseDownAction_{|ms| };
			table[i].mouseUpAction_{|ms| };

			view[i].keyDownAction_({arg view,char,modifiers,unicode,keycode;
			//copy
			if(keycode == 8,
				{dataCopyPaste.copyData(data[i].value);
					"copy data".postln},
				{});
			//paste
			if(keycode == 35,
				{   //update the CV
					//dataCopyPaste.pasteData.postln;
					data[i].value = dataCopyPaste.pasteData.linlin(0, 1, -1, 1);
					"paste data".postln},
				{})
		});

		};

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		//place objects on view
		//table view editors
		n.collect{|i| viewLayout[i].addSpanning(item: table[i], row: 0, column: 0) };



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