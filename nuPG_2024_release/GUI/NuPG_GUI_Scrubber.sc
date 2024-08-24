NuPG_GUI_ScrubberView {
	var <>window;
	var <>trainSync;
	var <>trainProgress;
	var <>progresDisplay;
	var <>progresTask;
	var <>sliderRecordTask, <>sliderPlaybackTask;
	var <>path;
	var <>stack;


	draw {|dimensions, data, tasks, synthesis, n = 1|
		var view, viewLayout, sliderRecordPlay, funTextEditor;
		var groups;

		//get GUI defs
		var guiDefinitions = NuPG_GUI_Definitions;
		//var sliderRecordPlayer = NuPG_Slider_Recorder_Palyer;
		//sliderRecordPlayer.data = data.data_progressSlider;
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//window
		window = Window("_scanner", dimensions, resizable: false);
		window.userCanClose_(0);
		//window.alwaysOnTop_(true);
		window.view.background_(guiDefinitions.bAndKGreen);
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

		//////////////////////////////////////////////////////////////////////////////////////////

		trainProgress = n.collect{ };
		sliderRecordPlay = n.collect{ };
		trainSync = n.collect{ };
		progresTask = n.collect{ };
		progresDisplay = n.collect{ };
		funTextEditor = n.collect{ };
		////////////////////////////////////////////////////////////////

		n.collect{|i|
			trainSync[i] = guiDefinitions.nuPGButton([
				["S", Color.black, Color.white], ["S", Color.black, Color.new255(250, 100, 90)]], 18, 20);

			trainSync[i].action_{|butt|
				var iter = n.collect{|l| l }.asSet.remove(i).asArray;
				var st = butt.value; st.postln; iter.postln;
				switch(st,
					0, {},
					1, {}
				)
			};
			trainProgress[i] = guiDefinitions.nuPGSlider(20, 910);
			trainProgress[i].mouseUpAction_({
				if(sliderRecordPlay[i].value == 1,
					{sliderRecordPlay[i].valueAction = 2},
					{});
			});

			progresDisplay[i] = guiDefinitions.nuPGNumberBox(20, 30);
			//progresDisplay[i].value = data.data_scrubber[i].value * 2048;

			sliderRecordPlay[i] = guiDefinitions.nuPGButton([
				["R", Color.black, Color.white],
					 ["R", Color.black, Color.new255(250, 100, 90)],
					 ["P", Color.white, Color.new255(250, 100, 90)]], 20, 25);

			sliderRecordPlay[i].action_({|butt|
							var st = butt.value;
							switch(st,
						0, {sliderRecordTask[i].stop; sliderPlaybackTask[i].stop},
					    1, {sliderPlaybackTask[i].stop; sliderRecordTask[i].play},
						2, {sliderPlaybackTask[i].play; sliderRecordTask[i].stop}
					)
			});

			funTextEditor[i] =
			guiDefinitions.nuPGButton([["FUN", Color.black, Color.white]], 20, 25);
			funTextEditor[i].action_({/*
				File.open(path ++ "scanFunctions.scd")
			*/});

		};

		n.collect{|i|
			viewLayout[i].addSpanning(trainSync[i], row: 0, column: 0);
			viewLayout[i].addSpanning(trainProgress[i], row: 0, column: 1, columnSpan: 15);
			viewLayout[i].addSpanning(progresDisplay[i], row: 0, column: 16);
			viewLayout[i].addSpanning(sliderRecordPlay[i], row: 0, column: 17);
			//viewLayout[i].addSpanning(funTextEditor[i], row: 0, column: 18);
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