package org.sudowars.Model.Game;

/**
 * This class is an extension of the {@link SingleplayerPlayerSlot} optimized for multi player games
 */
public class MultiplayerPlayerSlot extends SingleplayerPlayerSlot {

	private static final long serialVersionUID = 1L;
	private Score score = new Score();
	
	/**
	 * Gets the current score of the player attached to this slot.
	 *
	 * @return The {@link Score} of the player.
	 */
	public Score getScore() {
		return this.score;
	}
	
	/**
	 * Gets the hashcode of the current instance.
	 * @return an integer value indicating the hashcode of the current instance
	 */
	@Override
	public int hashCode() {
		final int prime = 27;
		int result = super.hashCode();
		result = prime * result + ((this.score == null) ? 0 : this.score.hashCode());
		return result;
	}
	
	/**
	 * Indicates whether the current instance is equal to a given object.
	 * @param otherObject reference to an object to check for equality
	 * @return {@code true} if given obejct refers to the same instance,<br>
	 * or refers to another MultiplayerPlayerSlot instance with equal attributes/properties,<br>
	 * otherwise {@code false}
	 */
	@Override
	public boolean equals(Object otherObject) {
		boolean result = false;
		if (otherObject instanceof MultiplayerPlayerSlot) {
			MultiplayerPlayerSlot otherSlot = (MultiplayerPlayerSlot) otherObject;
			result = (this == otherSlot || attributesEqual(this, otherSlot));
		}
		return result;
	}

	private static boolean attributesEqual(MultiplayerPlayerSlot first, MultiplayerPlayerSlot second) {
		assert first != null && second != null;
		
		return (first.hasPaused == second.hasPaused && first.score.equals(second.score)
				&& objectsEqual(first.attachedPlayer, second.attachedPlayer)
				&& objectsEqual(first.notes, second.notes));
		
	}
}
