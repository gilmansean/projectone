package net.blackbearmagic.projects.hyperspintools;

public enum Action {
	CREATE("Directory Create Only"), MOVE("File Move Only"), CREATE_MOVE(
			"Create Directory and Move File");

	private final String name;

	private Action(String s) {
		this.name = s;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : this.name.equals(otherName);
	}

	@Override
	public String toString() {
		return this.name;
	}

	public String getValue() {
		return this.name;
	}

	public static Action getEnum(String value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		for (Action v : Action.values()) {
			if (value.equalsIgnoreCase(v.getValue())) {
				return v;
			}
		}
		throw new IllegalArgumentException();
	}
}
