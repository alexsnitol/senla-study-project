import java.util.ArrayList;

public class Garage {
	// true - place is taken
	// false - place is free
	private ArrayList<Boolean> places = new ArrayList<>();


	Garage() {}

	/**
	 *
	 * @param sizeOfPlaces
	 */
	Garage(int sizeOfPlaces) {
		addPlace(sizeOfPlaces);
	}

	/**
	 *
	 * @param number
	 */
	public void addPlace(int number) {
		for (int i = 0; i < number; i++)
			places.add(false);
	}

	/**
	 *
	 * @param index
	 */
	public void deletePlace(int index) {
		if (index >= 0 && index < places.size())
				places.remove(index);

	}

	/**
	 *
	 * @param index
	 */
	public boolean getPlace(int index) {
		if (index >= 0 && index < places.size())
			return places.get(index);
		return true;
	}

	public int sizePlaces() {
		return places.size();
	}

	/**
	 *
	 * @param index
	 */
	public byte takePlace(int index) {
		if (index >= 0 && index < places.size()) {
			if (places.get(index)) {
				return -1;
			}
			else {
				places.set(index, true);
			}
		}
		return 0;
	}

	/**
	 *
	 * @param index
	 */
	public void freePlace(int index) {
		if (index >= 0 && index < places.size())
			places.set(index, false);
	}

	public void printPlaces() {
		for (int i = 0; i < places.size(); i++) {
			System.out.print("place " + (i + 1) + " ");
			System.out.println((places.get(i)) ? "is taken" : "is free");
		}
	}
}