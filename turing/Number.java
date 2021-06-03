public class Number {
	private class NumberWrapper {
		Number n;
	}

	static class Zero extends Number {
		public Zero() {
			super(null);
		}
	
		@Override
		public void executeIfZero(Action action) {
			action.execute();
		}
		
		@Override
		public void executeIfNotZero(Action action) {
		}
		
		@Override
		public int value() {
			return 0;
		}
		
		@Override
		public void executeIfEquals(Number n, Action a) {
			n.executeIfZero(a);
		}
	}
	
	static class NegativeNumber extends Number {
		private Number positive;
		
		public NegativeNumber(Number p) {
			super(null);
			positive = p;
		}
		
		@Override
		public Number succ() {
			NumberWrapper w = new NumberWrapper();
			
			positive.executeIfZero(() -> {
				w.n = zero().succ();
			});
			
			positive.executeIfNotZero(() -> {
				w.n = new NegativeNumber(positive.pre);
			});
			
			return w.n;
		}
		
		@Override
		public Number pre() {
			return new NegativeNumber(positive.succ());
		}
		
		@Override
		public int value() {
			return -positive.value();
		}
		
		@Override
		public void executeIfNegative(Action a) {
			a.execute();
		}
		
		@Override
		public void executeIfNotNegative(Action a) {
		}
		
		@Override
		public void executeIfZero(Action a) {
			positive.executeIfZero(a);
		}
		
		@Override
		public void executeIfNotZero(Action a) {
			positive.executeIfNotZero(a);
		}
		
		@Override
		public void executeIfEquals(Number n, Action a) {
			n.executeIfNegative(() -> {
				positive.executeIfEquals(((NegativeNumber) n).positive, a);
			});
		}
		
		@Override
		public void forEach(Action a) {
			positive.forEach(a);
		}
	}

	private Number pre;
	
	private Number(Number pre) {
		this.pre = pre;
	}
	
	public static Number zero() {
		return new Zero();
	}
	
	public Number succ() {
		return new Number(this);
	}
	
	public void forEach(Action a) {
		executeIfNotZero(() -> {
			a.execute();
			pre.forEach(a);
		});
	}
	
	public Number add(Number n) {
		NumberWrapper w = new NumberWrapper();
		w.n = this;
	
		n.executeIfNegative(() -> {
			n.forEach(() -> {
				w.n = w.n.pre();
			});
		});
		n.executeIfNotNegative(() -> {
			n.forEach(() -> {
				w.n = w.n.succ();
			});
		});
		
		return w.n;
	}
	
	public Number pre() {
		NumberWrapper w = new NumberWrapper();
		executeIfZero(() -> {
			w.n = new NegativeNumber(succ());
		});
		
		executeIfNotZero(() -> {
			w.n = pre;
		});
		
		return w.n;
	}
	
	public void executeIfZero(Action action) {
	}
	
	public void executeIfNotZero(Action action) {
		action.execute();
	}
	
	public int value() {
		return this.pre.value() + 1;
	}
	
	public void executeIfEquals(Number n, Action a) {
		n.executeIfNotNegative(() -> {
			n.executeIfNotZero(() -> {
				pre.executeIfEquals(n.pre, a);
			});
		});
	}
	
	public void executeIfNegative(Action a) {
	}
	
	public void executeIfNotNegative(Action a) {
		a.execute();
	}
	
	public String toString() {
		return String.valueOf(value());
	}
}
