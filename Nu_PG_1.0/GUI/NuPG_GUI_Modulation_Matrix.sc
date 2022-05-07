NuPG_GUI_Modulator_Matrix {

	var <>window;
	var <>matrix;
	var <>offset;
	var <>polarity;

	build {|data, colorScheme = 0, modulators|

		var view, layout, slotGrid, slots, actions;
		var defs = NuPG_GUI_definitions;
		//list of all parameters + 9 additional oscillators
		var paramNames = 3.collect{[
			"_trigger frequency", "_grain frequency", "_envelope multiplication", "_pan", "_amp",
			"_fm amount", "_fm ration", "_flux",
		]}.flatten ++ ["_mod 1", "_mod 2", "_mod 3", "_mod 4"];


		window = Window.new("MODULATOR MATRIX",
			Rect.fromArray(defs.nuPGDimensions[42]), resizable: false);
		window.userCanClose = false;
		window.layout_(layout = GridLayout.new().margins_([3,3,3,3]).vSpacing_(2).hSpacing_(2));

		//how many slots?
		slots = 6.collect{defs.nuPGView(colorScheme) };
		slotGrid = 6.collect{GridLayout.new().margins_(3).vSpacing_(5).hSpacing_(5) };
		//layout for each view
		//add content to each slot
		6.collect{|i| slots[i].layout_(slotGrid[i])};


		matrix =  paramNames.size.collect{paramNames.size.collect{}};

		paramNames.size.collect{|k|
				//get names to StaticText
			var text = UserView().background_(Color.white).maxHeight_(150).maxWidth_(20)
			.drawFunc = { |view|
				var center = view.bounds.extent * 0.7;
				var rect = Rect(-105, -10, 150, 20);

    Pen.translate(center.x, center.y)
    .rotate(1.57, 0, 0)
    .stringLeftJustIn(paramNames[k], rect, defs.nuPGFont, Color.black);
};

				//put these on selected unit
				slotGrid[0].addSpanning(text, 0, k);
		};

			//param names
			paramNames.size.collect{|k|
				//get names to StaticText
			var text = UserView().background_(Color.white).maxHeight_(20).maxWidth_(150)
			.drawFunc = { |view|
				var center = view.bounds.extent * 0.7;
				var rect = Rect(-105, -15, 150, 20);

    Pen.translate(center.x, center.y)
    .rotate(0, 0, 0)
    .stringLeftJustIn(paramNames[k], rect, defs.nuPGFont, Color.black);
};
				//put these on selected unit
				slotGrid[1].addSpanning(text, k, 0);
		};

		//matrix
		//param names
		paramNames.size.collect{|i|
			paramNames.size.collect{|l|

			matrix[i][l] = defs.nuPGButton(
							[["[]", Color.white, Color.grey],
						     ["X", Color.black, Color.new255(250, 100, 90)]], 20, 20)
						.action_({|butt|
							var st = butt.value;
							switch(st,
								0, {},
								1, {}
					)});
					//put these on selected unit
				    slotGrid[2].addSpanning(matrix[i][l], i, l);
			}
		};





		//param names
	    layout.addSpanning(slots[0], 0, 1);
		//param names
	    layout.addSpanning(slots[1], 1, 0);
		//matrix
	    layout.addSpanning(slots[2], 1, 1);

		//window.front;
}
	visible {|boolean|
		^window.visible = boolean
	}

}