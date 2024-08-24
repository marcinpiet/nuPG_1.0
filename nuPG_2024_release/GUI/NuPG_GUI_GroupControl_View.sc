NuPG_GUI_GroupControl_View {

	var <>window;
	var <>localActivators;
	var <>stack;

	draw {|dimensions, synthesis, n = 1|
		var view, viewLayout;
		var groups;

		//get GUI defs
		var guiDefinitions = NuPG_GUI_Definitions;
		//var sliderRecordPlayer = NuPG_Slider_Recorder_Palyer;
		//sliderRecordPlayer.data = data.data_progressSlider;
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//window
		window = Window("_groups control", dimensions, resizable: false);
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
		localActivators = n.collect{ 3.collect{} }; //3 groups of [formant, envMul, pan, amp]
		//local, instance specific objects
		n.collect{|i|

			3.collect{|l|
				localActivators[i][l] = guiDefinitions.nuPGButton([
					["ACTIVATE", Color.black, Color.white], ["DEACTIVATE", Color.black, Color.new255(250, 100, 90)]], 18, 290);
				localActivators[i][l].font_(guiDefinitions.nuPGFont(size: 9));
				localActivators[i][l].action_{|butt|
				var st = butt.value; st.postln;
				switch(st,
						0, {synthesis.trainInstances[i].set(("group_" ++ (l+1) ++ "_onOff").asSymbol, 0);
							("group_" ++ (l+1) ++ "_onOff").postln;
						},
						1, {synthesis.trainInstances[i].set(("group_" ++ (l+1) ++ "_onOff").asSymbol, 1);
							("group_" ++ (l+1) ++ "_onOff").postln;
						}
				)
			};
			};

		};

		//load int views
		n.collect{|i|
			3.collect{|l|
				viewLayout[i].addSpanning(localActivators[i][l], row: 0, column: 5 + l);
			};

		};


		//load views into stacks
		n.collect{|i|
			stack.add(view[i])
		};


		^window.front;

	}

}