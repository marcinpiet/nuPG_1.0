(
//set number of output channels to 16
Server.default.options.numOutputBusChannels = 16;
Server.default.options.memSize = 8192 * 10;
Server.default.options.numWireBufs = 2048;

//tables path
~path = Platform.userExtensionDir ++ "/Nu_PG_2.0/TABLES/";
~files = Platform.userExtensionDir ++ "/Nu_PG_2.0/FILES/";
//buffers
~buffers = 3.collect{|i|

	var bufnums = [[3,4,5],[6,7,8],[9,10,11]];


	3.collect{|l| Buffer.new(s, 2048,1, bufnum: bufnums[i][l])}
};


//data
~data = NuPG_Conductor.new;
~data.conductorInit;
~map = NuPG_Mapping.new;
//sliderPlayer
~sliderPlay = NuPG_SliderPalyer;
~sliderArray = ~sliderPlay.sliderArray;
~ppModArray = ~sliderPlay.ppModArray;
~pulsarArray = ~sliderPlay.pulsaretArray;
~envelopeArray = ~sliderPlay.envelopeArray;
~frequencyArray = ~sliderPlay.frequencyArray;
~scrubbArray = ~sliderPlay.scrubbArray;

~ppModRecordSlider = ~sliderPlay.ppModRecordTask(~data, ~ppModArray);
~ppModPlaybackSlider = ~sliderPlay.ppModPlaybackTask(~data, ~ppModArray);

~pulsaretRecordSlider = ~sliderPlay.pulsaretSliderRecordTask(~data, ~pulsarArray);
~pulsaretPlaybackSlider = ~sliderPlay.pulsaretSliderPlaybackTask(~data, ~pulsarArray, ~buffers);

~envelopeRecordSlider = ~sliderPlay.envelopeSliderRecordTask(~data, ~envelopeArray);
~envelopePlaybackSlider = ~sliderPlay.envelopeSliderPlaybackTask(~data, ~envelopeArray, ~buffers);

~frequencyRecordSlider = ~sliderPlay.frequencySliderRecordTask(~data, ~frequencyArray);
~frequencyPlaybackSlider = ~sliderPlay.frequencySliderPlaybackTask(~data, ~frequencyArray, ~buffers);

~scrubbRecordSlider = ~sliderPlay.scrubbRecordTask(~data, ~scrubbArray);
~scrubbPlaybackSlider = ~sliderPlay.scrubbPlaybackTask(~data, ~scrubbArray);

~sliderRecordTasks = ~sliderPlay.recordTask(~data, ~sliderArray);
~sliderPlayBackTasks =  ~sliderPlay.playbackTask(~data, ~sliderArray);

~pulsaretToBufferUpdater = ~map.pulsaretDataToBufferUpdater(~data, ~buffers);

//GUI
~main = NuPG_GUI_Main.new;
~main.build(~sliderRecordTasks, ~sliderPlayBackTasks);


~graphGUI = 5.collect{|i| NuPG_GUI_Graph_Editor.new };
5.do{|i| ~graphGUI[i].defaultTablePath = ~path};

5.do{|i|
	var params = [
		"_trigger_frequency",
		"_grain_frequency",
		"_envelope_multiplicator",
		"_pan",
		"_amp",
	];

	~graphGUI[i].build(coordinates: i, data: ~data, paramName: params[i], dataSlot: i)
};

~pattern = ~map;
~sequencer = NuPG_GUI_Sequencer_Micro.new;
~sequencer.build(pattern: ~pattern, editors: ~graphGUI);

//scrubber task
~scrubberTask = ~map.scrubberTask(~data, ~sequencer);
3.collect{|i|
	5.collect{|l|
	var param = [\param1, \param2, \param3, \param4, \param5];
	~scrubberTask[i].set(param[l], 0);
	}
};
//scrubber gui
~scrubberGUI = NuPG_GUI_Scrubber.new;
~scrubberGUI.build(recordTasks: ~scrubbRecordSlider, playbackTasks: ~scrubbPlaybackSlider, sequencer: ~sequencer, data: ~data, scrubberTask: ~scrubberTask);

//BUILD editors
~pulsaretEditor = NuPG_GUI_Pulsaret_Editor.new;
~envelopeEditor = NuPG_GUI_Envelope_Editor.new;
~frequencyEditor = NuPG_GUI_Frequency_Editor.new;
//for standalone path needs to be fixed
//~pulsarEditor.tablePath =  String.scDir ++
~pulsaretEditor.defaultTablePath = ~path;
~pulsaretEditor.build(data: ~data,  buffer: ~buffers, recordTasks: ~pulsaretRecordSlider, playbackTasks: ~pulsaretPlaybackSlider, dataToBufferUpdater: ~pulsaretToBufferUpdater);
~envelopeEditor.defaultTablePath = ~path;
~envelopeEditor.build(data: ~data,  buffer: ~buffers, recordTasks: ~envelopeRecordSlider, playbackTasks: ~envelopePlaybackSlider);
~frequencyEditor.defaultTablePath = ~path;
~frequencyEditor.build(data: ~data,  buffer: ~buffers, recordTasks: ~frequencyRecordSlider, playbackTasks: ~frequencyPlaybackSlider);

//SHAPER
~pulsaretShaper = NuPG_GUI_Pulsaret_Shaper.new;
~pulsaretShaper.build(data: ~data, buffer: ~buffers);

~tables = NuPG_GUI_Tables.new;
~tables.build(data: ~data, buffers: ~buffers, pulsaretEditor: ~pulsaretEditor, envelopeEditor: ~envelopeEditor, frequencyEditor: ~frequencyEditor, pulsaretShaper: ~pulsaretShaper);

~ppModulation = NuPG_GUI_ppModulation.new;
~ppModulation.build(recordTasks: ~ppModRecordSlider, playbackTasks: ~ppModPlaybackSlider);

~sequencerSpeed = NuPG_GUI_Micro_ReadingControl.new;

~synthDefMenu = NuPG_GUI_SynthDefMenu.new;
~synthDefMenu.build;

~masking = NuPG_GUI_Masking.new;
~sieveMasking = NuPG_GUI_Sieve_Masking.new;
~sieveMasking.path = ~files;
~sieveMasking.build(data: ~data);
~masking.build(data: ~data, sieveMaskingEditor: ~sieveMasking);

//////////////////////////////////////////////////////////////////////////////////////////////////
~modulatorsChain = NuPG_Modulators.modulatorChain;
~modulatorsWrap = NuPG_Modulators.modulatorWrap;
~subMod = NuPG_Modulators.subModulators;

//modulator editor
~modEditor = 4.collect{ NuPG_GUI_Modulators.new };
~modEditor = 4.collect{|i| ~modEditor[i].build(data: ~data, iter: i, modWrap: ~modulatorsWrap, mod: ~subMod) };
//MATRIX

~modulationMatrix = NuPG_GUI_Modulator_Matrix.new;
~modulationMatrix.build(data: ~data, ndef: 3.collect{|i| NuPG_Ndefs.trains[i]}, modulatorsChain: ~modulatorsChain, modulatorEditor: ~modEditor);
//////////////////////////////////////////////////////////////////////////////////////////////////
//CONTROL
~control = NuPG_GUI_Control.new;
~control.build(views: [~main, ~tables, ~pulsaretEditor, ~envelopeEditor, ~frequencyEditor, ~sequencer, ~ppModulation, ~sequencerSpeed, ~scrubberGUI, ~pulsaretShaper, ~masking, ~sieveMasking] ++ ~scrubberGUI ++ ~graphGUI ++ ~modulationMatrix ++ ~modEditor, pattern: ~pattern, data: ~data, buffers: ~buffers, pulsarToBufferUpdater: ~pulsaretToBufferUpdater);

/////////////////////////////////////////////////////////////////////////////////////////////////
~sequencerSpeed.build(sequencer: ~sequencer, scrubberGUI: ~scrubberGUI, controlGUI: ~control,
	graphEditorGUI: ~graphGUI, scrubberTask: ~scrubberTask);

//~parameterLinking = NuPG_GUI_ParameterLinking.new;
//~parameterLinking.build(data: ~data);

~extensions = NuPG_GUI_Extensions.new;
~extensions.build(ppModulation: [~ppModulation], synthDefMenu: ~synthDefMenu, masking: ~masking, wfMatrix: ~modulationMatrix);


~map.mapDataToGUI(~data, [~main, ~ppModulation, ~sequencer, ~sequencerSpeed, ~masking, ~sieveMasking, ~tables, ~pulsaretEditor, ~envelopeEditor, ~frequencyEditor, ~pulsaretShaper, ~graphGUI, ~control, ~scrubberGUI, ~modulationMatrix, ~modEditor]);
//matrix mapping
//ranges & matrix buttons

3.collect{|i|
	8.collect{|l|
		2.collect{|k|
			~data.data_modulatorsRange[i][l][k].connect(~modulationMatrix.range[i][l][k]);
		};
	/*4.collect{|k|

			~data.data_matrix[i][l][k].connect(~modulationMatrix.matrix[i][l][k]);
};*/
	}
};

~server = NuPG_GUI_Server.new;
~server.modChain = ~modulatorsChain;
~server.modWrap = ~modulatorsWrap;
~server.mod = ~subMod;
~server.build(data: ~data, buffers: ~buffers, pattern: ~pattern, map: ~map);
)
