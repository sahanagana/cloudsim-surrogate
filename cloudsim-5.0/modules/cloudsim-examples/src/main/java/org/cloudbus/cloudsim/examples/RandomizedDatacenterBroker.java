package org.cloudbus.cloudsim.examples;

import java.util.ArrayList;
import java.util.Random;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;

public class RandomizedDatacenterBroker extends DatacenterBroker{
	public ArrayList<Vm> vmList;
	public ArrayList<Integer> cloudletAllocations;
	public ArrayList<Integer> vmAllocations;
	private Random rand;

    public RandomizedDatacenterBroker(String name) throws Exception {
		super(name);
		vmList = new ArrayList<>();
		cloudletAllocations = new ArrayList<>();
		vmAllocations = new ArrayList<>();
		rand = new Random();
	}
	
	public void submitVmList(ArrayList<Vm> vmList) {
		this.vmList = vmList;
		super.submitVmList(vmList);
	}

	public void submitCloudletList(ArrayList<Cloudlet> cloudletList) {
		for (Cloudlet i : cloudletList) {
			i.setVmId(rand.nextInt(vmList.size()));
		}
		super.submitCloudletList(cloudletList);
	}
}
