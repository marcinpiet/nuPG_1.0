NuPG_StartUp {

	var <>channels;
	var <>instances;
	var <>here, <>tablesPath, <>filesPath, <>presetsPath;
	var <>server;
	var <>envelope, <>envelope_buffers;
	var <>pulsaret, <>pulsaret_buffers;
	var <>freq, <>frequency_buffers;
	var <>data;
	var <>synthesis;
	var <>loopTask, <>scrubbTask, <>singleShotTask, <>fundPatt;
	var <>sliderRecordPlaybackTask, <>scrubbArray, <>scrubbRecordTask, <>scrubbPlaybackTask;
	var <>guiDefinitions;
	var <>control, <>extensions, <>progressSlider, <>scrubber, <>record;
	var <>pulsaretTable, <>envelopeTable, <>main, <>maskingTable, <>fundamentalTable;
	var <>formantOneTable, <>formantTwoTable, <>formantThreeTable;
	var <>envelopeMult_One, <>envelopeMult_Two, <>envelopeMult_Three;
	var <>panOneTable, <>panTwoTable, <>panThreeTable;
	var <>ampOneTable, <>ampTwoTable, <>ampThreeTable;
	var <>groupsControl, <>trainControl, <>fourier, <>sieves, <>masking, <>modulators;
	var <>pulsaretTableEditor, <>envelopeTableEditor, <>probabilityTableEditor, <>fundamentalTableEditor,
	<>formantOneTableEditor, <>formantTwoTableEditor, <>formantThreeTableEditor, <>envelopeMult_One_Editor,
	<>envelopeMult_Two_Editor, <>envelopeMult_Three_Editor,
	<>panOneTable_Editor, <>panTwoTable_Editor, <>panThreeTable_Editor,
	<>ampOneTable_Editor, <>ampTwoTable_Editor, <>ampThreeTable_Editor;
	var <>presets;
	var <>modulationTable, <>modulationTableEditor, <>modulationRatioTable, <>modulationRatioEditor,
	<>multiparameterModulationTable, <>multiparameterModulationTableEditor;
	var <>groupsOffest;
	var <>matrixMod, <>modulator1, <>modulator2, <>modulator3, <>modulator4;


	start {|numChannels = 2, numInstances = 1|

		this.serverSetup(numChannels, numInstances);
		this.setPaths();
		this.serverStart();

	}

	serverSetup {|numChannels = 2, numInstances = 1|

		//set number of output channels
		channels = numChannels;
		//settable number of instances = number of pulsar streams
		//this needs to be set at build time and cannot be changed after
		instances = numInstances;

		//set the server
		server = Server.default;
		// server settings
		server.options.recChannels_(channels).recSampleFormat_("int24");
		server.options.memSize = 192000 * 24;
		server.options.numOutputBusChannels = 32;
		server.options.numWireBufs = 256;

		"Server Setup".postln;
	}

	setPaths {
		// set paths for tables, files and presets.
		// default location is here, in the nuPG quark folder:
		here = thisProcess.nowExecutingPath.dirname;
		// keep tablesPath etc if given already, else use default:
		tablesPath = tablesPath ?? { here +/+ "/TABLES/" };
		filesPath =  filesPath ?? { here +/+ "/FILES/" };
		presetsPath = presetsPath ?? { here +/+ "/PRESETS/" };

		"Paths SetUp".postln;

	}

	serverStart {

		server.waitForBoot({
			// Get corresponding number of buffers
			// Envelope random data to fill the buffer
			envelope = Signal.sineFill(2048, { 0.0.rand }.dup(7));
			envelope_buffers = instances.collect { |i| Buffer.loadCollection(server, envelope, 1) };

			// Pulsaret waveform random data to fill the buffer
			pulsaret = Signal.sineFill(2048, { 0.0.rand }.dup(7));
			pulsaret_buffers = instances.collect { |i| Buffer.loadCollection(server, pulsaret, 1) };

			// Frequency
			freq = Signal.newClear(2048).fill(1.0);
			frequency_buffers = instances.collect { |i| Buffer.loadCollection(server, freq, 1) };

			// Generate global data structure
			data = NuPG_Data.new;
			data.conductorInit(instances);
			instances.collect { |i|
				data.conductor.addCon(\con_ ++ i, data.instanceGeneratorFunction(i));
			};

			// Generate an equal number of synthesis graphs
			synthesis = NuPG_Synthesis.new;
			synthesis.trains(instances, numChannels: channels);

			// Map buffers <-> data <-> synthesis
			loopTask = NuPG_LoopTask.new;
			loopTask.load(data: data, synthesis: synthesis, n: instances);
			instances.collect { |i|
				loopTask.tasks[i].set(\playbackDirection, 0);
			};

			scrubbTask = NuPG_ScrubbTask.new;
			scrubbTask.load(data: data, synthesis: synthesis, n: instances);

			sliderRecordPlaybackTask = NuPG_SliderRecordPlaybackTasks.new;
			scrubbArray = sliderRecordPlaybackTask.scrubbArray(n: instances);
			scrubbRecordTask = sliderRecordPlaybackTask.scrubbRecordTask(
				data: data, array: scrubbArray, n: instances
			);
			scrubbPlaybackTask = sliderRecordPlaybackTask.scrubbPlaybackTask(
				data: data, array: scrubbArray, n: instances
			);

			// GUI definitions and parameters
			guiDefinitions = NuPG_GUI_Definitions;

			// Main GUI (intermediary control)
			main = NuPG_GUI_Main.new;
			main.draw("_main", guiDefinitions.mainViewDimensions, n: instances);
			instances.collect { |i|
				main.data[i] = data.data_main[i];
				13.collect { |l|
					data.data_main[i][l].connect(main.slider[i][l]);
					data.data_main[i][l].connect(main.numberDisplay[i][l]);
				};
			};

			// Modulation amount
			modulationTable = NuPG_GUI_Table_View.new;
			modulationTable.defaultTablePath = tablesPath;
			modulationTable.draw("_modulation amount", guiDefinitions.modulationAmountViewDimensions, n: instances);
			instances.collect { |i|
				modulationTable.data[i] = data.data_modulationAmount[i];
				data.data_modulationAmount[i].connect(modulationTable.table[i]);
				2.collect { |l|
					data.data_modulationAmount_maxMin[i][l].connect(modulationTable.minMaxValues[i][l]);
				};
			};
			modulationTable.visible(0);

			// Modulation table editor
			modulationTableEditor = NuPG_GUI_Table_Editor_View.new;
			modulationTableEditor.defaultTablePath = tablesPath;
			modulationTableEditor.draw("_modulation amount editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect { |i|
				modulationTableEditor.data[i] = data.data_modulationAmount[i];
				data.data_modulationAmount[i].connect(modulationTableEditor.table[i]);
				2.collect { |l|
					data.data_modulationAmount_maxMin[i][l].connect(modulationTableEditor.minMaxValues[i][l]);
				};
			};
			modulationTable.editorView = modulationTableEditor;

			// Modulation ratio
			modulationRatioTable = NuPG_GUI_Table_View.new;
			modulationRatioTable.defaultTablePath = tablesPath;
			modulationRatioTable.draw("_modulation ratio", guiDefinitions.modulationRatioViewDimensions, n: instances);
			instances.collect { |i|
				modulationRatioTable.data[i] = data.data_modulationRatio[i];
				data.data_modulationRatio[i].connect(modulationRatioTable.table[i]);
				2.collect { |l|
					data.data_modulationRatio_maxMin[i][l].connect(modulationRatioTable.minMaxValues[i][l]);
				};
			};
			modulationRatioTable.visible(0);

			// Modulation ratio editor
			modulationRatioEditor = NuPG_GUI_Table_Editor_View.new;
			modulationRatioEditor.defaultTablePath = tablesPath;
			modulationRatioEditor.draw("_modulation ratio editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect { |i|
				modulationRatioEditor.data[i] = data.data_modulationRatio[i];
				data.data_modulationRatio[i].connect(modulationRatioEditor.table[i]);
				2.collect { |l|
					data.data_modulationRatio_maxMin[i][l].connect(modulationRatioEditor.minMaxValues[i][l]);
				};
			};
			modulationRatioTable.editorView = modulationRatioEditor;

			// Multi-parameter modulation table
			multiparameterModulationTable = NuPG_GUI_Table_View.new;
			multiparameterModulationTable.defaultTablePath = tablesPath;
			multiparameterModulationTable.draw("_multi parameter modulation", guiDefinitions.multiParameterModulationViewDimensions, n: instances);
			instances.collect { |i|
				multiparameterModulationTable.data[i] = data.data_multiParamModulation[i];
				data.data_multiParamModulation[i].connect(multiparameterModulationTable.table[i]);
				2.collect { |l|
					data.data_mulParamModulation_maxMin[i][l].connect(multiparameterModulationTable.minMaxValues[i][l]);
				};
			};
			multiparameterModulationTable.visible(0);

			// Multi-parameter modulation table editor
			multiparameterModulationTableEditor = NuPG_GUI_Table_Editor_View.new;
			multiparameterModulationTableEditor.defaultTablePath = tablesPath;
			multiparameterModulationTableEditor.draw("_multi parameter modulation editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect { |i|
				multiparameterModulationTableEditor.data[i] = data.data_multiParamModulation[i];
				data.data_multiParamModulation[i].connect(multiparameterModulationTableEditor.table[i]);
				2.collect { |l|
					data.data_mulParamModulation_maxMin[i][l].connect(multiparameterModulationTableEditor.minMaxValues[i][l]);
				};
			};
			multiparameterModulationTable.editorView = multiparameterModulationTableEditor;

			//modulators
			modulators = NuPG_GUI_Modulators.new;
			modulators.draw("_modulators", guiDefinitions.modulatorsViewDimensions, synthesis, n: instances);
			modulators.tables = [multiparameterModulationTable, modulationRatioTable, modulationTable];
			//mapping
			instances.collect{|i|
				//gui <-> data
				modulators.data[i] = data.data_modulators[i];
				3.collect{|l|
					data.data_modulators[i][l].connect(modulators.slider[i][l]);
					data.data_modulators[i][l].connect(modulators.numberDisplay[i][l]);
				};
			};

			//groups offset
			groupsOffest = NuPG_GUI_GroupsOffset.new;
			groupsOffest.draw("_groupsOffset", guiDefinitions.groupsOffsetViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				groupsOffest.data[i] = data.data_groupsOffset[i];
				3.collect{|l|
					data.data_groupsOffset[i][l].connect(groupsOffest.slider[i][l]);
					data.data_groupsOffset[i][l].connect(groupsOffest.numberDisplay[i][l]);
				};
			};

			//pulsaret table
			pulsaretTable = NuPG_GUI_Table_View.new;
			pulsaretTable.defaultTablePath = tablesPath;
			pulsaretTable.draw("_pulsaret waveform", guiDefinitions.pulsaretViewDimensions, buffer: 1, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				pulsaretTable.data[i] = data.data_pulsaret[i];
				data.data_pulsaret[i].connect(pulsaretTable.table[i]);
				2.collect{|l|
					data.data_pulsaret_maxMin[i][l].connect(pulsaretTable.minMaxValues[i][l]);
				};
				//gui <-> buffers
				pulsaretTable.setBuffer[i] = pulsaret_buffers[i];
			};

			//pulsaret table editor
			pulsaretTableEditor = NuPG_GUI_Table_Editor_View.new;
			pulsaretTableEditor.defaultTablePath = tablesPath;
			pulsaretTableEditor.draw("_pulsaret editor", guiDefinitions.tableEditorViewDimensions, buffer: 1, n: instances);
			instances.collect{|i|
				//gui <-> data
				pulsaretTableEditor.data[i] = data.data_pulsaret[i];
				data.data_pulsaret[i].connect(pulsaretTableEditor.table[i]);
				2.collect{|l|
					data.data_pulsaret_maxMin[i][l].connect(pulsaretTableEditor.minMaxValues[i][l]);
				};
				//gui <-> buffers
				pulsaretTableEditor.setBuffer[i] = pulsaret_buffers[i];
			};
			pulsaretTable.editorView = pulsaretTableEditor;

			//envelope
			envelopeTable = NuPG_GUI_Table_View.new;
			envelopeTable.defaultTablePath = tablesPath;
			envelopeTable.draw("_envelope", guiDefinitions.envelopeViewDimensions, buffer: 1, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				envelopeTable.data[i] = data.data_envelope[i];
				data.data_envelope[i].connect(envelopeTable.table[i]);
				2.collect{|l|
					data.data_envelope_maxMin[i][l].connect(envelopeTable.minMaxValues[i][l]);
				};
				//gui <-> buffers
				envelopeTable.setBuffer[i] = envelope_buffers[i];
			};

			//envelope table editor
			envelopeTableEditor = NuPG_GUI_Table_Editor_View.new;
			envelopeTableEditor.defaultTablePath = tablesPath;
			envelopeTableEditor.draw("_envelope editor", guiDefinitions.tableEditorViewDimensions, buffer: 1, n: instances);
			instances.collect{|i|
				//gui <-> data
				envelopeTableEditor.data[i] = data.data_envelope[i];
				data.data_envelope[i].connect(envelopeTableEditor.table[i]);
				2.collect{|l|
					data.data_envelope_maxMin[i][l].connect(envelopeTableEditor.minMaxValues[i][l]);
				};
				//gui <-> buffers
				envelopeTableEditor.setBuffer[i] = envelope_buffers[i];
			};
			envelopeTable.editorView = envelopeTableEditor;

			//probability masking
			maskingTable = NuPG_GUI_Table_View.new;
			maskingTable.defaultTablePath = tablesPath;
			maskingTable.draw("_pulseProbabilityMask", guiDefinitions.maskingViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				maskingTable.data[i] = data.data_probabilityMask[i];
				data.data_probabilityMask[i].connect(maskingTable.table[i]);
				2.collect{|l|
					data.data_probabilityMask_maxMin[i][l].connect(maskingTable.minMaxValues[i][l]);
				};
			};

			//probability table editor
			probabilityTableEditor = NuPG_GUI_Table_Editor_View.new;
			probabilityTableEditor.defaultTablePath = tablesPath;
			probabilityTableEditor.draw("_pulseProbabilityMask editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				probabilityTableEditor.data[i] = data.data_probabilityMask[i];
				data.data_probabilityMask[i].connect(probabilityTableEditor.table[i]);
				2.collect{|l|
					data.data_probabilityMask_maxMin[i][l].connect(probabilityTableEditor.minMaxValues[i][l]);
				};
			};
			maskingTable.editorView = probabilityTableEditor;

			//fundamental frequency
			fundamentalTable = NuPG_GUI_Table_View.new;
			fundamentalTable.defaultTablePath = tablesPath;
			fundamentalTable.draw("_fundamentalFrequency", guiDefinitions.fundamentalViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				fundamentalTable.data[i] = data.data_fundamentalFrequency[i];
				data.data_fundamentalFrequency[i].connect(fundamentalTable.table[i]);
				2.collect{|l|
					data.data_fundamentalFrequency_maxMin[i][l].connect(fundamentalTable.minMaxValues[i][l]);
				};
			};
			fundamentalTable.pattern[0] = fundPatt;

			//fundamental table editor
			fundamentalTableEditor = NuPG_GUI_Table_Editor_View.new;
			fundamentalTableEditor.defaultTablePath = tablesPath;
			fundamentalTableEditor.draw("_fundamentalFrequency editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				fundamentalTableEditor.data[i] = data.data_fundamentalFrequency[i];
				data.data_fundamentalFrequency[i].connect(fundamentalTableEditor.table[i]);
				2.collect{|l|
					data.data_fundamentalFrequency_maxMin[i][l].connect(fundamentalTableEditor.minMaxValues[i][l]);
				};
			};
			fundamentalTable.editorView = fundamentalTableEditor;
			fundamentalTableEditor.parentView = fundamentalTable;

			//formant frequency one
			formantOneTable = NuPG_GUI_Table_View.new;
			formantOneTable.defaultTablePath = tablesPath;
			formantOneTable.draw("_formantFrequency_One", guiDefinitions.formantOneViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				formantOneTable.data[i] = data.data_formantFrequencyOne[i];
				data.data_formantFrequencyOne[i].connect(formantOneTable.table[i]);
				2.collect{|l|
					data.data_formantFrequencyOne_maxMin[i][l].connect(formantOneTable.minMaxValues[i][l]);
				};
			};

			//formant one table editor
			formantOneTableEditor = NuPG_GUI_Table_Editor_View.new;
			formantOneTableEditor.defaultTablePath = tablesPath;
			formantOneTableEditor.draw("_formantFrequency_One editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				formantOneTableEditor.data[i] = data.data_formantFrequencyOne[i];
				data.data_formantFrequencyOne[i].connect(formantOneTableEditor.table[i]);
				2.collect{|l|
					data.data_formantFrequencyOne_maxMin[i][l].connect(formantOneTableEditor.minMaxValues[i][l]);
				};
			};
			formantOneTable.editorView = formantOneTableEditor;
			formantOneTableEditor.parentView = formantOneTable;

			//formant frequency two
			formantTwoTable = NuPG_GUI_Table_View.new;
			formantTwoTable.defaultTablePath = tablesPath;
			formantTwoTable.draw("_formantFrequency_Two", guiDefinitions.formantTwoViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				formantTwoTable.data[i] = data.data_formantFrequencyTwo[i];
				data.data_formantFrequencyTwo[i].connect(formantTwoTable.table[i]);
				2.collect{|l|
					data.data_formantFrequencyTwo_maxMin[i][l].connect(formantTwoTable.minMaxValues[i][l]);
				};
			};

			//formant two table editor
			formantTwoTableEditor = NuPG_GUI_Table_Editor_View.new;
			formantTwoTableEditor.defaultTablePath = tablesPath;
			formantTwoTableEditor.draw("_formantFrequency_Two editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				formantTwoTableEditor.data[i] = data.data_formantFrequencyTwo[i];
				data.data_formantFrequencyTwo[i].connect(formantTwoTableEditor.table[i]);
				2.collect{|l|
					data.data_formantFrequencyTwo_maxMin[i][l].connect(formantTwoTableEditor.minMaxValues[i][l]);
				};
			};
			formantTwoTable.editorView = formantTwoTableEditor;
			formantTwoTableEditor.parentView = formantTwoTable;

			//formant frequency three
			//formant frequency three
			formantThreeTable = NuPG_GUI_Table_View.new;
			formantThreeTable.defaultTablePath = tablesPath;
			formantThreeTable.draw("_formantFrequency_Three", guiDefinitions.formantThreeViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				formantThreeTable.data[i] = data.data_formantFrequencyThree[i];
				data.data_formantFrequencyThree[i].connect(formantThreeTable.table[i]);
				2.collect{|l|
					data.data_formantFrequencyThree_maxMin[i][l].connect(formantThreeTable.minMaxValues[i][l]);
				};
			};

			//formant three table editor
			formantThreeTableEditor = NuPG_GUI_Table_Editor_View.new;
			formantThreeTableEditor.defaultTablePath = tablesPath;
			formantThreeTableEditor.draw("_formantFrequency_Three editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				formantThreeTableEditor.data[i] = data.data_formantFrequencyThree[i];
				data.data_formantFrequencyThree[i].connect(formantThreeTableEditor.table[i]);
				2.collect{|l|
					data.data_formantFrequencyThree_maxMin[i][l].connect(formantThreeTableEditor.minMaxValues[i][l]);
				};
			};
			formantThreeTable.editorView = formantThreeTableEditor;
			formantThreeTableEditor.parentView = formantThreeTable;

			//envelope multiplication one
			envelopeMult_One = NuPG_GUI_Table_View.new;
			envelopeMult_One.defaultTablePath = tablesPath;
			envelopeMult_One.draw("_envelopeDil_One", guiDefinitions.envelopeOneViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				envelopeMult_One.data[i] = data.data_envelopeMulOne[i];
				data.data_envelopeMulOne[i].connect(envelopeMult_One.table[i]);
				2.collect{|l|
					data.data_envelopeMulOne_maxMin[i][l].connect(envelopeMult_One.minMaxValues[i][l]);
				};
			};

			//envelope multiplication one table editor
			envelopeMult_One_Editor = NuPG_GUI_Table_Editor_View.new;
			envelopeMult_One_Editor.defaultTablePath = tablesPath;
			envelopeMult_One_Editor.draw("_envelopeDil_One editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				envelopeMult_One_Editor.data[i] = data.data_envelopeMulOne[i];
				data.data_envelopeMulOne[i].connect(envelopeMult_One_Editor.table[i]);
				2.collect{|l|
					data.data_envelopeMulOne_maxMin[i][l].connect(envelopeMult_One_Editor.minMaxValues[i][l]);
				};
			};
			envelopeMult_One.editorView = envelopeMult_One_Editor;
			envelopeMult_One_Editor.parentView = envelopeMult_One;

			//envelope multiplication two
			envelopeMult_Two = NuPG_GUI_Table_View.new;
			envelopeMult_Two.defaultTablePath = tablesPath;
			envelopeMult_Two.draw("_envelopeDil_Two", guiDefinitions.envelopeTwoViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				envelopeMult_Two.data[i] = data.data_envelopeMulTwo[i];
				data.data_envelopeMulTwo[i].connect(envelopeMult_Two.table[i]);
				2.collect{|l|
					data.data_envelopeMulTwo_maxMin[i][l].connect(envelopeMult_Two.minMaxValues[i][l]);
				};
			};

			//envelope multiplication two table editor
			envelopeMult_Two_Editor = NuPG_GUI_Table_Editor_View.new;
			envelopeMult_Two_Editor.defaultTablePath = tablesPath;
			envelopeMult_Two_Editor.draw("_envelopeDil_Two editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				envelopeMult_Two_Editor.data[i] = data.data_envelopeMulTwo[i];
				data.data_envelopeMulTwo[i].connect(envelopeMult_Two_Editor.table[i]);
				2.collect{|l|
					data.data_envelopeMulTwo_maxMin[i][l].connect(envelopeMult_Two_Editor.minMaxValues[i][l]);
				};
			};
			envelopeMult_Two.editorView = envelopeMult_Two_Editor;
			envelopeMult_Two_Editor.parentView = envelopeMult_Two;

			//envelope multiplication three
			envelopeMult_Three = NuPG_GUI_Table_View.new;
			envelopeMult_Three.defaultTablePath = tablesPath;
			envelopeMult_Three.draw("_envelopeDil_Three", guiDefinitions.envelopeThreeViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				envelopeMult_Three.data[i] = data.data_envelopeMulThree[i];
				data.data_envelopeMulThree[i].connect(envelopeMult_Three.table[i]);
				2.collect{|l|
					data.data_envelopeMulThree_maxMin[i][l].connect(envelopeMult_Three.minMaxValues[i][l]);
				};
			};

			//envelope multiplication three table editor
			envelopeMult_Three_Editor = NuPG_GUI_Table_Editor_View.new;
			envelopeMult_Three_Editor.defaultTablePath = tablesPath;
			envelopeMult_Three_Editor.draw("_envelopeDil_Three editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				envelopeMult_Three_Editor.data[i] = data.data_envelopeMulThree[i];
				data.data_envelopeMulThree[i].connect(envelopeMult_Three_Editor.table[i]);
				2.collect{|l|
					data.data_envelopeMulThree_maxMin[i][l].connect(envelopeMult_Three_Editor.minMaxValues[i][l]);
				};
			};
			envelopeMult_Three.editorView = envelopeMult_Three_Editor;
			envelopeMult_Three_Editor.parentView = envelopeMult_Three;

			//pan one
			panOneTable = NuPG_GUI_Table_View.new;
			panOneTable.defaultTablePath = tablesPath;
			panOneTable.draw("_pan_One", guiDefinitions.panOneViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				panOneTable.data[i] = data.data_panOne[i];
				data.data_panOne[i].connect(panOneTable.table[i]);
				2.collect{|l|
					data.data_panOne_maxMin[i][l].connect(panOneTable.minMaxValues[i][l]);
				};
			};

			//pan one table editor
			panOneTable_Editor = NuPG_GUI_Table_Editor_View.new;
			panOneTable_Editor.defaultTablePath = tablesPath;
			panOneTable_Editor.draw("_pan_One editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				panOneTable_Editor.data[i] = data.data_panOne[i];
				data.data_panOne[i].connect(panOneTable_Editor.table[i]);
				2.collect{|l|
					data.data_panOne_maxMin[i][l].connect(panOneTable_Editor.minMaxValues[i][l]);
				};
			};
			panOneTable.editorView = panOneTable_Editor;
			panOneTable_Editor.parentView = panOneTable;

			//pan two
			panTwoTable = NuPG_GUI_Table_View.new;
			panTwoTable.defaultTablePath = tablesPath;
			panTwoTable.draw("_pan_Two", guiDefinitions.panTwoViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				panTwoTable.data[i] = data.data_panTwo[i];
				data.data_panTwo[i].connect(panTwoTable.table[i]);
				2.collect{|l|
					data.data_panTwo_maxMin[i][l].connect(panTwoTable.minMaxValues[i][l]);
				};
			};

			//pan two table editor
			panTwoTable_Editor = NuPG_GUI_Table_Editor_View.new;
			panTwoTable_Editor.defaultTablePath = tablesPath;
			panTwoTable_Editor.draw("_pan_Two editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				panTwoTable_Editor.data[i] = data.data_panTwo[i];
				data.data_panTwo[i].connect(panTwoTable_Editor.table[i]);
				2.collect{|l|
					data.data_panTwo_maxMin[i][l].connect(panTwoTable_Editor.minMaxValues[i][l]);
				};
			};
			panTwoTable.editorView = panTwoTable_Editor;
			panTwoTable_Editor.parentView = panTwoTable;

			//pan three
			panThreeTable = NuPG_GUI_Table_View.new;
			panThreeTable.defaultTablePath = tablesPath;
			panThreeTable.draw("_pan_Three", guiDefinitions.panThreeViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				panThreeTable.data[i] = data.data_panThree[i];
				data.data_panThree[i].connect(panThreeTable.table[i]);
				2.collect{|l|
					data.data_panThree_maxMin[i][l].connect(panThreeTable.minMaxValues[i][l]);
				};
			};

			//pan three table editor
			panThreeTable_Editor = NuPG_GUI_Table_Editor_View.new;
			panThreeTable_Editor.defaultTablePath = tablesPath;
			panThreeTable_Editor.draw("_pan_Three editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				panThreeTable_Editor.data[i] = data.data_panThree[i];
				data.data_panThree[i].connect(panThreeTable_Editor.table[i]);
				2.collect{|l|
					data.data_panThree_maxMin[i][l].connect(panThreeTable_Editor.minMaxValues[i][l]);
				};
			};
			panThreeTable.editorView = panThreeTable_Editor;
			panThreeTable_Editor.parentView = panThreeTable;

			//amp one
			ampOneTable = NuPG_GUI_Table_View.new;
			ampOneTable.defaultTablePath = tablesPath;
			ampOneTable.draw("_amp_One", guiDefinitions.ampOneViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				ampOneTable.data[i] = data.data_ampOne[i];
				data.data_ampOne[i].connect(ampOneTable.table[i]);
				2.collect{|l|
					data.data_ampOne_maxMin[i][l].connect(ampOneTable.minMaxValues[i][l]);
				};
			};

			//amp one table editor
			ampOneTable_Editor = NuPG_GUI_Table_Editor_View.new;
			ampOneTable_Editor.defaultTablePath = tablesPath;
			ampOneTable_Editor.draw("_amp_One editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				ampOneTable_Editor.data[i] = data.data_ampOne[i];
				data.data_ampOne[i].connect(ampOneTable_Editor.table[i]);
				2.collect{|l|
					data.data_ampOne_maxMin[i][l].connect(ampOneTable_Editor.minMaxValues[i][l]);
				};
			};
			ampOneTable.editorView = ampOneTable_Editor;
			ampOneTable_Editor.parentView = ampOneTable;

			//amp two
			ampTwoTable = NuPG_GUI_Table_View.new;
			ampTwoTable.defaultTablePath = tablesPath;
			ampTwoTable.draw("_amp_Two", guiDefinitions.ampTwoViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				ampTwoTable.data[i] = data.data_ampTwo[i];
				data.data_ampTwo[i].connect(ampTwoTable.table[i]);
				2.collect{|l|
					data.data_ampTwo_maxMin[i][l].connect(ampTwoTable.minMaxValues[i][l]);
				};
			};

			//amp two table editor
			ampTwoTable_Editor = NuPG_GUI_Table_Editor_View.new;
			ampTwoTable_Editor.defaultTablePath = tablesPath;
			ampTwoTable_Editor.draw("_amp_Two editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				ampTwoTable_Editor.data[i] = data.data_ampTwo[i];
				data.data_ampTwo[i].connect(ampTwoTable_Editor.table[i]);
				2.collect{|l|
					data.data_ampTwo_maxMin[i][l].connect(ampTwoTable_Editor.minMaxValues[i][l]);
				};
			};
			ampTwoTable.editorView = ampTwoTable_Editor;
			ampTwoTable_Editor.parentView = ampTwoTable;

			//amp three
			ampThreeTable = NuPG_GUI_Table_View.new;
			ampThreeTable.defaultTablePath = tablesPath;
			ampThreeTable.draw("_amp_Three", guiDefinitions.ampThreeViewDimensions, n: instances);
			//mapping
			instances.collect{|i|
				//gui <-> data
				ampThreeTable.data[i] = data.data_ampThree[i];
				data.data_ampThree[i].connect(ampThreeTable.table[i]);
				2.collect{|l|
					data.data_ampThree_maxMin[i][l].connect(ampThreeTable.minMaxValues[i][l]);
				};
			};

			//amp three table editor
			ampThreeTable_Editor = NuPG_GUI_Table_Editor_View.new;
			ampThreeTable_Editor.defaultTablePath = tablesPath;
			ampThreeTable_Editor.draw("_amp_Three editor", guiDefinitions.tableEditorViewDimensions, n: instances);
			instances.collect{|i|
				//gui <-> data
				ampThreeTable_Editor.data[i] = data.data_ampThree[i];
				data.data_ampThree[i].connect(ampThreeTable_Editor.table[i]);
				2.collect{|l|
					data.data_ampThree_maxMin[i][l].connect(ampThreeTable_Editor.minMaxValues[i][l]);
				};
			};
			ampThreeTable.editorView = ampThreeTable_Editor;
			ampThreeTable_Editor.parentView = ampThreeTable;



			// Record view initialization
			record = NuPG_GUI_Record_View.new;
			record.draw(guiDefinitions.recordViewDimensions, n: instances);

			// Groups control view initialization
			groupsControl = NuPG_GUI_GroupControl_View.new;
			groupsControl.draw(guiDefinitions.groupsControlViewDimensions, synthesis, n: instances);

			// Scrubber view initialization
			scrubber = NuPG_GUI_ScrubberView.new;
			scrubber.draw(guiDefinitions.scrubberViewDimensions, data: data, tasks: scrubbTask, synthesis: synthesis, n: instances);
			scrubber.path = filesPath;
			instances.collect { |i|
				data.data_scrubber[i].connect(scrubber.trainProgress[i]);
				data.data_scrubber[i].connect(scrubber.progresDisplay[i]);
			};
			scrubber.sliderRecordTask = scrubbRecordTask;
			scrubber.sliderPlaybackTask = scrubbPlaybackTask;

			// Single shot task
			singleShotTask = NuPG_LoopTask.new;
			singleShotTask.loadSingleshot(data: data, synthesis: synthesis, progressSlider: progressSlider, n: instances);

			// Train control view initialization
			trainControl = NuPG_GUI_TrainControl_View.new;
			trainControl.draw(guiDefinitions.trainControlViewDimensions, loopTask, singleShotTask, scrubber, synthesis, progressSlider, n: instances);

			// Progress slider tasks setup
			progressSlider = NuPG_ProgressSliderPlay.new;
			progressSlider.load(data, trainControl, n: instances);
			instances.collect { |i|
				data.data_trainDuration[i].connect(trainControl.trainDuration[i]);
				trainControl.scrubbTask[i] = scrubbTask.tasks[i];
				trainControl.progresTask[i] = progressSlider.tasks[i];
				progressSlider.tasks[i].set(\progressDirection, 0);
			};

			// Preset view initialization
			presets = NuPG_GUI_Presets_View.new;
			presets.defaultPresetPath = presetsPath;
			presets.draw("_presets", guiDefinitions.presetsViewDimensions, n: instances);
			presets.data = data;
			instances.collect { |i|
				data.conductor[(\con_ ++ i).asSymbol].preset.presetCV.connect(presets.currentPreset[i]);
				data.conductor[(\con_ ++ i).asSymbol].preset.presetCV.connect(presets.interpolationFromPreset[i]);
				data.conductor[(\con_ ++ i).asSymbol].preset.targetCV.connect(presets.interpolationToPreset[i]);
				data.conductor[(\con_ ++ i).asSymbol].preset.interpCV.connect(presets.presetInterpolationSlider[i]);
				presets.pulsaretBuffers[i] = pulsaret_buffers[i];
				presets.envelopeBuffers[i] = envelope_buffers[i];

				presets.interpolationFromPreset[i].keyDownAction_({ arg view, char, modifiers, unicode, keycode;
					if (keycode == 36, {
						pulsaret_buffers[i].sendCollection(data.data_pulsaret[i].value);
						envelope_buffers[i].sendCollection(data.data_envelope[i].value);
					}, { });
					if (keycode == 76, {
						pulsaret_buffers[i].sendCollection(data.data_pulsaret[i].value);
						envelope_buffers[i].sendCollection(data.data_envelope[i].value);
					}, { });
				});
				presets.presetInterpolationSlider[i].mouseUpAction_ {
					pulsaret_buffers[i].sendCollection(data.data_pulsaret[i].value);
					envelope_buffers[i].sendCollection(data.data_envelope[i].value);
				};
			};

			// Fourier view initialization
			fourier = NuPG_GUI_Fourier_View.new;
			fourier.draw("_fourier", guiDefinitions.fourierViewDimensions, n: instances);
			instances.collect { |i|
				fourier.data[i] = data.data_fourier[i];
			};

			// Masking view initialization
			masking = NuPG_GUI_Masking.new;
			masking.draw("_masking", guiDefinitions.maskingControlDimensions, n: instances);
			instances.collect { |i|
				data.data_probabilityMaskSingular[i].connect(masking.probability[i]);
				2.collect { |l| data.data_burstMask[i][l].connect(masking.burtsRest[i][l]) };
				data.data_channelMask[i][0].connect(masking.channel[i][0]);
			};

			// Sieves view initialization
			sieves = NuPG_GUI_Sieves.new;
			sieves.path = filesPath;
			sieves.draw("_sieves", guiDefinitions.sieveViewDimensions, synthesis: synthesis, n: instances);
			instances.collect { |i| 2.collect { |l| sieves.data[i][l] = data.data_sieveMask[i][l] } };

			// Modulators views initialization
			modulator1 = NuPG_GUI_ModulatorsView.new;
			modulator1.draw("_m1", guiDefinitions.modulatorOneViewDimensions, n: instances);
			instances.collect { |i|
				data.data_modulator1[i][1].connect(modulator1.modFreq[i]);
				data.data_modulator1[i][2].connect(modulator1.modDepth[i]);
				modulator1.modType[i].action_({ |m| synthesis.trainInstances[i].set(\modulator_type_one, m.value) });
			};

			modulator2 = NuPG_GUI_ModulatorsView.new;
			modulator2.draw("_m2", guiDefinitions.modulatorOneViewDimensions.moveBy(0, -105), n: instances);
			instances.collect { |i|
				data.data_modulator2[i][1].connect(modulator2.modFreq[i]);
				data.data_modulator2[i][2].connect(modulator2.modDepth[i]);
				modulator2.modType[i].action_({ |m| synthesis.trainInstances[i].set(\modulator_type_two, m.value) });
			};

			modulator3 = NuPG_GUI_ModulatorsView.new;
			modulator3.draw("_m3", guiDefinitions.modulatorOneViewDimensions.moveBy(0, -210), n: instances);
			instances.collect { |i|
				data.data_modulator3[i][1].connect(modulator3.modFreq[i]);
				data.data_modulator3[i][2].connect(modulator3.modDepth[i]);
				modulator3.modType[i].action_({ |m| synthesis.trainInstances[i].set(\modulator_type_three, m.value) });
			};

			modulator4 = NuPG_GUI_ModulatorsView.new;
			modulator4.draw("_m4", guiDefinitions.modulatorOneViewDimensions.moveBy(0, -315), n: instances);
			instances.collect { |i|
				data.data_modulator4[i][1].connect(modulator4.modFreq[i]);
				data.data_modulator4[i][2].connect(modulator4.modDepth[i]);
				modulator4.modType[i].action_({ |m| synthesis.trainInstances[i].set(\modulator_type_four, m.value) });
			};

			matrixMod = NuPG_GUI_ModMatrix.new;
			matrixMod.draw("_modulation matrix", guiDefinitions.modMatrixViewDimensions, [modulator1, modulator2, modulator3, modulator4], n: instances);
			instances.collect { |i|
				4.collect { |k| 13.collect { |l| data.data_matrix[i][k][l].connect(matrixMod.matrix[i][k][l]) } };
			};

			// Extensions view
			extensions = NuPG_GUI_Extensions_View.new;
			extensions.draw(guiDefinitions.extensionsViewDimensions, viewsList: [modulators, fourier, masking, sieves, groupsOffest, matrixMod], n: instances);

			// Control view
			control = NuPG_GUI_Control_View.new;
			control.draw(guiDefinitions.controlViewDimensions, viewsList: [
				pulsaretTable, envelopeTable, main, maskingTable, fundamentalTable,
				formantOneTable, formantTwoTable, formantThreeTable, envelopeMult_One,
				envelopeMult_Two, envelopeMult_Three, panOneTable, panTwoTable, panThreeTable,
				ampOneTable, ampTwoTable, ampThreeTable, groupsControl, trainControl, fourier,
				sieves, masking, modulators, pulsaretTableEditor, envelopeTableEditor,
				probabilityTableEditor, fundamentalTableEditor, formantOneTableEditor, formantTwoTableEditor, formantThreeTableEditor,
				envelopeMult_One_Editor, envelopeMult_Two_Editor, envelopeMult_Three_Editor,
				panOneTable_Editor, panTwoTable_Editor, panThreeTable_Editor,
				ampOneTable_Editor, ampTwoTable_Editor, ampThreeTable_Editor, presets,
				modulationTable, modulationTableEditor, modulationRatioTable, modulationRatioEditor,
				multiparameterModulationTable, multiparameterModulationTableEditor,
				groupsOffest, matrixMod, modulator1, modulator2, modulator3, modulator4
			], n: instances);

		}).doWhenBooted({
	instances.collect{|i|
		synthesis.trainInstances[i].set(\pulsaret_buffer, pulsaret_buffers[i].bufnum);
		synthesis.trainInstances[i].set(\envelope_buffer, envelope_buffers[i].bufnum);
		synthesis.trainInstances[i].set(\frequency_buffer, frequency_buffers[i].bufnum);

		synthesis.trainInstances[i].setControls([
			fundamental_frequency: data.data_main[i][0],
			formant_frequency_One: data.data_main[i][1],
			formant_frequency_Two: data.data_main[i][2],
			formant_frequency_Three: data.data_main[i][3],
			envMul_One: data.data_main[i][4],
			envMul_Two: data.data_main[i][5],
			envMul_Three: data.data_main[i][6],
			pan_One: data.data_main[i][7],
			pan_Two: data.data_main[i][8],
			pan_Three: data.data_main[i][9],
			amplitude_One: data.data_main[i][10],
			amplitude_Two: data.data_main[i][11],
			amplitude_Three: data.data_main[i][12],
			fmAmt: data.data_modulators[i][0],
			fmRatio: data.data_modulators[i][1],
			allFluxAmt: data.data_modulators[i][2],
			burst: data.data_burstMask[i][0],
			rest: data.data_burstMask[i][1],
			chanMask: data.data_channelMask[i][0],
			centerMask: data.data_channelMask[i][1],
			sieveMod: data.data_sieveMask[i][0],
			sieveSequence: data.data_sieveMask[i][1],
			probability: data.data_probabilityMaskSingular[i],
			offset_1: data.data_groupsOffset[i][0],
			offset_2: data.data_groupsOffset[i][1],
			offset_3: data.data_groupsOffset[i][2]
		]);
};})
	}

}

//test
//a = NuPG_StartUp.new;
//a.start;



