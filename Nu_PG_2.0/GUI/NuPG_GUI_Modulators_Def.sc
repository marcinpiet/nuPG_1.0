NuPG_GUI_Modulators_Definition_Editors {

	var <>window;
	var <>stack;
	var <>textField;
	var <>filesPath;

	build {|iter, colorScheme = 0|

		var view, viewLayout, slotGrid, slots, actions;
		var defs = NuPG_GUI_definitions;
		var inputText = (filesPath ++"/*.txt").pathMatch;
		inputText = inputText.clump(4);

		window = Window.new("DEFINITION EDITOR_" ++ iter.asInteger,
			Rect.fromArray(defs.nuPGDimensions[43 + iter.asInteger]), resizable: false);
		window.userCanClose = false;
		window.layout_(stack = StackLayout.new());

		//view for each stack
		view = 3.collect{defs.nuPGView(1)};
		//view layout
		viewLayout = 3.collect{GridLayout.new()};

		3.collect{|i| view[i].layout_(viewLayout[i].margins_([1,1,1,1]).spacing_(1))};

		//3 slots for 3 stacks
		slots = 3.collect{ 2.collect { defs.nuPGView(colorScheme) } };
		//grid - to organise objects - for each slot
		slotGrid = 3.collect{ 2.collect { GridLayout.new().margins_(3).vSpacing_(5).hSpacing_(5) } };
		//layout for each view
		//add content to each slot
		3.collect{|i| 2.collect{|l| slots[i][l].layout_(slotGrid[i][l])} };

		textField = 3.collect{};
		//add elements
		3.collect{|i|
			var evalButton;

			textField[i] = TextView.new();
			textField[i].open(inputText[i][iter]);
			evalButton = Button().states_([["EVAL"]]);
			//put them into GUI
			slotGrid[i][0].addSpanning(textField[i], 0, 0);
			slotGrid[i][1].addSpanning(evalButton, 0, 0);
		};

		3.collect{|i| viewLayout[i].addSpanning(slots[i][0], 0, 0)};
	    3.collect{|i| viewLayout[i].addSpanning(slots[i][1], 1, 0)};
	    //3.collect{|i| viewLayout[i].addSpanning(slots[i][1], 0, 8)};

	    3.collect{|i| stack.add(view[i])};

		//window.front;
	}

	visible {|boolean|
		^window.visible = boolean
	}
}