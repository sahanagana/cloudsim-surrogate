package org.cloudbus.cloudsim.examples.power.random;

import java.util.ArrayList;
import java.util.Random;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;

public class RandomizedDatacenterBroker extends DatacenterBroker{
	public ArrayList<Vm> vmList;
	public ArrayList<Integer> cloudletAllocations;
	private Random rand;

    public RandomizedDatacenterBroker(String name) throws Exception {
		super(name);
		vmList = new ArrayList<>();
		cloudletAllocations = new ArrayList<>();
		rand = new Random();
	}

	public void submitCloudletList(ArrayList<Cloudlet> cloudletList) {
		for (Cloudlet i : cloudletList) {
			int allocation = rand.nextInt(vmList.size());
			i.setVmId(allocation);
			cloudletAllocations.add(allocation);
		}
		super.submitCloudletList(cloudletList);
	}
}
