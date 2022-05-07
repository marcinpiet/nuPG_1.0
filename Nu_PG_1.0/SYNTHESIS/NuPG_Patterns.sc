NuPG_Sequencer_Patterns {
	var <>valueMicro, <>microSeqData, <>microSeqTask;
	var <>valueMeso, <>mesoSeqData, <>mesoSeqTask;

//Micro Sequencer
	mapMicroSeq {|data|

		//three trains
		microSeqData = 3.collect{|i|
			//5 parameters of a sequencer
			5.collect{|n|
				Prout({
					loop{ data.data_microSequencer[i][n].value.do(_.yield)}})
				.linlin(0.0, 1.0,
					data.data_microSequencerRanges[i][n][0],
					data.data_microSequencerRanges[i][n][1]).loop.asStream
			}
		};


		valueMicro = [1,1,1,0,1]!3;

		microSeqTask = 3.collect{|i|
			Tdef(("microTask_"++(1+i).asString).asSymbol, {|ev|

			var names = ["trigFreqMicro_", "grainFreqMicro_", "envMultMicro_", "panMicro_",
			"ampMicro_"];
		    var patt = names.size.collect{|l|
					PL((names[l]++(1+i).asString).asSymbol).loop.asStream
				};

				inf.do({
					Ndef(("train_"++(1+i).asString).asSymbol).set(

					\micro_trigFreqMult, patt[0].next,
					\micro_grainMult, patt[1].next,
					\micro_envRateMult, patt[2].next,
					\micro_panMult, patt[3].next,
					\micro_ampMult, patt[4].next);
					data.data_microSpeed[i].value.reciprocal.wait

				})
			})
		}

	}
	//micro sequencer pattern
	microTask {
		^microSeqTask
	}

	//Meso Sequencer
	mapMesoSeq {|data|

		//three trains
		mesoSeqData = 3.collect{|i|
			//5 parameters of a sequencer
			5.collect{|n|
				Prout({
					loop{ data.data_mesoSequencer[i][n].value.do(_.yield)}})
				.linlin(0.0, 1.0,
					data.data_mesoSequencerRanges[i][n][0],
					data.data_mesoSequencerRanges[i][n][1]).loop.asStream
			}
		};


		valueMeso = [1,1,1,0,1]!3;

		mesoSeqTask = 3.collect{|i|
			Tdef(("mesoTask_"++(1+i).asString).asSymbol, {|ev|

			var names = ["trigFreqMeso_", "grainFreqMeso_", "envMultMeso_", "panMeso_",
			"ampMeso_"];
		    var patt = names.size.collect{|l|
					PL((names[l]++(1+i).asString).asSymbol).loop.asStream};

				inf.do({
					Ndef(("train_"++(1+i).asString).asSymbol).set(

					\meso_trigFreqMult, patt[0].next,
					\meso_grainMult, patt[1].next,
					\meso_envRateMult, patt[2].next,
					\meso_panMult, patt[3].next,
					\meso_ampMult, patt[4].next);
					(NuPg_Preset.data_speedMeso[i][0].value/2048).wait

				})
			})
		}
	}
	//micro sequencer pattern
	mesoTask {
		^mesoSeqTask
	}
}