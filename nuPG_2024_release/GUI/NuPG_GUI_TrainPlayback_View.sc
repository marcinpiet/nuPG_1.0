NuPG_GUI_TrainControl_View {

	var <>window;
	var <>trainPlayStop, <>trainLoopPlayStop, <>trainPlayOnce;
	var <>trainSync;
	var <>scrubbEditor, <>scrubbTask;
	var <>trainDuration, <>trainProgress;
	var <>progresTask;
	var <>trainDirection;
	var <>stack;

	draw {|dimensions, tasks, taskSingle, scrubGUI, synthesis, n = 1|
		var view, viewLayout;
		var groups;

		//get GUI defs
		var guiDefinitions = NuPG_GUI_Definitions;
		//var sliderRecordPlayer = NuPG_Slider_Recorder_Palyer;
		//sliderRecordPlayer.data = data.data_progressSlider;
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//window
		window = Window("_train control", dimensions, resizable: false);
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

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		trainPlayStop = n.collect{ };
		trainLoopPlayStop = n.collect{ };
		trainPlayOnce = n.collect{ };
		trainDuration = n.collect{ };
		trainProgress = n.collect{ };
		trainDirection = n.collect{ };
		scrubbEditor = n.collect{ };
		trainSync = n.collect{ };
		progresTask = n.collect{ };
		scrubbTask = n.collect{ };
		//local, instance specific objects
		n.collect{|i|
			trainPlayStop[i] = guiDefinitions.nuPGButton([
				["[n]", Color.black, Color.white], ["|>", Color.black, Color.new255(250, 100, 90)]], 18, 30);
			trainPlayStop[i].action_{|butt|
				var st = butt.value; st.postln;
				switch(st,
					0, {synthesis.trainInstances[i].stop;},
					1, {synthesis.trainInstances[i].play;}
				)
			};

			trainLoopPlayStop[i] = guiDefinitions.nuPGButton([
				["loop", Color.black, Color.white], ["loop", Color.black, Color.new255(250, 100, 90)]], 18, 40);
			trainLoopPlayStop[i].action_{|butt|
				var st = butt.value; st.postln;
				switch(st,
					0, {
						tasks.tasks[i].stop; progresTask[i].stop;
						synthesis.trainInstances[i].set(
							\fundamental_frequency_loop, 1,
							\formant_frequency_One_loop, 1,
							\formant_frequency_Twoloop, 1,
							\formant_frequency_Three_loop, 1,
							\envMul_One_loop, 1,
							\envMul_Two_loop, 1,
							\envMul_Three_loop, 1,
							\pan_One_loop, 0,
							\pan_Two_loop, 0,
							\pan_Three_loop, 0,
							\amplitude_One_loop, 1,
							\amplitude_Two_loop, 1,
							\amplitude_Three_loop, 1,
							\probability_loop, 1,
						)
					},
					1, {tasks.tasks[i].play; progresTask[i].play}
				)
			};

			trainPlayOnce[i] = guiDefinitions.nuPGButton([
				["oneShot", Color.black, Color.white]], 18, 40);

			trainPlayOnce[i].action_{|butt|
				taskSingle.taskSingleShot[i].play;
			};


			trainSync[i] = guiDefinitions.nuPGButton([
				["S", Color.black, Color.white], ["S", Color.black, Color.new255(250, 100, 90)]], 18, 20);

			trainSync[i].action_{|butt|
				var iter = n.collect{|l| l }.asSet.remove(i).asArray;
				var st = butt.value; st.postln; iter.postln;
				switch(st,
					0, {
						n.collect{|l| trainLoopPlayStop[l].valueAction_(0)};
						iter.size.collect{|l| trainSync[iter[l]].value_(0)}
					},
					1, {
						n.collect{|l| trainLoopPlayStop[l].valueAction_(1)};
						iter.size.collect{|l| trainSync[iter[l]].value_(1)}
					}
				)
			};

			trainDuration[i] = guiDefinitions.nuPGNumberBox(20, 30);
			trainDuration[i].action_{};

			trainProgress[i] = guiDefinitions.nuPGSlider(20, 330);
			trainProgress[i].action_{|num| };

			trainDirection[i] = guiDefinitions.nuPGButton([
				[">", Color.black, Color.white], ["<", Color.black, Color.white]], 18, 30);
			trainDirection[i].action_{|butt|
				var st = butt.value; st.postln;
				switch(st,
					0, { trainLoopPlayStop[i].valueAction_(0);
						progresTask[i].stop;
						tasks.tasks[i].set(\playbackDirection, 0);
						progresTask[i].set(\progressDirection, 0);
						progresTask[i].play;
						trainLoopPlayStop[i].valueAction_(1);
					},
					1, {
						trainLoopPlayStop[i].valueAction_(0);
						progresTask[i].stop;
						tasks.tasks[i].set(\playbackDirection, 1);
						progresTask[i].set(\progressDirection, 1);
						progresTask[i].play;
						trainLoopPlayStop[i].valueAction_(1)}
				)
			};

			scrubbEditor[i] = guiDefinitions.nuPGButton([
				["SC", Color.black, Color.white], ["SC", Color.black, Color.new255(250, 100, 90)]], 18, 20);

			scrubbEditor[i].action_{|butt|
				//var iter = n.collect{|l| l }.asSet.remove(i).asArray;
				var st = butt.value; st.postln;
				switch(st,
					0, {scrubGUI.visible(0); scrubbTask[i].stop;},
					1, {
						scrubGUI.visible(1);
						scrubbTask[i].play;
						trainLoopPlayStop[i].valueAction_(0);
						trainProgress[i].valueAction_(0);
						progresTask[i].stop;
					}
				)
			};

		};

		//load int views
		n.collect{|i|
			viewLayout[i].addSpanning(trainPlayStop[i], row: 0, column: 0);
			viewLayout[i].addSpanning(trainLoopPlayStop[i], row: 0, column: 1);
			viewLayout[i].addSpanning(trainPlayOnce[i], row: 0, column: 2);
			viewLayout[i].addSpanning(trainSync[i], row: 0, column: 3);
			viewLayout[i].addSpanning(guiDefinitions.nuPGStaticText("_dur", 20, 40), row: 0, column: 4);
			viewLayout[i].addSpanning(trainDuration[i], row: 0, column: 5);
			viewLayout[i].addSpanning(trainProgress[i], row: 0, column: 6);
			viewLayout[i].addSpanning(trainDirection[i], row: 0, column: 7);
			viewLayout[i].addSpanning(scrubbEditor[i], row: 0, column: 8);
		};


		//load views into stacks
		n.collect{|i|
			stack.add(view[i])
		};


		^window.front;

	}

}