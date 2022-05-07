//NuPG
//changed the naming convention
//swapped SEQUENCE for a GRAPH
/*
the term graph bettwer reflects the nature of the tool and it points at atemporality of representation

graph - a diagram showing the relation between variable quantities, typically of two variables, each measured along one of a pair of axes at right angles.

*/

NuPG_GUI_Sequencer_Micro {

	var <>window;
	var <>stack;
	var <>sequencerPlay;
	var <>multisliders;
	var <>ranges;
	var <>scrubberSlider;
	var <>mode = 0;

	build {|colorScheme = 0, pattern, editors|

		var view, viewLayout, slotGrid, slots, actions;
		var defs = NuPG_GUI_definitions;
		var names = [
			[\trigFreqMicro_1, \grainFreqMicro_1, \envMultMicro_1, \panMicro_1,
				\ampMicro_1],
			[\trigFreqMicro_2, \grainFreqMicro_2, \envMultMicro_2, \panMicro_2,
				\ampMicro_2],
			[\trigFreqMicro_3, \grainFreqMicro_3, \envMultMicro_3, \panMicro_3,
				\ampMicro_3]
		];
		var default = [1,1,1,0,1];


		window = Window.new("GRAPHS",
			Rect.fromArray(defs.nuPGDimensions[3]), resizable: false);
		window.userCanClose = false;
		window.layout_(stack = StackLayout.new());


		//view for each stack
		view = 3.collect{defs.nuPGView(1)};
		//view layout
		viewLayout = 3.collect{GridLayout.new()};
    	3.collect{|i| view[i].layout_(viewLayout[i].margins_([3,3,3,3]).spacing_(1))};

		//3 slots for 3 stacks
		slots = 3.collect{ defs.nuPGView(colorScheme) };
		//grid - to organise objects - for each slot
		slotGrid = 3.collect{ GridLayout.new().margins_([3,3,3,3]) };
		//layout for each view
		//add content to each slot
		3.collect{|i| slots[i].layout_(slotGrid[i])};


		//data as arrays
		sequencerPlay = 5.collect{};
		multisliders = 3.collect{5.collect{}};
		ranges = 3.collect{5.collect{2.collect}};
		scrubberSlider = 3.collect{5.collect{}};

//load objects into views
		3.collect{|i|

			5.collect{|l|
				var shiftB = [3, 10, 17, 24, 31, 38, 45];
				var shiftM = [4, 11, 18, 25, 32, 39, 46];
				var items = [
					"_trigger_frequency",
					"_grain_frequency",
					"_envelope_multiplicator",
					"_pan",
					"_amp",
				];

				//labels
				slotGrid[i].addSpanning(defs.nuPGText(items[l], 20, 150), shiftB[l], 2, columnSpan: 3);

				//sequencer play and stop button
				sequencerPlay[l] = defs.nuPGButton(
					[["|>", Color.white, Color.grey],
					 ["|>", Color.black, Color.new255(250, 100, 90)]], 20, 20)
				.action_({|butt|
					var st = butt.value;
					//var st.postln;
					switch(mode,
						0, {
							switch(st,
						0, {currentEnvironment.put(names[i][l], default[l])},
						1, {currentEnvironment.put(names[i][l], pattern.sequencerPatt[i][l])}
					) },
						1, {
							switch(st,
						0, {currentEnvironment.put(names[i][l], default[l])},
						1, {currentEnvironment.put(names[i][l], pattern.sequencerPatt[i][l])}
					)})
				});
				slotGrid[i].addSpanning(sequencerPlay[l], shiftB[l], 0);

				//open and close sequencer editor
				slotGrid[i].addSpanning(defs.nuPGButton([["E"], ["E", Color.black, Color.new255(250, 100, 90)]], 20, 20)
					.action_({|butt|
						var st = butt.value;
						//var st.postln;
						switch(st,
							0, {editors[l].visible(0)},
							1, {editors[l].visible(1)}
				)}), shiftB[l], 1);

				//scrubSlider
				scrubberSlider[i][l] = defs.nuPGSlider()
				.thumbSize_(1)
				.knobColor_(Color.new255(250, 100, 90))
				.background_(Color.gray(1, 0))
				.visible_(0);
				slotGrid[i].addSpanning(scrubberSlider[i][l], shiftM[l], 0, rowSpan: 6, columnSpan: 6);

				//multisliders
				multisliders[i][l] = defs.nuPGMultislider(index: l).action_({})
				.background_(Color.gray(1, 0));
				slotGrid[i].addSpanning(multisliders[i][l], shiftM[l], 0, rowSpan: 6, columnSpan: 6);

				multisliders[i][l].action = {};


				//ranges
				slotGrid[i].addSpanning(defs.nuPGText("_min"), shiftM[l] + 4, 6);
				ranges[i][l][0] = defs.nuPGNumberBox(25, 30); //min
				slotGrid[i].addSpanning(ranges[i][l][0], shiftM[l] + 5, 6);

				slotGrid[i].addSpanning(defs.nuPGText("_max"), shiftM[l], 6);
				ranges[i][l][1] = defs.nuPGNumberBox(25, 30); //max
				slotGrid[i].addSpanning(ranges[i][l][1], shiftM[l] + 1, 6);


			};

		};

		3.collect{|i| viewLayout[i].addSpanning(slots[i], 0, 0)};



		3.collect{|i| stack.add(view[i])};

		//return a window
		^window.front

	}
}