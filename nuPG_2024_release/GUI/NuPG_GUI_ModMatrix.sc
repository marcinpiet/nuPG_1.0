NuPG_GUI_ModMatrix {

	var <>window;
	var <>stack;
	var <>data;
	var <>matrix;


	draw {|name, dimensions, modulatorsList, n = 1|

		var view, viewLayout;
		var modNum, paramNum, paramLabels, modLabels, paramNames, modNames, modEdit;
		var guiDefinitions = NuPG_GUI_Definitions;
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		//window
		window = Window(name, dimensions, resizable: false);
		window.userCanClose = false;
		window.view.background_(guiDefinitions.bAndKGreen);
		window.userCanClose = false;


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
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		data = n.collect{ 2.collect{} };
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		//place objects on view
		//table view editors
		modNames = ["_m1", "_m2", "_m3", "_m4"];
		paramNames = [
			"_fundamental",
			"_formant_01",
			"_formant_02",
			"_formant_03",
			"_env_01",
			"_env_02",
			"_env_03",
			"_pan_01",
			"_pan_02",
			"_pan_03",
			"_amp_01",
			"_amp_02",
			"_amp_03"
		];
		modNum = modNames.size;
		paramNum = paramNames.size;
		paramLabels = n.collect{ paramNum.collect{} };
		modLabels = n.collect{ modNum.collect{} };
		matrix = n.collect{ modNum.collect{ paramNum.collect{} }};
		modEdit = n.collect{ modNum.collect{} };

		n.collect{|i|

			modNum.collect{|k|
				modEdit[i][k] = guiDefinitions.nuPGButton(
						[["[E]", Color.white, Color.grey],
						   ["E", Color.black, Color.new255(250, 100, 90)]], 20, 20)
					.action = {|butt|

				var st = butt.value; st.postln;
				switch(st,
						0, {modulatorsList[k].visible(0)},
						1, {modulatorsList[k].visible(1)}
				)
			};
			};

			modNames.size.collect{|k|
				modLabels[i][k] = guiDefinitions.nuPGStaticText(modNames[k], 20, 20)
			};

			paramNames.size.collect{|k|
				paramLabels[i][k] = guiDefinitions.nuPGStaticText(paramNames[k], 20, 70)
			};

			modNum.collect{|k|
				paramNum.collect{|l|
					matrix[i][k][l] = guiDefinitions.nuPGButton(
						[["[]", Color.white, Color.grey],
							["X", Color.black, Color.new255(250, 100, 90)]], 20, 20)
					.valueAction_(0);
			}};
		};


		n.collect{|i|

			modNum.collect{|k|
				viewLayout[i].addSpanning(item: modEdit[i][k], row: 0, column: 1 + k)
			};

			modNum.collect{|k|
				viewLayout[i].addSpanning(item: modLabels[i][k], row: 1, column: 1 + k)
			};

			paramNum.collect{|k|
				viewLayout[i].addSpanning(item: paramLabels[i][k], row: 2 + k, column: 0)
			};

			modNum.collect{|k|
				paramNum.collect{|l|
					viewLayout[i].addSpanning(item: matrix[i][k][l], row: 2 + l, column: 1 + k);
			}};
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