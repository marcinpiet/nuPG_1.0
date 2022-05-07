//nuPG preset sytem, add, remove, save, read prestets of settings

NuPG_Preset {

	var <>presets;
	var <>presetTrainOne, <>presetTrainTwo, <>presetTrainThree;
	var <>presetPulsaretOne, <>presetPulsaretTwo, <>presetPulsaretThree;
	var <>presetEnvelopeOne, <>presetEnvelopeTwo, <>presetEnvelopeThree;
	var <>presetFrequencyOne, <>presetFrequencyTwo, <>presetFrequencyThree;
	var <>dataFromFile;

	//initialise a preset as list with maximum presets set to 1000
	initPresets {
		//global preset
		presets = List.new(1000);
		//presets for each train
		presetTrainOne = List.new(1000);
		presetTrainTwo = List.new(1000);
		presetTrainThree = List.new(1000);
		//pulsaret preset
		presetPulsaretOne = List.new(1000);
		presetPulsaretTwo = List.new(1000);
		presetPulsaretThree = List.new(1000);
		//envelope preset
		presetEnvelopeOne = List.new(1000);
		presetEnvelopeTwo = List.new(1000);
		presetEnvelopeThree = List.new(1000);
		//frequency preset
		presetFrequencyOne = List.new(1000);
		presetFrequencyTwo = List.new(1000);
		presetFrequencyThree = List.new(1000);
	}


	addTrainPreset {|train, data|

        //reformat data

		var dataFromParams = [
			//tables
			data.data_tables[train].size.collect{|i| data.data_tables[train][i].value },
			//main
			data.data_main[train].size.collect{|i| data.data_main[train][i].value },
			//micro seq
			data.data_microSequencer[train].size.collect{|i| data.data_microSequencer[train][i].value },
			//ppModulation
			data.data_ppModulation[train].size.collect{|i| data.data_ppModulation[train][i].value },
			//matrix
			data.data_wfModulation_matrix[train].size.collect{|i|
				data.data_wfModulation_matrix[train][i].size.collect{|l| data.data_wfModulation_matrix[train][i][l].value }
			},
			//offset
			data.data_wfModulation_matrix_offset[train].size.collect{|i|
				data.data_wfModulation_matrix_offset[train][i].size.collect{|l| data.data_wfModulation_matrix_offset[train][i][l].value}
			},
			//polarity
			data.data_wfModulation_matrix_polarity[train].size.collect{|i|
				data.data_wfModulation_matrix_polarity[train][i].size.collect{|l|
					data.data_wfModulation_matrix_polarity[train][i][l].value}
			},
			//modulators
			data.data_modulators[train].size.collect{|i|
				data.data_modulators[train][i].size.collect{|l|
					data.data_modulators[train][i][l].value}
			},
			//modulatorType
			data.data_modulatorType[train].size.collect{|i|
				data.data_modulatorType[train][i].size.collect{|l|
					data.data_modulatorType[train][i][l].value}
			},

			//modulation wavefold lo and hi bondaries
			data.data_modulation_wf_ranges[train].size.collect{|i|
				data.data_modulation_wf_ranges[train][i].size.collect{|l|
					data.data_modulation_wf_ranges[train][i][l].value}
			}
		];

		var newPreset = case
		{train == 0} {presetTrainOne.add(dataFromParams)}
		{train == 1} {presetTrainTwo.add(dataFromParams)}
		{train == 2} {presetTrainThree.add(dataFromParams)};

		data.data_preset_trains[train].value = newPreset.size - 1;

		("New Train Preset Added. Total Number of Presets: " ++ newPreset.size).postln;
	}

	removeTrainPreset {|train, data|

		var removePreset = case
		{train == 0} {
			presetTrainOne.removeAt(data.data_preset_trains[train].value);
			data.data_preset_trains[train].value = data.data_preset_trains[train].value -1
		}
		{train == 1} {
			presetTrainTwo.removeAt(data.data_preset_trains[train].value);
			data.data_preset_trains[train].value = data.data_preset_trains[train].value -1
		}
		{train == 2} {
			presetTrainThree.removeAt(data.data_preset_trains[train].value);
			data.data_preset_trains[train].value = data.data_preset_trains[train].value -1
		};
	}

	recallTrainPreset {|train, data, presetNum|

		var preset = case
		{train == 0} {presetTrainOne}
		{train == 1} {presetTrainTwo}
		{train == 2} {presetTrainThree};

		//check if there is enough presets
		if(
			presetNum <= (preset.size - 1),
            //if preset number exist recall
            {
				//recall table preset
				data.data_tables[train].size.collect{|i| data.data_tables[train][i].value = preset[presetNum][0][i]  };

				//recall main preset
				data.data_main[train].size.collect{|i| data.data_main[train][i].value =  preset[presetNum][1][i]};

				//recall micro sequencer
                data.data_microSequencer[train].size.collect{|i| data.data_microSequencer[train][i].value =  preset[presetNum][2][i]};

				//recall ppModulation
				data.data_ppModulation[train].size.collect{|i| data.data_ppModulation[train][i].value =  preset[presetNum][3][i]};

				//recall matrix
                data.data_wfModulation_matrix[train].size.collect{|i|
					data.data_wfModulation_matrix[train][i].size.collect{|l|
						data.data_wfModulation_matrix[train][i][l].value =  preset[presetNum][4][i][l]}
				};

				//recall offset
                data.data_wfModulation_matrix_offset[train].size.collect{|i|
					data.data_wfModulation_matrix_offset[train][i].size.collect{|l|
						data.data_wfModulation_matrix_offset[train][i][l].value =  preset[presetNum][5][i][l]}
				};

				//recall polarity
                data.data_wfModulation_matrix_polarity[train].size.collect{|i|
					data.data_wfModulation_matrix_polarity[train][i].size.collect{|l|
						data.data_wfModulation_matrix_polarity[train][i][l].value =  preset[presetNum][6][i][l]}
				};

				//recall modulators
				 data.data_modulators[train].size.collect{|i|
					data.data_modulators[train][i].size.collect{|l|
						data.data_modulators[train][i][l].value =  preset[presetNum][7][i][l]}
				};

				//recall modulatorType
				 data.data_modulatorType[train].size.collect{|i|
					data.data_modulatorType[train][i].size.collect{|l|
						data.data_modulatorType[train][i][l].value =  preset[presetNum][8][i][l]}
				};

				//recall modulation wf ranges
				 data.data_modulation_wf_ranges[train].size.collect{|i|
					data.data_modulation_wf_ranges[train][i].size.collect{|l|
						data.data_modulation_wf_ranges[train][i][l].value =  preset[presetNum][9][i][l]}
				};

				//update current preset number
				data.data_preset_trains[train].value = presetNum;
			},
            //if preset doesn't exist display error message
			{("PRESETS SIZE IS " ++ (preset.size - 1) ++ " :CHOOSE SMALLER VALUE").postln}
		)
	}

	//save presets to a file
	saveTrainPresets {|train = 0|

		var preset = case
		{train == 0} {presetTrainOne}
		{train == 1} {presetTrainTwo}
		{train == 2} {presetTrainThree};

		Dialog.savePanel({|path|
			preset.writeArchive(path)
		},
		{"canceled".postln} )
	}
	//open file with presets
    openTrainPresets {|train = 0, data|
		var preset = case
		{train == 0} {
			Dialog.openPanel(
			{|path|
				dataFromFile = Object.readArchive(path);
				data.data_preset_trains[train].value = dataFromFile.size - 1;
				presetTrainOne = dataFromFile;
					this.recallTrainPreset(train, data, data.data_preset_trains[train].value);
			},
			{"canceled".postln});

		}
		{train == 1} {
			Dialog.openPanel(
			{|path|
				dataFromFile = Object.readArchive(path);
				data.data_preset_trains[train].value = dataFromFile.size - 1;
				presetTrainTwo = dataFromFile;
					this.recallTrainPreset(train, data, data.data_preset_trains[train].value);
			},
			{"canceled".postln});

		}
		{train == 2} {
			Dialog.openPanel(
			{|path|
				dataFromFile = Object.readArchive(path);
				data.data_preset_trains[train].value = dataFromFile.size - 1;
				presetTrainThree = dataFromFile;
					this.recallTrainPreset(train, data, data.data_preset_trains[train].value);
			},
			{"canceled".postln});

		};

		preset;
	}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//PULSARET PRESET
	addPulsaretPreset {|train, data|

        //reformat data

		var dataFromPulsaret = data.data_pulsaret[train].value;

		var newPreset = case
		{train == 0} {presetPulsaretOne.add(dataFromPulsaret)}
		{train == 1} {presetPulsaretTwo.add(dataFromPulsaret)}
		{train == 2} {presetPulsaretThree.add(dataFromPulsaret)};

		data.data_preset_pulsaret[train].value = newPreset.size - 1;

		("New Pulsaret Preset Added. Total Number of Presets: " ++ newPreset.size).postln;
	}

	removePulsaretPreset {|train, data|

		var removePreset = case
		{train == 0} {
			presetPulsaretOne.removeAt(data.data_preset_pulsaret[train].value );
			data.data_preset_pulsaret[train].value = data.data_preset_pulsaret[train].value -1}
		{train == 1} {
			presetPulsaretTwo.removeAt(data.data_preset_pulsaret[train].value );
			data.data_preset_pulsaret[train].value = data.data_preset_pulsaret[train].value -1
		}
		{train == 2} {
			presetPulsaretThree.removeAt(data.data_preset_pulsaret[train].value );
			data.data_preset_pulsaret[train].value = data.data_preset_pulsaret[train].value -1
		};
	}

	recallPulsaretPreset {|train, data, presetNum|

		var preset = case
		{train == 0} {presetPulsaretOne}
		{train == 1} {presetPulsaretTwo}
		{train == 2} {presetPulsaretThree};

		//check if there is enough presets
		if(
			presetNum <= (preset.size - 1),
            //if preset number exist recall
            {
				//recall table preset
				data.data_pulsaret[train].value = preset[presetNum];

				data.data_preset_pulsaret[train].value = presetNum;
			},
            //if preset doesn't exist display error message
			{("PRESETS SIZE IS " ++ (preset.size - 1).asString ++ " :CHOOSE SMALLER VALUE").postln}
		)
	}

	//save pulsaret presets to a file
	savePulsaretPresets {|train|

		var preset = case
		{train == 0} {presetPulsaretOne}
		{train == 1} {presetPulsaretTwo}
		{train == 2} {presetPulsaretThree};

		Dialog.savePanel({|path| preset.writeArchive(path); preset.postln}, {"canceled".postln} )
	}
	//open pulsaret file with presets
    openPulsaretPresets {|train = 0, data|
		var preset = case
		{train == 0} {
			Dialog.openPanel(
			{|path|
				dataFromFile = Object.readArchive(path);
					data.data_preset_pulsaret[train].value = dataFromFile.size - 1;
				presetPulsaretOne = dataFromFile;
					this.recallPulsaretPreset(train, data, data.data_preset_pulsaret[train].value);
			},
			{"canceled".postln});

		}
		{train == 1} {
			Dialog.openPanel(
			{|path|
				dataFromFile = Object.readArchive(path);
					data.data_preset_pulsaret[train].value = dataFromFile.size - 1;
				presetPulsaretTwo = dataFromFile;
					this.recallPulsaretPreset(train, data, data.data_preset_pulsaret[train].value);
			},
			{"canceled".postln});

		}
		{train == 2} {
			Dialog.openPanel(
			{|path|
				dataFromFile = Object.readArchive(path);
					data.data_preset_pulsaret[train].value = dataFromFile.size - 1;
				presetPulsaretThree = dataFromFile;
					this.recallPulsaretPreset(train, data, data.data_preset_pulsaret[train].value);
			},
			{"canceled".postln});

		};

		preset;
	}
	///////////////////////////////////////////////////////////////////////////////////////
	//ENVELOPE PRESETS
	addEnvelopePreset{|train, data|

        //reformat data

		var dataFromEnvelope = data.data_envelope[train].value;

		var newPreset = case
		{train == 0} {presetEnvelopeOne.add(dataFromEnvelope)}
		{train == 1} {presetEnvelopeTwo.add(dataFromEnvelope)}
		{train == 2} {presetEnvelopeThree.add(dataFromEnvelope)};

		data.data_preset_envelope[train].value = newPreset.size - 1;

		("New Envelope Preset Added. Total Number of Presets: " ++ newPreset.size).postln;
	}

	removeEnvelopePreset {|train, data|

		var removePreset = case
		{train == 0} {
			presetEnvelopeOne.removeAt(data.data_preset_envelope[train].value );
			data.data_preset_envelope[train].value = data.data_preset_envelope[train].value -1}
		{train == 1} {
			presetEnvelopeTwo.removeAt(data.data_preset_envelope[train].value );
			data.data_preset_envelope[train].value = data.data_preset_envelope[train].value -1
		}
		{train == 2} {
			presetEnvelopeThree.removeAt(data.data_preset_envelope[train].value );
			data.data_preset_envelope[train].value = data.data_preset_envelope[train].value -1
		};
	}

	recallEnvelopePreset {|train, data, presetNum|

		var preset = case
		{train == 0} {presetEnvelopeOne}
		{train == 1} {presetEnvelopeTwo}
		{train == 2} {presetEnvelopeThree};

		//check if there is enough presets
		if(
			presetNum <= (preset.size - 1),
            //if preset number exist recall
            {
				//recall table preset
				data.data_envelope[train].value = preset[presetNum];

				data.data_envelope[train].value = presetNum;
			},
            //if preset doesn't exist display error message
			{("PRESETS SIZE IS " ++ (preset.size - 1).asString ++ " :CHOOSE SMALLER VALUE").postln}
		)
	}

	//save pulsaret presets to a file
	saveEnvelopePresets {|train|

		var preset = case
		{train == 0} {presetEnvelopeOne}
		{train == 1} {presetEnvelopeTwo}
		{train == 2} {presetEnvelopeThree};

		Dialog.savePanel({|path| preset.writeArchive(path); preset.postln}, {"canceled".postln} )
	}
	//open pulsaret file with presets
    openEnvelopePresets {|train = 0, data|
		var preset = case
		{train == 0} {
			Dialog.openPanel(
			{|path|
				dataFromFile = Object.readArchive(path);
					data.data_preset_envelope[train].value = dataFromFile.size - 1;
				presetPulsaretOne = dataFromFile;
					this.recallEnvelopePreset(train, data, data.data_preset_pulsaret[train].value);
			},
			{"canceled".postln});

		}
		{train == 1} {
			Dialog.openPanel(
			{|path|
				dataFromFile = Object.readArchive(path);
					data.data_preset_envelope[train].value = dataFromFile.size - 1;
				presetEnvelopeTwo = dataFromFile;
					this.recallEnvelopePreset(train, data, data.data_preset_envelope[train].value);
			},
			{"canceled".postln});

		}
		{train == 2} {
			Dialog.openPanel(
			{|path|
				dataFromFile = Object.readArchive(path);
					data.data_preset_envelope[train].value = dataFromFile.size - 1;
				presetEnvelopeThree = dataFromFile;
					this.recallEnvelopePreset(train, data, data.data_preset_envelope[train].value);
			},
			{"canceled".postln});

		};

		preset;
	}

	///////////////////////////////////////////////////////////////////////////////////////
	//FREQUENCY PRESETS
	addFrequencyPreset{|train, data|

        //reformat data

		var dataFromFrequency = data.data_frequency[train].value;

		var newPreset = case
		{train == 0} {presetFrequencyOne.add(dataFromFrequency)}
		{train == 1} {presetFrequencyTwo.add(dataFromFrequency)}
		{train == 2} {presetFrequencyThree.add(dataFromFrequency)};

		data.data_preset_frequency[train].value = newPreset.size - 1;

		("New Frequency Table Preset Added. Total Number of Presets: " ++ newPreset.size).postln;
	}

	removeFrequencyPreset {|train, data|

		var removePreset = case
		{train == 0} {
			presetFrequencyOne.removeAt(data.data_preset_frequency[train].value);
			data.data_preset_frequency[train].value = data.data_preset_frequency[train].value -1
		}
		{train == 1} {
			presetFrequencyTwo.removeAt(data.data_preset_frequency[train].value);
			data.data_preset_frequency[train].value = data.data_preset_frequency[train].value -1
		}
		{train == 2} {
			presetFrequencyThree.removeAt(data.data_preset_frequency[train].value);
			data.data_preset_frequency[train].value = data.data_preset_frequency[train].value -1
		};
	}

	recallFrequencyPreset {|train, data, presetNum|

		var preset = case
		{train == 0} {presetFrequencyOne}
		{train == 1} {presetFrequencyTwo}
		{train == 2} {presetFrequencyThree};

		//check if there is enough presets
		if(
			presetNum <= (preset.size - 1),
            //if preset number exist recall
            {
				//recall table preset
				data.data_frequency[train].value = preset[presetNum];

				data.data_frequency[train].value = presetNum;
			},
            //if preset doesn't exist display error message
			{("PRESETS SIZE IS " ++ (preset.size - 1).asString ++ " :CHOOSE SMALLER VALUE").postln}
		)
	}

	//save pulsaret presets to a file
	saveFrequencyPresets {|train|

		var preset = case
		{train == 0} {presetFrequencyOne}
		{train == 1} {presetFrequencyTwo}
		{train == 2} {presetFrequencyThree};

		Dialog.savePanel({|path| preset.writeArchive(path); preset.postln}, {"canceled".postln} )
	}
	//open pulsaret file with presets
    openFrequencyPresets {|train = 0, data|
		var preset = case
		{train == 0} {
			Dialog.openPanel(
			{|path|
				dataFromFile = Object.readArchive(path);
					data.data_preset_frequency[train].value = dataFromFile.size - 1;
				presetFrequencyOne = dataFromFile;
					this.recallFrequencyPreset(train, data, data.data_preset_frequency[train].value);
			},
			{"canceled".postln});

		}
		{train == 1} {
			Dialog.openPanel(
			{|path|
				dataFromFile = Object.readArchive(path);
					data.data_preset_frequency[train].value = dataFromFile.size - 1;
				presetFrequencyTwo = dataFromFile;
					this.recallFrequencyPreset(train, data, data.data_preset_frequency[train].value);
			},
			{"canceled".postln});

		}
		{train == 2} {
			Dialog.openPanel(
			{|path|
				dataFromFile = Object.readArchive(path);
					data.data_preset_frequency[train].value = dataFromFile.size - 1;
				presetFrequencyThree = dataFromFile;
					this.recallFrequencyPreset(train, data, data.data_preset_frequency[train].value);
			},
			{"canceled".postln});

		};

		preset;
	}

    ///////////////////////////////////////////////////////////////////////////////////////
	//PRESET INTERPOLATION
	interpolateFunction {|from, to|

		var intepolation = Array.interpolation(200, from, to);

		^intepolation;
	}

	interpolatePulsaret {|train, sourcePreset, targetPreset|
		var preset = case
		{train == 0} {presetPulsaretOne}
		{train == 1} {presetPulsaretTwo}
		{train == 2} {presetPulsaretThree};
	}

	}