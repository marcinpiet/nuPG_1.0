/*
an interface for sequencer reading functions
- speed control (number box + slider),
- scrub - user controlled reading position
- graph - reading position according to user specified graph (x - time, y - data point)
*/

NuPG_GUI_Micro_ReadingControl {

	 var <>window;
	 var <>stack;
	 var <>slider;
     var <>numberBox;

	build {|colorScheme = 0, sequencer, scrubberGUI, controlGUI, graphEditorGUI, scrubberTask|

		var view, viewLayout, slotGrid, slots, actions;
		//initialise crub and graph views here
		var scrub;
		var graph;
		//get defs
		var defs = NuPG_GUI_definitions;
		var reshapeGraphData =  3.collect{|i|
			5.collect{|l| graphEditorGUI[l].scanningSlider[i] }
		};


		window = Window.new("MICRO READING CONTROL", Rect.fromArray(defs.nuPGDimensions[36]),
			resizable: false);
		window.userCanClose = false;
		window.layout_(stack = StackLayout.new().margins_(1));


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

		slider = 3.collect{};
		numberBox = 3.collect{};

		3.collect{|i|

			var scrubButton;
			var graphButton;

			slider[i] = defs.nuPGSlider(150, 15).orientation_(\vertical);
			numberBox[i] = defs.nuPGNumberBox(25, 30);

			slotGrid[i].addSpanning(slider[i], 0, 0);
			slotGrid[i].addSpanning(numberBox[i], 1, 0);

			scrubButton = defs.nuPGButton([["S"],["S", Color.black, Color.new255(250, 100, 90)]], 20, 40)
			.action_({|butt|
							var st = butt.value;
							switch(st,
					0, {
						5.collect{|l| sequencer.scrubberSlider[i][l].visible = 0};
						scrubberGUI.visible(0);
						5.collect{|l| reshapeGraphData[i][l].visible = 0 };
						scrubberTask[i].stop;
					},
					1, {
						5.collect{|l| sequencer.scrubberSlider[i][l].visible = 1};
						scrubberGUI.visible(1);
						controlGUI.sequencePlayButton[i].valueAction = 0;
						5.collect{|l| reshapeGraphData[i][l].visible = 1};
						scrubberTask[i].play;
				} )
			});
			slotGrid[i].addSpanning(scrubButton, 2, 0);
			//graphButton = defs.nuPGButton([["GRAPH"],["GRAPH", Color.black, Color.new255(250, 100, 90)]], 20, 40);
			//slotGrid[i].addSpanning(graphButton, 0, 3);


		};

		3.collect{|i| viewLayout[i].addSpanning(slots[i], 0, 0, columnSpan: 4)};



		3.collect{|i| stack.add(view[i])};


		^window.front

	}

	switchView {|index = 0|
		^stack.index = index
	}
}

