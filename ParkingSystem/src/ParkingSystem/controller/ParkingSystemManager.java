package ParkingSystem.controller;

import java.util.Calendar;
import java.util.UUID;

import ParkingSystem.Entities.CreditCard;
import ParkingSystem.Entities.Gate;
import ParkingSystem.Entities.Status;
import ParkingSystem.Entities.Ticket;
import ParkingSystem.UI.parkingGUI;

public class ParkingSystemManager {

	public GateManagement gatemanagement = new GateManagement();
	public TicketManagement ticketmager = new TicketManagement();
	public PaymentManagement paymanager = new PaymentManagement();
	public OccupancyManagement occupancy = new OccupancyManagement();
	public FraudPreventionManagement fraudManager = new FraudPreventionManagement();
	public CreditCard creditcard = new CreditCard();

	public CreditCard getCreditcard() {
		return creditcard;
	}

	public void setCreditcard(CreditCard creditcard) {
		this.creditcard = creditcard;
	}

	public Ticket ticket;
	/*
	public Gate getGate() {
		return gatemanagement.;
	}

	public void setGate(Gate gate) {
		this.gate = gate;
	}
*/
	public ParkingSystemManager() {

	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Status processExitFor(UUID ticketID) {

		Status status = null;
		Ticket ticket = null;
		for (Ticket t : ticketmager.getTicketcollection()) {
			if (t.getTicektID().compareTo(ticketID) == 0) {
				ticket = t;
			}
			// TODO add your handling code here:
		}

		//if (fraudManager.isValidTicet(ticket)	&& ticket.getIsPaid() == true) {
		if (fraudManager.checkNoExitWithoutPay(ticket)) {
			
			ticket.deactivatetheTicektStatus();
			
			Gate g1 = gatemanagement.openExitGate(1);

			fraudManager.ticketgatecollection.put(ticket, g1);

			
			Gate g2 = gatemanagement.closeExitGate(1);
			
			occupancy.decrementOcccupancy();

			fraudManager.ticketgatecollection.put(ticket, g2);

			status = new Status(true, "Vehicle exited from gate.");

		} else {
			status = new Status(true, "Unknown vehicle. Inform security.");
		}
		return status;
	}

	/*
	public void gateCloseOpeation(Gate gate) {
		// TODO add your handling code here:

		if (gate.gateStatus == GateStatus.Open) {
			gate.gateStatus = GateStatus.Close;
			// parkingGUI.jTextField3.setText(gate.gateStatus.toString());
		}

		if (gate.gateStatus == GateStatus.Open) {
			gate.gateStatus = GateStatus.Close;
			// parkingGUI.jTextField3.setText(gate.gateStatus.toString());
		} else {
			// parkingGUI.jTextField3.setText(gate.gateStatus.toString());
		}

	}
*/
	public void printTicketOperation() {

		// TODO add your handling code here:

		final Ticket ticket = ticketmager.createTicket();

		ticket.activatetheTicektStatus();
		ticket.generateTicketID();

		if (fraudManager.isValidTicet(ticket)) {
			this.ticket = ticket;

			occupancy.incrementOcccupancy();

			Thread ts = new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						Thread.sleep(2000);

					} catch (InterruptedException e) {

					}

				}
			});

			ts.start();
		}

	}

	public void calculateFare(parkingGUI parkingGUI) {
		// TODO add your handling code here:
		Calendar c = Calendar.getInstance();

		java.util.Date currenttime = c.getTime();
		
        //TODO:Disply hourly rate through system.
	
		
		ticket.setExitTime(currenttime);
		paymanager.setHourlyRate(11);

		long rate = paymanager.calculateParkingDuration(ticket);
		ticket.setTicketAmount(rate);

		// objticketmanager.gatemanagement.ExitGate(1);

		parkingGUI.jTextField5.setText(Long.toString(rate));
	}

	public float processPayment(Ticket ticket) {

		CreditCard crdeitcard = new CreditCard();

		// associating ticket id to credit card id

		crdeitcard.setTicketID(ticket.getTicektID());
		
		if(fraudManager.isValidTicet(ticket))

		paymanager.Pay(ticket);

		float ticektAmount = ticket.getTicketAmount();

		return ticektAmount;
	}

	public ParkingSystemManager(parkingGUI parkingGUI) {

	}

}