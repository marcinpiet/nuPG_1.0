NuPG_GUI_Extensions_View {

	var <>window;
	var <>stack;

	draw {|dimensions, viewsList, n = 1|
		var layout, stackView, stackViewGrid;
		var extensionsButtons;

		//get GUI defs
		var guiDefinitions = NuPG_GUI_Definitions;
		//var sliderRecordPlayer = NuPG_Slider_Recorder_Palyer;
		//sliderRecordPlayer.data = data.data_progressSlider;
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//window
		//control window contains two separate views -> global and -> local
		//global is the same across all instances
		window = Window("_extensions", dimensions, resizable: false);
		window.userCanClose_(0);
		//window.alwaysOnTop_(true);
		window.view.background_(guiDefinitions.bAndKGreen);
		window.layout_(layout = GridLayout.new() );
		layout.margins_([3, 2, 2, 2]);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//global objects definition
		extensionsButtons = 6.collect{|i|
			var extensions = ["MODULATORS", "FOURIER", "MASKING", "SIEVES", "GROUPS\n OFFSET", "MATRIX\n MODULATION "];
			guiDefinitions.nuPGButton([
				[extensions[i], Color.black, Color.white],
				[extensions[i], Color.black, Color.new255(250, 100, 90)]], 40, 80)
			.action_{|butt|
						var st = butt.value; st.postln;
						switch(st,
					0, { viewsList[i].visible(0) },
					1, { viewsList[i].visible(1)  }
						)
					};
		};

		//insert into the view
		6.collect{|i|
			var row = [0, 0, 0, 1, 1, 1];
			var col = [0, 1, 2, 0, 1, 2];
			layout.addSpanning(extensionsButtons[i], row: row[i], column: col[i])
		};

		^window.front;

	}

}