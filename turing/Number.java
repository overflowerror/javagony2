public class Number {
	private static class NumberWrapper {
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
		
		@Override
		public void executeIfNotEquals(Number n, Action a) {
			n.executeIfNotZero(a);
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
		
		public void executeIfNotEquals(Number n, Action a) {
			n.executeIfNegative(() -> {
				positive.executeIfNotEquals(((NegativeNumber) n).positive, a);
			});
			n.executeIfNotNegative(() -> {
				a.execute();
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
	
	public static Number max(Number n1, Number n2) {
		NumberWrapper w = new NumberWrapper();
	
		n1.executeIfNegative(() -> {
			n2.executeIfNegative(() -> {
				w.n = Number.max(((NegativeNumber) n2).positive, ((NegativeNumber) n1).positive);
			});
			n2.executeIfNotNegative(() -> {
				w.n = n2;
			});
		});
		n1.executeIfNotNegative(() -> {
			n2.executeIfNegative(() -> {
				w.n = n1;
			});
			n2.executeIfNotNegative(() -> {
				n1.executeIfZero(() -> {
					n2.executeIfZero(() -> {
						w.n = zero();
					});
					n2.executeIfNotZero(() -> {
						w.n = n2;
					});
				});
				n1.executeIfNotZero(() -> {
					n2.executeIfZero(() -> {
						w.n = n1;
					});
					n2.executeIfNotZero(() -> {
						Number tmp = Number.max(n1.pre, n2.pre);
						tmp.executeIfEquals(n1.pre, () -> {
							w.n = n1;
						});
						tmp.executeIfEquals(n2.pre, () -> {
							w.n = n2;
						});
					});
				});
			});
		});
		
		return w.n;
	}
	
	public static Number min(Number n1, Number n2) {
		Number max = max(n1, n2);
		
		NumberWrapper w = new NumberWrapper();
		
		n1.executeIfEquals(max, () -> {
			w.n = n2;
		});
		
		n2.executeIfEquals(max, () -> {
			w.n = n1;
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
	
	public void executeIfNotEquals(Number n, Action a) {
		n.executeIfNotNegative(() -> {
			n.executeIfNotZero(() -> {
				pre.executeIfNotEquals(n.pre, a);
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
