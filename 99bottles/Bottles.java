public class Bottles {
	@FunctionalInterface
	static private interface Action {
		void execute();
	}
	@FunctionalInterface
	static private interface Consumer<T> {
		void take(T t);
	}

	static private class NumberWrapper {
		public Number number;

		public NumberWrapper(Number n) {
			number = n;
		} 
	}
	
	static private class CompareResult {
		private Number r1;
		private Number r2;

		public CompareResult(Number r1, Number r2) {
			this.r1 = r1;
			this.r2 = r2;
		}

		public void executeIfEqual(Action action) {
			r1.executeIfZero(() -> {
				r2.executeIfZero(action);
			});
		}

		public void executeIfGreaterOrEqual(Action action) {
			r2.executeIfZero(action);
		}

		public void executeIfLessOrEqual(Action action) {
			r1.executeIfZero(action);
		}

		public void executeIfGreater(Action action) {
			r2.executeIfZero(() -> {
				r1.executeIfNotZero(action);
			});
		}
		public void executeIfLess(Action action) {
			r1.executeIfZero(() -> {
				r2.executeIfNotZero(action);
			});
		}
	}

	static private class Number {
		private Number predecessor;

		public Number(Number pre) {
			predecessor = pre;
		}

		public Number clone() {
			return new Number(predecessor);
		}

		public static Number succ(Number pre) {
			return new Number(pre);
		}

		public static Number zero() {
			return new Zero();
		}

		public static Number one() {
			return succ(zero());
		}

		public static Number two() {
			return succ(one());
		}

		public Number pre() {
			return predecessor;
		}

		public static Number add(Number n1, Number n2) {
			NumberWrapper wrapper = new NumberWrapper(zero());

			n1.forEach((n) -> {
				wrapper.number = succ(wrapper.number);
			});

			n2.forEach((n) -> {
				wrapper.number = succ(wrapper.number);
			});

			return wrapper.number;
		}

		public static Number multiply(Number n1, Number n2) {
			NumberWrapper wrapper = new NumberWrapper(zero());

			n1.forEach((n) -> {
				wrapper.number = add(wrapper.number, n2); 
			});

			return wrapper.number;
		}

		public int value() {
			return predecessor.value() + 1;
		}
		
		public void executeIfZero(Action action) {
		}

		public void executeIfNotZero(Action action) {
			action.execute();
		}

		public void forEach(Consumer<Number> consumer) {
			executeIfNotZero(() -> {
				consumer.take(this);
				predecessor.forEach(consumer);
			});
		}

		public static Number subtract(Number n1, Number n2) {
			NumberWrapper wrapper = new NumberWrapper(n1.clone());

			n2.forEach((n) -> {
				wrapper.number = wrapper.number.pre();
			});

			return wrapper.number;
		}

		public CompareResult compare(Number compare) {
			return new CompareResult(subtract(this, compare), subtract(compare, this));
		}

		public String toString() {
			return String.valueOf(value());
		}
	}

	static private class Zero extends Number {
		public Zero() {
			super(null);
		}

		@Override
		public Number clone() {
			return new Zero();
		}

		@Override
		public int value() {
			return 0;
		}

		@Override
		public void executeIfZero(Action action) {
			action.execute();
		}

		@Override
		public void executeIfNotZero(Action action) {
		}
	
		@Override
		public Number pre() {
			return this;
		}
	}



	public static void main(String[] args) {
		Number one = Number.one();
		Number five = Number.add(Number.add(Number.two(), Number.two()), one);
		Number ten = Number.multiply(five, Number.two());
		Number hundred = Number.multiply(ten, ten);
		Number nintynine = hundred.pre();

		nintynine.forEach((n) -> {
			CompareResult cr = n.compare(one);
			cr.executeIfGreater(() -> {
				System.out.println(n + " bottles of beer on the wall.");
			});
			cr.executeIfEqual(() -> {
				System.out.println("One bottle of beer on the wall.");
			});
		});

		System.out.println("No bottles of beer on the wall.");
	}
}
