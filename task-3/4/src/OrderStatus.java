import java.util.ArrayList;
import java.util.Arrays;

public class OrderStatus {

	private ArrayList<String> statuses = new ArrayList<>(Arrays.asList("completed", "cancelled", "in process"));

	/**
	 * 
	 * @param status
	 */
	public void addStatus(String status) {
		statuses.add(status);
	}

	/**
	 * 
	 * @param index
	 */
	public void deleteStatus(int index) {
		statuses.remove(index);
	}

	/**
	 * 
	 * @param index
	 */
	public String getStatus(int index) {
		return statuses.get(index);
	}

	/**
	 *
	 * @param index
	 * @param status
	 */
	public void setStatus(int index, String status) {
		statuses.set(index, status);
	}

	public int sizeOrderStatuses() {
		return statuses.size();
	}

}