/**
 * 
 */
package Model;

/**
 * Customer has three time stamps. With these stamps, system states could be calculated
 *
 */
class Customer extends Task {
	protected boolean flag;
	private static int customerID = 1;
	private long enterServer;
	private Server temp = null;

	/**
	 * @param type
	 * @param id
	 */
	Customer() {
		this.flag = false;
		this.type = "CUSTOMER";
		this.id = customerID++;
		this.initialTime = Controller.clock.CLOCK();
	}

	// If the customer is accepted by the system, then the flag = true
	// If it's rejected, the flag remains false
	protected void setFlag() {
		flag = true;
	}

	protected void timeEnterServer() {
		this.enterServer = Controller.clock.CLOCK();
	}

	protected void setTerminalTime() {
		this.terminalTime = Controller.clock.CLOCK();
	}

	// This method is used to investigate the average time being spent
	// in the system by each accepted customer
	protected long timeInSystem() {
		
		long TIS = this.terminalTime - this.initialTime;
		if (TIS < 0) {
			System.out.println("IMPOSSIBLE, Time In SYSTEM Could not be negative");
			System.out.println(this.terminalTime + " " + this.initialTime);
			System.exit(-1);
		}
		return TIS;

	}

	// This method is used to investigate the average service time being spent
	// in the system by
	// each accepted customer
	protected long timeInServer() {
		
		long TISE = this.terminalTime - this.enterServer;
		if (TISE < 0) {
			System.out.println("IMPOSSIBLE, Time In SERVER could not be negative");
			System.out.println(this.terminalTime + " " + this.enterServer);
			System.exit(-1);
		}
		return TISE;
	}

	// This method is used to investigate the average waiting time being spent
	// in the system by each accepted customer
	protected long timeInQueue() {
	
		long TIQ = this.enterServer - this.initialTime;
		if (TIQ < 0) {
			System.out.println("IMPOSSIBLE, Time In QUEUE could not be negative");
			System.out.println(this.enterServer + " " + this.initialTime);
			System.exit(-1);
		}
		return TIQ;
	}

	protected void setTimeEnteringServer() {
		this.enterServer = Controller.clock.CLOCK();
	}

	protected void getServerTrigger(Server s) {
		System.out.println("temp hold by " + this.toString() + " takes " + s.toString());
		temp = s;
	}

	public String toString() {
		return this.type + this.id;
	}

	// Customer itself is a TimerTask. The host server schedules a service time for it
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.setTerminalTime();
		temp.popTaskOut();
	}
}
