NuPG_Sieve {


	residual {|gen = 3, offset = 0, limit = 16|

		var residual = Sieve.new_o(gen, offset, limit);
		^residual;
	}

	//short
	r {|gen = 3, offset = 0, limit = 16| ^this.residual(gen, offset, limit)}

	//period of a sieve is equal to a lowest common multiple
	period {|sieve|
		//check the input format - if Sieve, convert to intervals, get an array, remove duplicates
		var getIntervals = sieve.collect{|item| item.toIntervals.list.asSet.asArray}.flatten;
		var conversion = getIntervals.reduce(\lcm);

		^conversion
	}

//sieve formatting
//adapted from Christopher Ariza python implementation of sieves within athenaCL environment

//sieve to binary
//take an integer series of values
//fill all spaces with zeros that are not occupied
//the result will always be sorted
	sieveSequentialBinary {|sieve|

		var seriesCopy = if(sieve.isKindOf(Sieve),  {sieve.list.array}, {"input not a sieve".postln});
	    var min = seriesCopy.minItem;
	    var max = seriesCopy.maxItem;
		var seriesAsBinary;
        var coversion = (min..max).collect{|x|
			seriesCopy.detect({|item|
				seriesAsBinary = x == item;
			});
			seriesAsBinary.asInteger;
		};

		^coversion;
	}

//a variant of sieve to binary
//takes an integer series of values
//get the width (interval) - this is always one value less then the input list
//replaces each integer with corresponding number of 0 and 1 in a sequence
//e.g. input = [2,3,3] -> output [0,0,1,1,1,0,0,0]
sieveSegmentedBinary {|series|

		var seriesCopy = if(series.isKindOf(Sieve),  {series.deepCopy}, {series.toSieve});

		var conversion = seriesCopy.toIntervals.list.asArray.collect{|i, index|
			Array.fill(i, { index.odd.asInteger })
		}.flatten;

		^conversion;
	}

//read all values from a list
//normalize values within min and maximum of series
	sieveNormalize {|series|

		var seriesCopy = if(series.isKindOf(Array),  {series.deepCopy}, {series.list.asArray});
		^seriesCopy.normalize;
	}

	//width in Ariza - is implemented by Meyer as  interval
sieveWidth {|series|

	var seriesCopy = if(series.isKindOf(Sieve),  {series.deepCopy}, {series.toSieve});

	^series.toIntervals.list.asArray;

}
}

