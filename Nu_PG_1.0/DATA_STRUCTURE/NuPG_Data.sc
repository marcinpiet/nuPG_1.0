//nuPg data structure based on Connection Quark and NumericControl Class
//
NuPG_Data {

	var <>data_main;
	var <>data_ppModulation;
	var <>data_modulators, <>data_modulatorType;
	var <>data_modulation_wf_ranges;
	var <>data_wfModulation_matrix_offset, <>data_wfModulation_matrix_polarity, <>data_wfModulation_matrix;
	var <>data_microSequencer, <>data_microSequencerRanges, <>data_microSpeed;
	var <>data_mesoSequencer, <>data_mesoSequencerRanges, <>data_mesoSpeed;
	var <>data_pulsaret;
	var <>data_envelope;
	var <>data_frequency;
	var <>data_tables;
	var <>data_pulsaret_shaper;
	var <>data_masking, <>data_sieveMasking;
	var <>data_parameterLink;
	//data presets
	var <>data_preset_global, <>data_preset_trains;
	var <>data_preset_pulsaret, <>data_preset_envelope, <>data_preset_frequency;
	var <>data_preset_train_interpolation;
	var <>data_preset_pulsaret_interpolation,  <>data_preset_envelope_interpolation, <>data_preset_frequency_interpolation;
	//data control
	var <>data_trains;

	trains {
		^data_trains = 3.collect{
			3.collect{
				NumericControlValue(spec: ControlSpec(0, 1, \lin, 1, 0))
		}
		}
	}

	main {
		//main parameters specs
		var specs =

		3.collect{
			var min = [1.0, 0.1, 0.0, -1.0, 0.0];
			var max = [3000.0, 16.0, 4.0, 1.0, 1.0];
			var warp = [\exp, \exp, \lin, \lin, \lin];
			var defVal = [10.0, 10.0, 1.0 ,0.0,0.5];

			5.collect{|l|
				ControlSpec(min[l], max[l], warp[l], 0.01, defVal[l])
			};
		};

		//load them into data_main variable
		^data_main = 3.collect{|i|
			5.collect{|l|
				NumericControlValue(spec: specs[i][l])
			}
		}
	}

	parameterLink { ^data_parameterLink = 6.collect{|l| NumericControlValue(spec: ControlSpec(-50.0, 50, \lin, 0.1, 0) )} }

	masking {

		var specs =

		3.collect{
			var min = [0.0, 1, 0, 0, 0];
			var max = [1.0, 2999, 2998, 1500, 1];
			var warp = [\lin, \lin, \lin, \lin, \lin];
			var step = [0.01, 1, 1, 1, 1];
			var defVal = [1.0, 1, 0 ,0, 1];

			5.collect{|l|
				ControlSpec(min[l], max[l], warp[l], step[l], defVal[l])
			};
		};

		^data_masking = 3.collect{|i|
			5.collect{|l|
				NumericControlValue(spec: specs[i][l])
			}
		}

	}

	sieveMasking {
		^data_sieveMasking = 3.collect{ [
			NumericControlValue(spec: ControlSpec(0, 1, \lin, 1, 0)),
			NumericControlValue(spec: ControlSpec(1, 16, \lin, 1, 1)),
			NumericControlValue((0..15))
		] }
	}

	modulation_wf_ranges {
		//parameters specs
		//range - low and high
		var specs =

		3.collect{
			4.collect{|k|
			var min = [0.0, 0.0];
			var max = [1.0, 1.0];
			var warp = [\lin, \lin];
			var defVal = [0.0, 0.0];
			var step = [0.01, 0.01];


			2.collect{|l|
					ControlSpec(min[l], max[l], warp[l], step[l], defVal[l])
			};
			}
		};
			//load them into data variable
		^data_modulation_wf_ranges = 3.collect{|i|
			4.collect{|k|
			2.collect{|l|
					NumericControlValue(spec: specs[i][k][l])
			}
			}
		}
	}

	wfModulation_matrix {
		//parameters specs
		//rmatrix of on-off buttons
		var specs =

		3.collect{  7.collect{ 4.collect{ ControlSpec(0, 1, \lin, 1, 0) } }};
			//load them into data variable
		^data_wfModulation_matrix = 3.collect{|i|
			7.collect{|k|
			4.collect{|l|
					NumericControlValue(spec: specs[i][k][l])
			}
			}
		}
	}

	wfModulation_matrix_offset {
		//parameters specs
		//rmatrix of on-off buttons
		var specs =

		3.collect{
			5.collect{|l|
				var limit = [100, 5, 5, 5, 5];
				var def = [25, 1, 1, 1, 1];
				ControlSpec(0, limit[l], \lin, 0.1, def[l])

		}};
			//load them into data variable
		^data_wfModulation_matrix_offset = 3.collect{|i|
			5.collect{|k|
				[ NumericControlValue(spec: specs[i][k]) ]
			}
		}
	}

	wfModulation_matrix_polarity {
		//parameters specs
		//rmatrix of on-off buttons
		var specs =

		3.collect{
			7.collect{|l|
				var def = [1, 1, 1, 1, 1, 1, 0];
				ControlSpec(0, 1, \lin, 1, def[l])

		}};
			//load them into data variable
		^data_wfModulation_matrix_polarity = 3.collect{|i|
			7.collect{|k|
				[ NumericControlValue(spec: specs[i][k]) ]
			}
		}
	}

	modulators {
		//parameters specs
		//mod freq, mod index
		var specs =

		3.collect{
			4.collect{


			2.collect{|l|
					var min = [0.01, 1];
			        var max = [3500.0, 10];
			        var warp = [\exp, \lin];
			        var defVal = [3.0, 1];
					var step = [0.01,1];
					ControlSpec(min[l], max[l], warp[l], step[l], defVal[l])
			};
			}
		};

		//load them into data variable
		^data_modulators = 3.collect{|i|
			4.collect{|k|
			2.collect{|l|
					NumericControlValue(spec: specs[i][k][l])
			}
			}
		}

	}

	modulatorType {
		//parameters specs
		//mod freq, mod index
		var specs =

		3.collect{
			4.collect{
				ControlSpec(0, 4, \lin, 1, 0)
			}
		};

		//load them into data variable
		^data_modulatorType = 3.collect{|i|
			4.collect{|k|
				[NumericControlValue(spec: specs[i][k])]

			}
		}

	}


	ppModulation {
		//parameters specs
		var specs =

		3.collect{
			var min = [0.0, 0.0, 0.0];
			var max = [16.0, 16.0, 8.0];
			var warp = [\lin, \lin, \lin];
			var defVal = [0.0, 0.0, 0.0];

			3.collect{|l|
				ControlSpec(min[l], max[l], warp[l], 0.01, defVal[l])
			};
		};

		//load them into data variable
		^data_ppModulation = 3.collect{|i|
			3.collect{|l|
				NumericControlValue(spec: specs[i][l])
			}
		}

	}

	//micro and meso sequencers
	//2048 data points - changed in relation to PG's table size
	microSequencer {
		^data_microSequencer = 3.collect{
			5.collect{
				NumericControlValue((0..2047)/2048)
			}
		}

	}
	//microSequencer Ranges
	microSequencerRanges {

		var specs =

		3.collect{
				var ranges = [
								[0.0, 10.0], //trigger frequency
								[0.1, 20.0], //grain frequency
								[0.1, 2.0], //envelope multiplicator
								[-1.0,1.0], //panning
								[0.0,2.0], //amplitude

							];

			5.collect{|i|
				2.collect{|l|
					ControlSpec(ranges[i][0], ranges[i][1], default: ranges[i][l])
				}
			};
		};


		^data_microSequencerRanges = 3.collect{|i|
			5.collect{|l|
				2.collect{|k|
					NumericControlValue(spec: specs[i][l][k])
				}
			}
		}

	}

	microSpeed {
		^data_microSpeed = 3.collect{
			NumericControlValue(spec: ControlSpec(75, 5000, 'lin', 0.01))
		}
	}

	mesoSequencer {
		^data_mesoSequencer = 3.collect{
			5.collect{
				NumericControlValue((0..2047)/2048)
			}
		}
	}

	//tables - pulsaret, envelope, frequency
	//2048 data points
	tables { ^data_tables = 3.collect{
		3.collect{|i| NumericControlValue((0..2047)/2048) }
	}
	}

	pulsaret { ^data_pulsaret = 3.collect{|i| data_tables[i][0] } }
	envelope { ^data_envelope = 3.collect{|i| data_tables[i][1] }}
	frequency { ^data_frequency = 3.collect{|i| data_tables[i][2] } }

	pulsaretShaper { ^data_pulsaret_shaper = 3.collect{NumericControlValue((0..15)/16)} }

	//presets
	presets {
		var allPresetData;
		var size = 199;

		//global preset
		data_preset_global = NumericControlValue(spec: ControlSpec(0, size, \lin, 1, 0));
		//3x train
		data_preset_trains = 3.collect{ NumericControlValue(spec: ControlSpec(0, size, \lin, 1, 0)) };
		//tables
		data_preset_pulsaret = 3.collect{ NumericControlValue(spec: ControlSpec(0, size, \lin, 1, 0)) };
		data_preset_envelope = 3.collect { NumericControlValue(spec: ControlSpec(0, size, \lin, 1, 0)) };
		data_preset_frequency = 3.collect { NumericControlValue(spec: ControlSpec(0, size, \lin, 1, 0)) };
		//preset sliders
		data_preset_train_interpolation = 3.collect{ NumericControlValue(spec: ControlSpec(0, size, 'lin', 1, 0)) };
		data_preset_pulsaret_interpolation = 3.collect{ NumericControlValue(spec: ControlSpec(0, size, 'lin', 1, 0)) };
		data_preset_envelope_interpolation = 3.collect{ NumericControlValue(spec: ControlSpec(0, size, 'lin', 1, 0)) };
		data_preset_frequency_interpolation = 3.collect{ NumericControlValue(spec: ControlSpec(0, size, 'lin', 1, 0)) };

		allPresetData = [
			data_preset_global, data_preset_trains,
			data_preset_pulsaret, data_preset_envelope, data_preset_frequency,
			data_preset_train_interpolation,
			data_preset_pulsaret_interpolation,  data_preset_envelope_interpolation, data_preset_frequency_interpolation
		];

		^allPresetData
	}
}