/*
an interface for sequencer reading functions
- speed control (number box + slider),
- scrub - user controlled reading position
- graph - reading position according to user specified graph (x - time, y - data point)
*/

NuPG_GUI_Scrubber {

	 var <>window;
	 var <>stack;
	 var <>slider;
	 var <>recButtons;
	 var <>selectButtons;

	build {|recordTasks, playbackTasks, sequencer, scrubberTask, colorScheme = 0, data|

		var view, viewLayout, slotGrid, slots, actions;
		//initialise crub and graph views here
		//get defs
		var defs = NuPG_GUI_definitions;
		var params = [
			\micro_trigFreqMult,
			\micro_grainMult,
			\micro_envRateMult,
            \micro_panMult,
			\micro_ampMult
		];


		window = Window.new("SCANNER", Rect.fromArray(defs.nuPGDimensions[51]),
			resizable: false);
		window.userCanClose = false;
		window.layout_(stack = StackLayout.new().margins_(1));


		//view for each stack
		view = 3.collect{defs.nuPGView(colorScheme)};
		//view layout
		viewLayout = 3.collect{GridLayout.new()};
		3.collect{|i| view[i].layout_(viewLayout[i].margins_([3,3,3,3]))};

		//1 slot (labels + sliders + numBoxes + slider rec buttons) for each view
		slots = 3.collect{defs.nuPGView(colorScheme) };
		//grid - to organise objects - for each slot
		slotGrid = 3.collect{GridLayout.new().margins_(1).vSpacing_(1).hSpacing_(1) };
		//layout for each view
		//add content to each slot
		3.collect{|i| slots[i].layout_(slotGrid[i])};

		slider = 3.collect{};
		recButtons = 3.collect{};
		selectButtons = 3.collect{5.collect{}};

		3.collect{|i|

			slider[i] = defs.nuPGSlider(15, 330)
			.thumbSize_(10)
			.knobColor_(Color.new255(250, 100, 90))
			.action_({});
			slider[i].mouseUpAction_({if( recButtons[i].value == 1, {recButtons[i].valueAction = 2},{});});

			slotGrid[i].addSpanning(slider[i], 0, 0, columnSpan: 5);

			recButtons[i] = defs.nuPGButton(
					[["R", Color.white, Color.grey],
					 ["R", Color.black, Color.new255(250, 100, 90)],
					 ["P", Color.white, Color.black]], 25, 25);
				recButtons[i].action_({|butt|
							var st = butt.value;
							switch(st,
						0, {recordTasks[i].stop; playbackTasks[i].stop;},
					    1, {playbackTasks[i].stop; recordTasks[i].play},
						2, {playbackTasks[i].play; recordTasks[i].stop}
					)});
				slotGrid[i].addSpanning(recButtons[i], 0, 5);

			5.collect{|l|
				var labels = ["trigFreq", "grainFreq", "envMul", "pan", "amp"];
				var taskParams = [\param1, \param2, \param3, \param4, \param5];

				selectButtons[i][l] =  defs.nuPGButton(
					[[labels[l], Color.white, Color.grey],
					[labels[l], Color.black, Color.new255(250, 100, 90)]], 10, 65)
				.action_({|butt|
							var st = butt.value;
							switch(st,
						0, {scrubberTask[i].set(taskParams[l], 0)},
						1, {scrubberTask[i].set(taskParams[l], 1)}
					)});

			slotGrid[i].addSpanning(selectButtons[i][l], 1, l);
			}
		};


		3.collect{|i| viewLayout[i].addSpanning(slots[i], 0, 0, columnSpan: 4)};



		3.collect{|i| stack.add(view[i])};


		//^window.front

	}

	visible {|boolean|
		^window.visible = boolean
	}
}

