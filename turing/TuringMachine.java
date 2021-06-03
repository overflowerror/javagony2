public class TuringMachine {

	private Tape tape;
	private StateMachine sm;
	private Number position;
	
	public TuringMachine() {
		tape = new Tape();
		sm = new StateMachine(() -> {
			System.out.println("halted");
			System.exit(0);
		});
		
		position = Number.zero();
		
		Number n = Number.zero();
		tape.set(n, '0');
		n = n.succ();
		tape.set(n, '1');
		n = n.succ();
		tape.set(n, '1');
		n = n.succ();
		tape.set(n, '0');
		n = n.succ();
		tape.set(n, '0');
		n = n.succ();
		tape.set(n, '1');
		n = n.succ();
		tape.set(n, '1');
		n = n.succ();
		tape.set(n, '0');
	}
	
	public void step() {
		Number oldPosition = position;
		char in = tape.get(position);
		int state = sm.currentState;
		StateMachine.Transition t = sm.stateChange(in);
		tape.set(position, t.write);
		position = position.add(t.move);
		
		//System.out.println(state + ", " + oldPosition + ", " + in + " -> " + t.nextState + ", " + t.write + ", " + position);
		
		step();
	}
	
	public void run() {
		step();
	}

	public static void main(String[] args) {
		TuringMachine tm = new TuringMachine();
		tm.run();
	}
}
