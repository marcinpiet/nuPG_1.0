NuPG_GUI_Control_View {

	var <>window;
	var <>trainPlayStopButton;
	var <>trainDuration, <>trainPlaybackDirection;
	var <>progressSlider, <>progressDisplay;
	var <>localActivators;
	var <>instanceMenu;
	var <>stack;

	draw {|dimensions, viewsList, n = 1|
		var layout, stackView, stackViewGrid;
		var collapseButton;
		var loopButton;
		var trainLabel, menuItems;
		var groups;

		//get GUI defs
		var guiDefinitions = NuPG_GUI_Definitions;
		//var sliderRecordPlayer = NuPG_Slider_Recorder_Palyer;
		//sliderRecordPlayer.data = data.data_progressSlider;
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//window
		//control window contains two separate views -> global and -> local
		//global is the same across all instances
		//local is instance specific, using stackView for multiple views
		window = Window("nuPG 2022", dimensions, resizable: false);
		window.userCanClose_(0);
		//window.alwaysOnTop_(true);
		window.view.background_(guiDefinitions.bAndKGreen);
		window.layout_(layout = GridLayout.new() );
		layout.margins_([3, 2, 2, 2]);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//global objects definition
		//trainLabel = guiDefinitions.nuPGStaticText("", 15, 80);
		//instances menu
		menuItems = n.collect{|i| "train_" ++ (i + 1).asString };
		instanceMenu = guiDefinitions.nuPGMenu(menuItems, 0, 70);
		instanceMenu.action_{|mn|
			viewsList.collect{|item, i|
				item.stack.index = instanceMenu.value;
		} };

		collapseButton = 3.collect{|l|
			var collapseIndices = [[5, 8, 11, 14], [6, 9, 12, 15], [7, 10, 13, 16]];

			guiDefinitions.nuPGButton([
				["_group_" ++ (1+l).asString, Color.black, Color.new255(250, 100, 90)],
				["_group_" ++ (1+l).asString, Color.black, Color.white]], 18, 50)
			.font_(guiDefinitions.nuPGFont(size: 9))
			.action_{|butt|
						var st = butt.value; st.postln;
						switch(st,
						0, { 4.collect{|k| viewsList[collapseIndices[l][k]].visible(1)}},
						1, { 4.collect{|k| viewsList[collapseIndices[l][k]].visible(0)}}
						)
					};
		};

		//insert into the view -> global
		//layout.addSpanning(trainLabel, row: 0, column: 0);
		layout.addSpanning(instanceMenu, row: 0, column: 0);
		3.collect{|i| layout.addSpanning(collapseButton[i], row: 0, column: 1 + i) };

		^window.front;

	}

}