public class Fizzbuzz {

	@FunctionalInterface
	static interface Action {
		void execute();
	}

	@FunctionalInterface
	static interface Consumer {
		void take(int i);
	}

	static Action nop = () -> {};
	
	static void executeIfElse(int condition, Action ifAction, Action elseAction) {
		Action[] branches = new Action[condition + 1];
		branches[condition] = ifAction;
		branches[0] = elseAction;
		branches[condition].execute();
	}
	
	static void forLoop(int start, int end, Consumer each) {
		executeIfElse(end - start, () -> {
			each.take(start);
			forLoop(start + 1, end, each);
		}, nop);
	}

	public static void main(String[] args) {
		forLoop(1, 20, i -> {
			executeIfElse(i % 3, () -> {
				executeIfElse(i % 5, () -> {
					System.out.print(i);
				}, () -> {
					System.out.print("Buzz");
				});
			}, () -> {
				System.out.print("Fizz");
				executeIfElse(i % 5, nop, () -> {
					System.out.print(" Buzz");
				});
			});
			System.out.println();
		});
	}
}
