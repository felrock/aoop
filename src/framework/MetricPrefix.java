package framework;
/**
 * MetricPrefix is used to make small and big numbers easier to read
 * */
public enum MetricPrefix{
	MEGA('M',6),
	KILO('k',3),
	NONE('\u0000',0),
	MILI('m',-3),
	MICRO('\u00B5',-6),
	NANO('n',-9),
	PICO('p',-12);
	MetricPrefix(char letter, int amount){
		this.letter = letter;
		this.amount = amount;
	}
	public char letter;
	public int amount;
	public static final String toString(double value) {
        double tval = value;
        int order = 0;
        if(tval == 0){
        	return Math.round(tval)+"";
        }
        while(tval > 1000.0) {
            tval /= 1000.0;
            order += 3;
        }
        while(tval < 1.0) {
            tval *= 1000.0;
            order -= 3;
        }
        for(MetricPrefix x : MetricPrefix.values()){
        	if(order == x.amount){
        		return Math.round(tval) +""+ x.letter;
        	}
        }
        return "";
    }
}