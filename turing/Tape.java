public class Tape {

	private class CharWrapper {
		char c;
	}

	@FunctionalInterface
	private interface Cell {
		char value(Number i);
	}
	
	private Cell cells;
	
	public Tape() {
		cells = (i) -> ' ';
	}
	
	public char get(Number i) {
		return cells.value(i);
	}
	
	public void set(Number i, char c) {
	
		final Cell _cells = cells;
	
		cells = (j) -> {
			CharWrapper w = new CharWrapper();
			
			w.c = _cells.value(j);
		
			j.executeIfEquals(i, () -> {
				w.c = c;
			});
			
			return w.c;
		};
	}
}
