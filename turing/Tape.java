public class Tape {

	private class CharWrapper {
		char c;
	}
	
	private class StringWrapper {
		String s;
	}

	@FunctionalInterface
	private interface Cell {
		char value(Number i);
	}
	
	private Cell cells;
	private Number min;
	private Number max;
	
	public Tape() {
		cells = (i) -> ' ';
		min = Number.zero();
		max = Number.zero();
	}
	
	public char get(Number i) {
		return cells.value(i);
	}
	
	public void set(Number i, char c) {
	
		final Cell _cells = cells;
		
		min = Number.min(min, i);
		max = Number.max(max, i);
	
		cells = (j) -> {
			CharWrapper w = new CharWrapper();
			
			w.c = _cells.value(j);
		
			j.executeIfEquals(i, () -> {
				w.c = c;
			});
			
			return w.c;
		};
	}
	
	private String cellString(Number min, Number max) {
		StringWrapper w = new StringWrapper();
		w.s = "";
		
		w.s = String.valueOf(get(min));
		
		min.executeIfNotEquals(max, () -> {
			w.s += cellString(min.succ(), max);
		});
		
		return w.s;
	}
	
	public String toString() {
		return min + " | " + cellString(min, max) + " | " + max;
	}
}
