NuPG_DataCopy {

	classvar <>copy;

	*copyData{|array|
		copy = array;

		^copy
	}

	*pasteData{


		var paste = this.copy;

		^paste
	}
}