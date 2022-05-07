NuPG_GUI_ppModulation {

	var <>window;
	var <>stack;
	var <>numberBoxes;
	var <>sliders;
	var <>recButtons;

	build {|recordTasks, playbackTasks, colorScheme = 0|

		var view, viewLayout, slotGrid, slots, actions;
		var defs = NuPG_GUI_definitions;
		var labelNames = [
			"_FM AMOUNT",
			"_FM RATIO",
			"_FLUX",
		];


		window = Window.new("ppMODULATION",
			Rect.fromArray(defs.nuPGDimensions[49]), resizable: false);
		window.userCanClose = false;
		window.layout_(stack = StackLayout.new());

		//view for each stack
		view = 3.collect{defs.nuPGView(1)};
		//view layout
		viewLayout = 3.collect{GridLayout.new()};

		3.collect{|i| view[i].layout_(viewLayout[i].margins_([3,3,3,3]))};

		//1 slot (labels + sliders + numBoxes + slider rec buttons) for each view
		slots = 3.collect{defs.nuPGView(colorScheme) };
		//grid - to organise objects - for each slot
		slotGrid = 3.collect{GridLayout.new().margins_(3).vSpacing_(5).hSpacing_(5) };
		//layout for each view
		//add content to each slot
		3.collect{|i| slots[i].layout_(slotGrid[i])};

		sliders = 3.collect{3.collect{}};
		numberBoxes = 3.collect{3.collect{}};
		recButtons = 3.collect{3.collect{}};

		actions =
		3.collect{|i|
			3.collect{|l| [
				Tdef(("sliderRecord_"++i++l.asString).asSymbol),
				Tdef(("sliderPlay_"++i++l.asString).asSymbol)
				]

		}};

		3.collect{|i|
			3.collect{|l|
				var shiftT = [0, 2, 4, 6, 8];
				var shiftS = [1, 3, 5, 7, 9];

				slotGrid[i].addSpanning(defs.nuPGText(labelNames[l], 20, 200),
					shiftT[l], 0, columnSpan: 2);

				sliders[i][l] = defs.nuPGSlider(15, 180);
				sliders[i][l].mouseUpAction_({if( recButtons[i][l].value == 1, {recButtons[i][l].valueAction = 2},{});});
				slotGrid[i].addSpanning(sliders[i][l], shiftS[l], 0);

				recButtons[i][l] = defs.nuPGButton(
					[["R", Color.white, Color.grey],
					 ["R", Color.black, Color.new255(250, 100, 90)],
					 ["P", Color.white, Color.black]],25, 15);
				recButtons[i][l].action_({|butt|
							var st = butt.value;
							switch(st,
						0, {recordTasks[i][l].stop; playbackTasks[i][l].stop;},
					    1, {playbackTasks[i][l].stop; recordTasks[i][l].play},
						2, {playbackTasks[i][l].play; recordTasks[i][l].stop}
					)});
				slotGrid[i].addSpanning(recButtons[i][l], shiftS[l], 1);

				numberBoxes[i][l] = defs.nuPGNumberBox(25, 30);
				slotGrid[i].addSpanning(numberBoxes[i][l], shiftS[l], 3)
			};


		};

		3.collect{|i| viewLayout[i].addSpanning(slots[i], 0, 0, columnSpan: 4)};

		3.collect{|i| stack.add(view[i])};


		//^window.front
	}

	visible {|boolean|
		^window.visible = boolean
	}
}