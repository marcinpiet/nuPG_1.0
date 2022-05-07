NuPG_Modulators {

	//submodulators
	*subModulators {
		var sin = {|modFrequency = 1, modIndex = 1, wfLo = 0, wfHi = 0|
			SmoothFoldS2.ar( in: modIndex * SinOsc.ar(modFrequency, mul: 1),
				lo: -1,
				hi: 1,
				foldRangeLo: wfLo,
				foldRangeHi: wfHi
			)
		};
		var saw = {|modFrequency = 1, modIndex = 1, wfLo = 0, wfHi = 0|
			SmoothFoldS2.ar(in: modIndex * LFSaw.ar(modFrequency, mul: 1),
				lo: -1,
				hi: 1,
				foldRangeLo: wfLo,
				foldRangeHi: wfHi
			)
		};
		var lato = {|modFrequency = 1, modIndex = 1, wfLo = 0, wfHi = 0|
			SmoothFoldS2.ar(in: modIndex *  LatoocarfianC.ar(
				freq: modFrequency,
				a: LFNoise2.kr(2,1.5,1.5),
				b: LFNoise2.kr(2,1.5,1.5),
				c: LFNoise2.kr(2,0.5,1.5),
				d: LFNoise2.kr(2,0.5,1.5),
				mul: 1, add: 0),
			lo: -1,
			hi: 1,
			foldRangeLo: wfLo,
			foldRangeHi: wfHi
		)};
		var heno = {|modFrequency = 1, modIndex = 1, wfLo = 0, wfHi = 0|
			SmoothFoldS2.ar(in: modIndex * HenonC.ar(
				freq: modFrequency,
				a: LFNoise2.kr(1, 0.2, 1.2),
				b: LFNoise2.kr(1, 0.15, 0.15),
				mul: 1, add: 0),
			lo: -1,
			hi: 1,
			foldRangeLo: wfLo,
			foldRangeHi: wfHi
		)};
		var gendy = {|modFrequency = 1, modIndex = 1, wfLo = 0, wfHi = 0|
			SmoothFoldS2.ar(in: modIndex *  Gendy3.ar(
				ampdist: 6,
				durdist: 3,
				adparam: 0.9,
				ddparam: 0.9,
				freq: modFrequency,
				ampscale: 1),
			lo: -1,
			hi: 1,
			foldRangeLo: wfLo,
			foldRangeHi: wfHi
		)};
		^[sin,saw,lato,heno,gendy]
	}

	/*//submodulators
	*subModulators {
		var sin = {|modFrequency = 1, modIndex = 1|
			modIndex * SinOsc.ar(modFrequency, mul: modFrequency)
		};
		var saw = {|modFrequency = 1, modIndex = 1|
			modIndex * LFSaw.ar(modFrequency, mul: modFrequency)
		};
		var lato = {|modFrequency = 1, modIndex = 1|
			modIndex *  LatoocarfianC.ar(
				freq: modFrequency,
				a: LFNoise2.kr(2,1.5,1.5),
				b: LFNoise2.kr(2,1.5,1.5),
				c: LFNoise2.kr(2,0.5,1.5),
				d: LFNoise2.kr(2,0.5,1.5),
				mul: 1, add: 0)};
		var heno = {|modFrequency = 1, modIndex = 1|
			HenonC.ar(
				freq: modFrequency,
				a: LFNoise2.kr(1, 0.2, 1.2),
				b: LFNoise2.kr(1, 0.15, 0.15),
				mul: 1, add: 0)};
		var gendy = {|modFrequency = 1, modIndex = 1|
			modIndex *  Gendy3.ar(
				ampdist: 6,
				durdist: 3,
				adparam: 0.9,
				ddparam: 0.9,
				freq: modFrequency,
				ampscale: 1)};

		^[sin,saw,lato,heno,gendy]
	}*/

	*modulatorWrap {

		^3.collect{|k|
			4.collect{|i|

		Ndef(("mod_"++k++i).asSymbol)
	}
}
	}

	*modulatorChain {
		^3.collect{|i|

			8.collect{|l|

				Ndef(("modParam_"++i++l).asSymbol);
			}
		}
	}
}