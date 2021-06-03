public class StateMachine {
	public static final Number END_STATE = Number.zero();

	public static final Number READ_LEFT = END_STATE.succ();
	public static final Number EXPECT_0_RIGHT = READ_LEFT.succ();
	public static final Number EXPECT_0_RIGHT_CHECK = EXPECT_0_RIGHT.succ();
	public static final Number EXPECT_1_RIGHT = EXPECT_0_RIGHT_CHECK.succ();
	public static final Number EXPECT_1_RIGHT_CHECK = EXPECT_1_RIGHT.succ();
	
	public static final Number READ_RIGHT = EXPECT_1_RIGHT_CHECK.succ();
	public static final Number EXPECT_0_LEFT = READ_RIGHT.succ();
	public static final Number EXPECT_0_LEFT_CHECK = EXPECT_0_LEFT.succ();
	public static final Number EXPECT_1_LEFT = EXPECT_0_LEFT_CHECK.succ();
	public static final Number EXPECT_1_LEFT_CHECK = EXPECT_1_LEFT.succ();

	public static final int INITIAL_STATE = READ_LEFT.value();
	
	public static final Number zero = Number.zero();
	public static final Number one = Number.zero().succ();
	public static final Number minusOne = Number.zero().pre();

	public class Transition {
		Number nextState;
		char write;
		Number move;
		
		public Transition(Number nextState, char write, Number move) {
			this.nextState = nextState;
			this.write = write;
			this.move = move;
		}
	}
	
	private Transition table[][];
	int currentState = INITIAL_STATE;
	
	private Action onHalt;
	
	public StateMachine(Action onHalt) {
	
		this.onHalt = onHalt;
		
		table = new Transition[11][256];
		
		table[READ_LEFT.value()]['1'] = new Transition(EXPECT_1_RIGHT, ' ', one);
		table[READ_LEFT.value()]['0'] = new Transition(EXPECT_0_RIGHT, ' ', one);
		table[READ_LEFT.value()][' '] = new Transition(END_STATE, ' ', zero);
		
		table[EXPECT_1_RIGHT.value()]['1'] = new Transition(EXPECT_1_RIGHT, '1', one);
		table[EXPECT_1_RIGHT.value()]['0'] = new Transition(EXPECT_1_RIGHT, '0', one);
		table[EXPECT_1_RIGHT.value()][' '] = new Transition(EXPECT_1_RIGHT_CHECK, ' ', minusOne);
		table[EXPECT_1_RIGHT_CHECK.value()]['1'] = new Transition(READ_RIGHT, ' ', minusOne);
		table[EXPECT_1_RIGHT_CHECK.value()]['0'] = new Transition(EXPECT_1_RIGHT_CHECK, ' ', zero);
		table[EXPECT_1_RIGHT_CHECK.value()][' '] = new Transition(EXPECT_1_RIGHT_CHECK, ' ', zero); // loop endless
		
		table[EXPECT_0_RIGHT.value()]['1'] = new Transition(EXPECT_0_RIGHT, '1', one);
		table[EXPECT_0_RIGHT.value()]['0'] = new Transition(EXPECT_0_RIGHT, '0', one);
		table[EXPECT_0_RIGHT.value()][' '] = new Transition(EXPECT_0_RIGHT_CHECK, ' ', minusOne);
		table[EXPECT_0_RIGHT_CHECK.value()]['0'] = new Transition(READ_RIGHT, ' ', minusOne);
		table[EXPECT_0_RIGHT_CHECK.value()]['1'] = new Transition(EXPECT_0_RIGHT_CHECK, ' ', zero);
		table[EXPECT_0_RIGHT_CHECK.value()][' '] = new Transition(EXPECT_0_RIGHT_CHECK, ' ', zero); // loop endless
		
		table[READ_RIGHT.value()]['1'] = new Transition(EXPECT_1_LEFT, ' ', minusOne);
		table[READ_RIGHT.value()]['0'] = new Transition(EXPECT_0_LEFT, ' ', minusOne);
		table[READ_RIGHT.value()][' '] = new Transition(END_STATE, ' ', zero);
		
		table[EXPECT_1_LEFT.value()]['1'] = new Transition(EXPECT_1_LEFT, '1', minusOne);
		table[EXPECT_1_LEFT.value()]['0'] = new Transition(EXPECT_1_LEFT, '0', minusOne);
		table[EXPECT_1_LEFT.value()][' '] = new Transition(EXPECT_1_LEFT_CHECK, ' ', one);
		table[EXPECT_1_LEFT_CHECK.value()]['1'] = new Transition(READ_LEFT, ' ', one);
		table[EXPECT_1_LEFT_CHECK.value()]['0'] = new Transition(EXPECT_1_LEFT_CHECK, ' ', zero);
		table[EXPECT_1_LEFT_CHECK.value()][' '] = new Transition(EXPECT_1_LEFT_CHECK, ' ', zero); // loop endless
		
		table[EXPECT_0_LEFT.value()]['1'] = new Transition(EXPECT_0_LEFT, '1', minusOne);
		table[EXPECT_0_LEFT.value()]['0'] = new Transition(EXPECT_0_LEFT, '0', minusOne);
		table[EXPECT_0_LEFT.value()][' '] = new Transition(EXPECT_0_LEFT_CHECK, ' ', one);
		table[EXPECT_0_LEFT_CHECK.value()]['0'] = new Transition(READ_LEFT, ' ', one);
		table[EXPECT_0_LEFT_CHECK.value()]['1'] = new Transition(EXPECT_0_LEFT_CHECK, ' ', zero);
		table[EXPECT_0_LEFT_CHECK.value()][' '] = new Transition(EXPECT_0_LEFT_CHECK, ' ', zero); // loop endless
	}
	
	public Transition stateChange(char input) {
		Transition t = table[currentState][input];
		
		t.nextState.executeIfZero(onHalt);
		
		currentState = t.nextState.value();
		
		return t;
	}
}
