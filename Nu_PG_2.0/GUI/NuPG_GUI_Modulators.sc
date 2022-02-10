NuPG_GUI_Modulators {

	var <>window;
	var <>stack;
	var <>sliders;
	var <>numBoxes;
	var <>mod_ranges;
	var <>wf_ranges;
	var <>modulatorType;

	build {|data, iter, editor, colorScheme = 0|

		var view, viewLayout, slotGrid, slots, actions;
		var defs = NuPG_GUI_definitions;


		window = Window.new("MODULATOR_" ++ iter.asInteger,
			Rect.fromArray(defs.nuPGDimensions[43 + iter.asInteger]), resizable: false);
		window.userCanClose = false;
		window.layout_(stack = StackLayout.new());

		//view for each stack
		view = 3.collect{defs.nuPGView(1)};
		//view layout
		viewLayout = 3.collect{GridLayout.new()};

		3.collect{|i| view[i].layout_(viewLayout[i].margins_([1,1,1,1]).spacing_(1))};

		//3 slots for 3 stacks
		slots = 3.collect{ 4.collect { defs.nuPGView(colorScheme) } };
		//grid - to organise objects - for each slot
		slotGrid = 3.collect{ 4.collect { GridLayout.new().margins_(5).vSpacing_(5).hSpacing_(5) } };
		//layout for each view
		//add content to each slot
		3.collect{|i| 4.collect{|l| slots[i][l].layout_(slotGrid[i][l])} };

		sliders = 3.collect{2.collect{}};
		numBoxes = 3.collect{2.collect{}};
		mod_ranges = 3.collect{2.collect{}};
		wf_ranges = 3.collect{2.collect{}};
		modulatorType = 3.collect{1.collect{}};

		//add elements
		3.collect{|i|
			var nodes = Ndef(("mod_"++i++iter).asSymbol);
			//modulator select unit
			var text = defs.nuPGText("_source", 20, 100);
			//modulator type menu
			//modulator editor
			var editButton = defs.nuPGButton([
			[ "E"],
			[ "E", Color.black, Color.new255(250, 100, 90)]], 20, 20)
			.action_({|butt| var st = butt.value;
				switch(st,
					0, {editor.visible(0)},
					1, {editor.visible(1)};
		)});

			modulatorType[i][0] = defs.nuPGMenu(["sine", "saw", "latoc", "hennon", "gendyN"], 0);
			/*modulatorType[i][0].action_({|menu|
				nodes.set(\modSel, modulatorType[i][0].value);
			});*/


			//put them into GUI
			slotGrid[i][0].addSpanning(text, 0, 0);
			slotGrid[i][0].addSpanning(modulatorType[i][0], 0, 1);
			slotGrid[i][0].addSpanning(editButton, 0, 2);
		};

		//add elements
	    3.collect{|i|
			//modulator frequency control
			var text = [
				defs.nuPGText("_frequency", 20, 190),
				defs.nuPGText("_index", 20, 190)
			];
           //mod amount
			sliders[i][0] = defs.nuPGSlider(15, 120);
			numBoxes[i][0] = defs.nuPGNumberBox(20, 25);
			sliders[i][1] = defs.nuPGSlider(15, 120);
			numBoxes[i][1] = defs.nuPGNumberBox(20, 25);

			//put them into GUI
			slotGrid[i][2].addSpanning(text[0], 0, 0);
			slotGrid[i][2].addSpanning(sliders[i][0], 1, 0);
			slotGrid[i][2].addSpanning(numBoxes[i][0], 1, 4);
			slotGrid[i][2].addSpanning(text[1], 2, 0);
			slotGrid[i][2].addSpanning(sliders[i][1], 3, 0);
			slotGrid[i][2].addSpanning(numBoxes[i][1], 3, 4);
		};

		3.collect{|i|
			var text = defs.nuPGText("wf range", 20, 150);
			var textLow = defs.nuPGText("_WF_lo", 20, 40);
			var textHi = defs.nuPGText("_WF_hi", 20, 40);
			wf_ranges[i][0] = defs.nuPGNumberBox(20, 25);
			wf_ranges[i][1] = defs.nuPGNumberBox(20, 25);
			//put these on selected unit
		    //slotGrid[i][1].addSpanning(text, 0, 1);
            slotGrid[i][1].addSpanning(textLow, 0, 0);
			slotGrid[i][1].addSpanning(wf_ranges[i][0],0, 1);
			slotGrid[i][1].addSpanning(textHi, 0, 2);
			slotGrid[i][1].addSpanning(wf_ranges[i][1], 0, 3);

		};

      3.collect{|i| viewLayout[i].addSpanning(slots[i][0], 0, 0)};
	  3.collect{|i| viewLayout[i].addSpanning(slots[i][2], 1, 0, rowSpan: 2)};
	  3.collect{|i| viewLayout[i].addSpanning(slots[i][1], 3, 0, rowSpan: 2)};

	 3.collect{|i| stack.add(view[i])};

	//window.front;
	}

	visible {|boolean|
		^window.visible = boolean
	}
}