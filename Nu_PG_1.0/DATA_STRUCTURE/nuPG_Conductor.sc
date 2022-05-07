//marcin pietruszewski (04.05.2018)

//nuPG class for data handling and presets
//based on Conductor quark by Ron Kuivila: http://www.wesleyan.edu/academics/faculty/rkuivila/profile.html
//method conductorInit needs to be called in order to initialise the conductor (!)
//and create slot values for nuPG parameters




NuPG_Conductor {

	var conductor;
	var <>data_main, <>data_perGrainModulation;
	var <>data_sequencer, <>data_sequencerSpeed, <>data_sequencerRanges;
	var <>data_matrix;
	var <>data_modulators;
	var <>data_modulatorsRange;
	var <>data_wavefold;
	var <>data_pulsaret, <> data_envelope, <>data_frequency;
	var <>data_pul_shaper, <>data_env_shaper, <>data_freq_shaper;
	var <>data_probabilityMask, <>data_burstMask, <>data_channelMask;
	var <>data_sieveMask, <>data_sieveMaskSequence;
	var <>data_pulsaretFFT;
	var <>data_envelope_dftZoom;
	var <>data_scrubber, <>data_scrubberPositionLocal;

	//conductor initialisation method
	conductorInit {

		//conductor's arguments -> setting up 1 global (con) and 5 local presets (con_n)
		conductor = Conductor.make {
			arg
			con, //global
			con_1 = \Conductor, //stream 1
			con_2 = \Conductor, //stream 2
			con_3 = \Conductor; //stream 3

			//data as arrays
			var st = 3; //three streams
			//main
			data_main = Array.newClear(st);
			//pergrain modulation
			data_perGrainModulation = Array.newClear(st);
			//sequencers
			data_sequencer = Array.newClear(st);
			data_sequencerSpeed = Array.newClear(st);
			data_sequencerRanges = Array.newClear(st);
			//modulator matrix
			data_matrix = Array.newClear(st);
			data_modulators = Array.newClear(st);
			data_modulatorsRange = Array.newClear(st);
			//wavefold
			data_wavefold = Array.newClear(st);
			//masking
			data_probabilityMask = Array.newClear(st);
			data_burstMask = Array.newClear(st);
			data_channelMask = Array.newClear(st);
			data_sieveMask = Array.newClear(st);
			data_sieveMaskSequence =  Array.newClear(st);
			//tables
			data_pulsaret = Array.newClear(st);
			data_envelope = Array.newClear(st);
			data_frequency = Array.newClear(st);
			//shaper tables
			data_pul_shaper = Array.newClear(st);
			//pulsaretFFT
			data_pulsaretFFT = Array.newClear(st);
			//env dftzoom
			data_envelope_dftZoom = Array.newClear(st);
			//scrubber
			data_scrubber = Array.newClear(st);
			//scrubberPosition
			data_scrubberPositionLocal = Array.newClear(st);



			st.collect{|i|
				//pack names from conductor arguemnt list (is there a simpler way?) in an array
				var conNames = [con_1, con_2, con_3];

				//this creates actual slots for values
				conNames[i].make{
					arg
					conductor,
					con_pul = \Conductor, //local pulsar conductor
					con_env = \Conductor, //local envelope conductor
					con_freq = \Conductor, //local frequency conductor

					//main
					mainTrig, mainGrain, mainEnv, mainPan, mainAmp,
					//pergrain modulators
					mainMod, mainRatio, mainFlux,
					//sequencers
					data_0, data_1, data_2, data_3, data_4, //params
					speed, //speed
					//sequencers ranges
					min_0, max_0, min_1, max_1, min_2, max_2, min_3, max_3,
					min_4, max_4,
					//modulator matrix
					mx_00_00, mx_01_00, mx_02_00, mx_03_00,
					mx_10_00, mx_11_00, mx_12_00, mx_13_00,
					mx_20_00, mx_21_00, mx_22_00, mx_23_00,
					mx_30_00, mx_31_00, mx_32_00, mx_33_00,
					mx_40_00, mx_41_00, mx_42_00, mx_43_00,
					mx_50_00, mx_51_00, mx_52_00, mx_53_00,
					mx_60_00, mx_61_00, mx_62_00, mx_63_00,
					mx_70_00, mx_71_00, mx_72_00, mx_73_00,
					//modulators
					freq_0, freq_1, freq_2, freq_3,
					index_0,  index_1, index_2, index_3,
					//modulators range
					mlow_0, mlow_1, mlow_2, mlow_3,
					mhi_0, mhi_1, mhi_2, mhi_3,
					mlow_4, mlow_5, mlow_6, mlow_7,
					mhi_4, mhi_5, mhi_6, mhi_7,
					//wavefold
					low_0, low_1, low_2, low_3,
					hi_0, hi_1, hi_2, hi_3,
					//shapers
					pul_shaper,
					//masking
					//probability
					probability,
					//burst
					burst, rest,
					//channel
					channelMask, centerMask,
					//sieve
					sieveMaskOn, sieveMod,
					sieveSequence,
					//pulsaretFFT
					pulsaretFFT,
					//env dftzoom
					envelopeDftZoom,
					//scrubber
					scrubberPosition,
					//local scrubbers
					scrubberPosition_1, scrubberPosition_2, scrubberPosition_3, scrubberPosition_4, scrubberPosition_5;

					//local tables names in an array
					var tableNames = [con_pul, con_env, con_freq];


					//pack param names in a list
					var paramNames = [
						//main
						[mainTrig, mainGrain, mainEnv, mainPan, mainAmp],
						//pergrain modulators
						[mainMod, mainRatio, mainFlux],
						//sequencers
						[data_0, data_1, data_2, data_3, data_4], //params
						[speed], //speed
						//sequencers ranges
						[[min_0, max_0], [min_1, max_1], [min_2, max_2], [min_3, max_3], [min_4, max_4]],
						//modulator matrix
						[
							[mx_00_00, mx_01_00, mx_02_00, mx_03_00],
								[mx_10_00, mx_11_00, mx_12_00, mx_13_00],
								[mx_20_00, mx_21_00, mx_22_00, mx_23_00],
								[mx_30_00, mx_31_00, mx_32_00, mx_33_00],
								[mx_40_00, mx_41_00, mx_42_00, mx_43_00],
								[mx_50_00, mx_51_00, mx_52_00, mx_53_00],
								[mx_60_00, mx_61_00, mx_62_00, mx_63_00],
								[mx_70_00, mx_71_00, mx_72_00, mx_73_00]


						],
						//modulators
						[[freq_0, index_0], [freq_1, index_1], [freq_2, index_2], [freq_3, index_3]],
						//wavefold
						[[low_0, hi_0], [low_1, hi_1], [low_2, hi_2], [low_3, hi_3]],
						//masking
						//probability
						[probability],
						//burst
						[burst, rest],
						//channel
						[channelMask, centerMask],
						//sieve
						[sieveMaskOn, sieveMod],
						//sieve sequence
						[sieveSequence],
						//shapers
						[pul_shaper],
						//pulsaretFFT
						[pulsaretFFT],
						//env dftzoom
						[envelopeDftZoom],
						//scrubber
						[scrubberPosition],
						//local scrubbers
						[scrubberPosition_1, scrubberPosition_2, scrubberPosition_3, scrubberPosition_4, scrubberPosition_5],
						//modulators range
						[
							[mlow_0, mhi_0], [mlow_1, mhi_1], [mlow_2, mhi_2], [mlow_3, mhi_3],
							[mlow_4, mhi_4], [mlow_5, mhi_5], [mlow_6, mhi_6], [mlow_7, mhi_7]
						],
					];


					//main parameters
					data_main[i] =
					paramNames[0].size.collect{|p|
						var defVal = [120, 1.0, 1.0, 0.0, 0.5];
						var ranges = [
							[1.0, 3000], //trigger frequency
							[0.05, 16.0], //grain frequency
							[0.0, 2.0], //envelope size
							[-1.0, 1.0], //panning
							[0.0, 1.0] //amplitude
						];

						var warp = [\exp, \exp, \lin, \lin, \lin];

						paramNames[0][p].sp(defVal[p], ranges[p][0], ranges[p][1], 0.001, warp[p]);
					};

					//pergrain mod
					data_perGrainModulation[i] =
					paramNames[1].size.collect{|p|
						var defVal = [0.0, 0.0, 0.0];
						var ranges = [
							[0.0, 16.0], //modulation amount
							[0.001, 16.0], //modulation ratio
							[0.0, 10.0], //flux
						];

						var warp = [ \lin, \lin, \lin];

						paramNames[1][p].sp(defVal[p], ranges[p][0], ranges[p][1], 0.001, warp[p]);
					};


					//sequencer
					data_sequencer[i] =
					paramNames[2].size.collect{|p|
						//value -> type of data to hold [array, integer]
						//sequencer holds 2048 data points by default
						var value = (0..2047)/2048;

						//-> 2048 data points within a range 0.0-1.0
						paramNames[2][p].sp(value, 0.0, 1.0);
					};

					//sequencer speed
					data_sequencerSpeed[i] =
					paramNames[3].size.collect{|p|

						paramNames[3][p].sp(150.0, 75.0, 3000.0, 0.001);
					};

					//sequencer ranges
					data_sequencerRanges[i] =
					paramNames[4].size.collect{|p|
						paramNames[4][p].size.collect{|l|
							var ranges = [
								[0.0, 10.0], //trigger frequency
								[0.1, 20.0], //grain frequency
								[0.1, 2.0], //envelope multiplicator
								[-1.0,1.0], //panning
								[0.0,2.0], //amplitude

							];
							paramNames[4][p][l].sp(ranges[p][l], ranges[p][0], ranges[p][1]);
						}
					};

					//matrix
					data_matrix[i] = paramNames[5].size.collect{|p|

						paramNames[5][p].size.collect{|l|
								paramNames[5][p][l].sp(0, 0, 1, 1)
						}
					};

					//modulators

					data_modulators[i] = paramNames[6].size.collect{|p|

						paramNames[6][p].size.collect{|l|
							var ranges = [
								[0.01, 50.0],
								[1, 10],
							];
							var step = [[0.01],[1]];
							var wrap = [[\exp],[\lin]];

							paramNames[6][p][l].sp(0.1, ranges[l][0], ranges[l][1], step[l][0], wrap[l][0])
						}
					};

					data_wavefold[i] =  paramNames[7].size.collect{|p|
						paramNames[7][p].size.collect{|l|

							paramNames[7][p][l].sp(0.0, 0.0, 1.0, 0.1)
						}
					};

					//masking parameters
					//probability
					data_probabilityMask[i] =
					paramNames[8].size.collect{|p|

						paramNames[8][p].sp(1.0, 0.0, 1.0, 0.01);
					};

					//burst
					data_burstMask[i] =
					paramNames[9].size.collect{|p|
						var defVal = [1, 0];
						var ranges = [
							[1, 2999], //burst
							[0, 2998], //rest
						];

						paramNames[9][p].sp(defVal[p], ranges[p][0], ranges[p][1], 1);
					};

					//channel
					data_channelMask[i] =
					paramNames[10].size.collect{|p|
						var defVal = [0, 1];
						var ranges = [
							[0, 1500], //channelMask
							[0, 1], //center repeat
						];

						paramNames[10][p].sp(defVal[p], ranges[p][0], ranges[p][1], 1);
					};

					//sieve
					data_sieveMask[i] =
					paramNames[11].size.collect{|p|
						var defVal = [0, 1];
						var ranges = [
							[0, 1], //sieve mask ON/OFF
							[1, 100], //sieve mod
						];

						paramNames[11][p].sp(defVal[p], ranges[p][0], ranges[p][1], 1);
					};

					//sieve sequence
					data_sieveMaskSequence[i] =
					paramNames[12].size.collect{|p|

						var value = (0..99)/100;
						//-> 16 data points within a range 0.0-1.0
						paramNames[12][p].sp(value, 0.0, 1.0);
					};

					//shapers parameters
					//pulsaret
					data_pul_shaper[i] =
					paramNames[13].size.collect{|p|

						var value = (0..15)/16;
						//-> 16 data points within a range 0.0-1.0
						paramNames[13][p].sp(value, 0.0, 1.0);
					};

					//pulsaretFFT
					data_pulsaretFFT[i] =
					paramNames[14].size.collect{|p|

						var value = (0..4095)/4096;
						paramNames[14][p].sp(value, 0.0, 1.0);
					};

					//env dftzoom
					data_envelope_dftZoom[i] =
					paramNames[15].size.collect{|p|

						paramNames[15][p].sp(1, 1, 10, 1);
					};

					//scrubber
					data_scrubber[i] =
					paramNames[16].size.collect{|p|

						paramNames[16][p].sp(0, 0, 1, 0.01);
					};
					//local scrubber
					data_scrubberPositionLocal[i] =
					paramNames[17].size.collect{|p|

						paramNames[17][p].sp(0, 0, 1, 0.01);
					};

					data_modulatorsRange[i] =  paramNames[18].size.collect{|p|

						paramNames[18][p].size.collect{|l|
							var ranges = [
								[0.0, 30.0],
								[0.01, 16],
								[0.0, 4.0],
								[-1.0, 1.0],
								[0.0, 1.0],
								[0.0, 16.0],
								[0.0, 16.0],
								[0.0, 16.0]
							];

							paramNames[18][p][l].sp(ranges[p][l], ranges[p][0], ranges[p][1], 0.01)
						}
					};


					//local tables presets -> rewrite it!!!!
					con_pul.make{
						arg con,
						table; //tables

						data_pulsaret[i] = table.sp((0..2047)/2048, -1.0, 1.0);

						//con.name_("stream "++i.asString);
						con.useInterpolator;
						con.presetKeys_(#[table]);
						con.interpKeys_(#[table]);
					};


					con_env.make{
						arg con,
						table; //tables

						data_envelope[i] = table.sp((0..2047)/2048, 0.0, 1.0);

						//con.name_("stream "++i.asString);
						con.useInterpolator;
						con.presetKeys_(#[table]);
						con.interpKeys_(#[table]);
					};

					con_freq.make{
						arg con,
						table; //tables

						data_frequency[i] = table.sp((0..2047)/2048, 0.0, 1.0);

						//con.name_("stream "++i.asString);
						con.useInterpolator;
						con.presetKeys_(#[table]);
						con.interpKeys_(#[table]);
					};

					conductor.name_("stream "++i.asString);
					conductor.useInterpolator;
					conductor.presetKeys_(#[
						//tables
						con_pul, con_env, con_freq,
						//main
						mainTrig, mainGrain, mainEnv, mainPan, mainAmp,
						//pergrain modulators
						mainMod, mainRatio, mainFlux,
						//sequencers
						data_0, data_1, data_2, data_3, data_4, //params
						speed, //speed
						//sequencers ranges
						min_0, max_0, min_1, max_1, min_2, max_2, min_3, max_3,
						min_4, max_4,
						//modulator matrix
						/*matrix,*/
						mx_00_00, mx_01_00, mx_02_00, mx_03_00,
					mx_10_00, mx_11_00, mx_12_00, mx_13_00,
					mx_20_00, mx_21_00, mx_22_00, mx_23_00,
					mx_30_00, mx_31_00, mx_32_00, mx_33_00,
					mx_40_00, mx_41_00, mx_42_00, mx_43_00,
					mx_50_00, mx_51_00, mx_52_00, mx_53_00,
					mx_60_00, mx_61_00, mx_62_00, mx_63_00,
					mx_70_00, mx_71_00, mx_72_00, mx_73_00,
						/*//modulators
						freq_0, freq_1, freq_2, freq_3,
						index_0,  index_1, index_2, index_3,
						//wavefold
						low_0, low_1, low_2, low_3,
						hi_0, hi_1, hi_2, hi_3,*/
						//shapers
						pul_shaper,
						//masking
						//probability
						probability,
						//burst
						burst, rest,
						//channel
						channelMask, centerMask,
						//sieve
						sieveMaskOn, sieveMod,
						sieveSequence,
						//pulsaretFFT
						//pulsaretFFT,
						//env dft zoom
						//envelopeDftZoom,
						//scrubb
						//scrubberPosition,
						//scrubberPosition_1, scrubberPosition_2, scrubberPosition_3, scrubberPosition_4, scrubberPosition_5
						mlow_0, mhi_0, mlow_1, mhi_1, mlow_2, mhi_2, mlow_3, mhi_3,
						mlow_4, mhi_4, mlow_5, mhi_5, mlow_6, mhi_6, mlow_7, mhi_7

					]);
					conductor.interpKeys_(#[
						//tables
						con_pul, con_env, con_freq,
						//main
						mainTrig, mainGrain, mainEnv, mainPan, mainAmp,
						//pergrain modulators
						mainMod, mainRatio, mainFlux,
						//sequencers
						data_0, data_1, data_2, data_3, data_4, //params
						speed, //speed
						//sequencers ranges
						min_0, max_0, min_1, max_1, min_2, max_2, min_3, max_3,
						min_4, max_4,
						//modulator matrix
						//matrix,
						mx_00_00, mx_01_00, mx_02_00, mx_03_00,
					mx_10_00, mx_11_00, mx_12_00, mx_13_00,
					mx_20_00, mx_21_00, mx_22_00, mx_23_00,
					mx_30_00, mx_31_00, mx_32_00, mx_33_00,
					mx_40_00, mx_41_00, mx_42_00, mx_43_00,
					mx_50_00, mx_51_00, mx_52_00, mx_53_00,
					mx_60_00, mx_61_00, mx_62_00, mx_63_00,
					mx_70_00, mx_71_00, mx_72_00, mx_73_00,

						//modulators
						freq_0, freq_1, freq_2, freq_3,
						index_0,  index_1, index_2, index_3,
						//wavefold
						low_0, low_1, low_2, low_3,
						hi_0, hi_1, hi_2, hi_3,
						//shapers
						pul_shaper,
						//masking
						//probability
						probability,
						//burst
						burst, rest,
						//channel
						channelMask, centerMask,
						//sieve
						sieveMaskOn, sieveMod,
						sieveSequence,
						//pulsaretFFT
						//pulsaretFFT,
						//env dft zoom
						//envelopeDftZoom,
						//scrubb
						//scrubberPosition,
						//scrubberPosition_1, scrubberPosition_2, scrubberPosition_3, scrubberPosition_4, scrubberPosition_5
						mlow_0, mhi_0, mlow_1, mhi_1, mlow_2, mhi_2, mlow_3, mhi_3,
						mlow_4, mhi_4, mlow_5, mhi_5, mlow_6, mhi_6, mlow_7, mhi_7
					]);

				}
			};
			con.useInterpolator;
		};
		^conductor
	}
	//just to be able to see that conductor is initialised
	conductor {
		^conductor
	}
	//get individual values from the conductor
	//parameters order -> 0-5 sequences, 6-14 main, 15 wetdry, 16 dur
	getValue {|stream, parameter|
		^conductor.value[stream][1][parameter][1]

	}

	getAllData {
		var data = [data_main, data_perGrainModulation, data_sequencer, data_sequencerSpeed, data_sequencerRanges,
			data_matrix, data_modulators, data_wavefold, data_probabilityMask, data_burstMask, data_channelMask,data_sieveMask, data_sieveMaskSequence, data_pulsaret, data_envelope, data_frequency,
			data_pul_shaper, data_pulsaretFFT, data_envelope_dftZoom, data_scrubber, data_scrubberPositionLocal, data_modulatorsRange];
		^data
	}
	valPack {
		//to do  - find nicer way of dividing the array = this doesn't do it!!!
		//need values to be packed in an array
		//5x [[6][9][1][1]]
		//?????
		var array;
		//5 streams all parameters in an array
		//5 streams array divided (clumps) into subarrays of 6,9,1,1 elements corresponding to parameters groups
		array = 3.collect{|s|
			(0..16).collect{|i| conductor.value[s][1][i][1]}.clumps([6, 9, 1, 1])};
		^array
	}
}


