import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Order {

	private Calendar timeOfCreated = new GregorianCalendar();
	private Calendar timeOfBegin;
	private Calendar timeOfCompletion;
	private String status;
	private boolean shiftTime = true;


	Order() {
		this.status = "in process";
	}

	/**
	 *
	 * @param minutes
	 */
	Order(Calendar timeOfBegin, int minutes) {
		this.timeOfBegin = timeOfBegin;
		this.timeOfCompletion = (Calendar)this.timeOfBegin.clone();
		this.timeOfCompletion.add(Calendar.MINUTE, minutes);
		this.status = "in process";
	}

	/**
	 *
	 * @param timeOfCompletion
	 */
	Order(Calendar timeOfCompletion) {
		this.timeOfCompletion = timeOfCompletion;
		this.status = "in process";
	}

	/**
	 *
	 * @param timeOfBegin
	 * @param timeOfCompletion
	 */
	Order(Calendar timeOfBegin, Calendar timeOfCompletion) {
		this.timeOfBegin = timeOfBegin;
		this.timeOfCompletion = timeOfCompletion;
		this.status = "in process";
	}

	/**
	 *
	 * @param timeOfCompletion
	 * @param status
	 * @param shiftTime
	 */
	Order(Calendar timeOfCompletion, String status, boolean shiftTime) {
		this.timeOfCompletion = timeOfCompletion;
		this.status = status;
		this.shiftTime = shiftTime;
	}

	/**
	 *
	 * @param timeOfBegin
	 * @param timeOfCompletion
	 * @param status
	 * @param shiftTime
	 */
	Order(Calendar timeOfBegin, Calendar timeOfCompletion, String status, boolean shiftTime) {
		this.timeOfBegin = timeOfBegin;
		this.timeOfCompletion = timeOfCompletion;
		this.status = status;
		this.shiftTime = shiftTime;
	}

	public Calendar getTimeOfCreated() {
		return timeOfCreated;
	}

	public Calendar getTimeOfBegin() {
		return timeOfBegin;
	}

	/**
	 *
	 * @param timeOfBegin
	 */
	public void setTimeOfBegin(Calendar timeOfBegin) {
		this.timeOfBegin = timeOfBegin;
	}

	public Calendar getTimeOfCompletion() {
		return this.timeOfCompletion;
	}

	/**
	 *
	 * @param timeOfCompletion
	 */
	public void setTimeOfCompletion(Calendar timeOfCompletion) {
		this.timeOfCompletion = timeOfCompletion;
	}

	/**
	 *
	 * @param minutes
	 */
	public void setTimeOfCompletion(int minutes) {
		this.timeOfCompletion = (Calendar)this.timeOfBegin.clone();
		this.timeOfCompletion.add(Calendar.MINUTE, minutes);
	}

	public String getStatus() {
		return this.status;
	}

	/**
	 *
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isShiftTime() {
		return shiftTime;
	}

	/**
	 *
	 * @param shiftTime
	 */
	public void setShiftTime(boolean shiftTime) {
		this.shiftTime = shiftTime;
	}

	/**
	 *
	 * @param minutes
	 */
	public void shiftTimeOfOrder(int minutes) {
		if (this.shiftTime) {
			this.timeOfBegin.add(Calendar.MINUTE, minutes);
			this.timeOfCompletion.add(Calendar.MINUTE, minutes);
		}
	}

	@Override
	public String toString() {
		return "Order{" +
				"timeOfCreated=" + timeOfCreated.getTime() +
				", timeOfBegin=" + timeOfBegin.getTime() +
				", timeOfCompletion=" + timeOfCompletion.getTime() +
				", status='" + status + '\'' +
				'}';
	}
}