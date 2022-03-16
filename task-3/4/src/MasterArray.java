import java.util.ArrayList;

public class MasterArray {

	private ArrayList<Master> masters = new ArrayList<Master>();

	/**
	 *
	 * @param master
	 */
	public void addMaster(Master master) {
		masters.add(master);
	}

	/**
	 *
	 * @param index
	 */
	public void deleteMaster(int index) {
		if (index >= 0 && index < masters.size())
			masters.remove(index);
	}

	/**
	 *
	 * @param index
	 */
	public Master getMaster(int index) {
		if (index >= 0 && index < masters.size())
			return masters.get(index);
		return null;
	}

	public int sizeMasters() {
		return masters.size();
	}

	public void printMasters() {
		for (int i = 0; i < masters.size(); i++) {
			System.out.println((i + 1) + ". " + masters.get(i).toString());
		}
	}

}